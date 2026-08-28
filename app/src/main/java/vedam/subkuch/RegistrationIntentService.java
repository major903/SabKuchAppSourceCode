/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vedam.subkuch;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import vedam.subkuch.network.Response;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.android.gms.tasks.Tasks;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.PushNotificationRequest;
import vedam.subkuch.network.models.classifieds.AddClassifiedResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.DeviceIdProvider;
import vedam.subkuch.utils.LogUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class RegistrationIntentService extends Worker {

    //GCM constants
    private static final String TAG = "RegIntentService";

    public static void enqueueWork(Context context, Intent work) {
        WorkManager.getInstance(context.getApplicationContext())
                .enqueue(new OneTimeWorkRequest.Builder(RegistrationIntentService.class).build());
    }

    public RegistrationIntentService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            String token = Tasks.await(getMessagingToken());
            LogUtils.LOGI(TAG, "FCM Registration Token received");

            if (!AppPrefs.getIsLoggedIn(getApplicationContext())) {
                return Result.success();
            }
            return sendRegistrationToServer(token) ? Result.success() : Result.retry();

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            LogUtils.LOGD(TAG, "Failed to complete token refresh", e);
            return Result.retry();
        }
    }

    @SuppressWarnings("deprecation")
    private com.google.android.gms.tasks.Task<String> getMessagingToken() {
        // The app server still accepts the FCM registration token. Firebase's replacement
        // registration callback provides a Firebase Installation ID, not this token.
        return FirebaseMessaging.getInstance().getToken();
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private boolean sendRegistrationToServer(final String token) throws InterruptedException {
        CountDownLatch requestFinished = new CountDownLatch(1);
        AtomicBoolean requestSucceeded = new AtomicBoolean(false);
        PushNotificationRequest request = new PushNotificationRequest();
        request.setUserId(AppPrefs.getPrefsUserId(getApplicationContext()));
        request.setToken(token);
        final String deviceId = DeviceIdProvider.getDeviceId(getApplicationContext());
        request.setDeviceId(deviceId);
        DataFetcher.registerForPush(getApplicationContext(), new Gson().toJson(request), response -> {
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                requestSucceeded.set(true);
                AppPrefs.setPrefsIsTokenSent(getApplicationContext(), true);
            }
            requestFinished.countDown();
        }, AddClassifiedResponse.class, error -> requestFinished.countDown());
        return requestFinished.await(10, TimeUnit.MINUTES) && requestSucceeded.get();
    }


}
