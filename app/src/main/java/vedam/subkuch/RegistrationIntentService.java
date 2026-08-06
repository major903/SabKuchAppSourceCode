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
import androidx.core.app.JobIntentService;

import vedam.subkuch.network.Response;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.PushNotificationRequest;
import vedam.subkuch.network.models.classifieds.AddClassifiedResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.DeviceIdProvider;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;


public class RegistrationIntentService extends JobIntentService {

    //GCM constants
    private static final String TAG = "RegIntentService";

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, RegistrationIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        try {

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            LogUtils.LOGD(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            String token = task.getResult();
                            LogUtils.LOGI(TAG, "FCM Registration Token: " + token);

                            boolean isUserLoggedIn = AppPrefs.getIsLoggedIn(getApplicationContext());

                            if (isUserLoggedIn)
                                sendRegistrationToServer(token);
                        }
                    });

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            LogUtils.LOGD(TAG, "Failed to complete token refresh", e);
        }
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        PushNotificationRequest request = new PushNotificationRequest();
        request.setUserId(AppPrefs.getPrefsUserId(this));
        request.setToken(token);
        final String deviceId = DeviceIdProvider.getDeviceId(this);
        request.setDeviceId(deviceId);
        DataFetcher.registerForPush(this, new Gson().toJson(request), onRegisterPushSuccessListener, AddClassifiedResponse.class, null);
    }

    private Response.Listener<AddClassifiedResponse> onRegisterPushSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE)
            AppPrefs.setPrefsIsTokenSent(this, true);
    };


}
