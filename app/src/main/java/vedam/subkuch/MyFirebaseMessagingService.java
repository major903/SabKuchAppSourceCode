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

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyInstanceIDLS";
    private SharedPreferences sharedPreferences;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

       /* boolean registeredInApp = sharedPreferences
                .getBoolean(PREFS_REGISTERED_IN_APP, false);

        if (registeredInApp) {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            sendRegistrationToServer(refreshedToken);
        }*/
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
        // Tag used to cancel the request
        /*String tag_json_obj = "json_gcm_req";

        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        final String phoneNumber = sharedPreferences.getString(PREFS_PHONE_NUMBER, "9343260001");
        final String countryCode = sharedPreferences.getString(PREFS_COUNTRY_CODE, "91");

        Map<String, String> params = new HashMap<>();
        params.put(DEVICE_ID, device_id);
        params.put(TOKEN_ID, token);
        params.put(DEVICE_TYPE, "Android");
        params.put(LOGIN_TYPE, Constants.CONTROLLER_NAME);
        params.put(PhoneNumber, phoneNumber);
        params.put(CountryCode, countryCode);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DEVICE_REGISTRY_API, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("GCM success" + response.toString());
                            if (response.getString(ReturnMessage).equalsIgnoreCase("success") ||
                                    response.getString(ReturnMessage).equalsIgnoreCase("updated")) {
                                // You should store a boolean that indicates whether the generated token has been
                                // sent to your server. If the boolean is false, send the token to your server,
                                // otherwise your server should have already received the token.
                                sharedPreferences.edit().putBoolean(PREFS_SENT_TOKEN_TO_SERVER, true).apply();
                                Toast.makeText(getApplicationContext(), "Registered for GCM.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(getApplicationContext(), "GCM not registered.", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(X_AUTH_TOKEN, "dXNlcm5hbWU6cGFzc3dvcmQ=");
                return headers;
            }
        };

// Adding request to request queue
        SabkuchApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);*/
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println(TAG + " " + remoteMessage.getData());
        String from = remoteMessage.getFrom();
        Log.d(TAG, "From: " + from);

        Map<String, String> data = remoteMessage.getData();

        String message = data.get("message");


        sendNotification((int) System.currentTimeMillis(), null, message);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param title   GCM title received.
     * @param message GCM message received.
     */
    private void sendNotification(int id, String title, String message) {
        /*Intent intent = intent = new Intent(this, MainActivity.class);
        ;

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = Constants.NOTIFICATION_CHANNEL_ID;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, Constants.NOTIFICATION_CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.notification_icon)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(
                        new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        if (title == null)
            notificationBuilder.setContentTitle(getString(R.string.app_name));
        else
            notificationBuilder.setContentTitle(title);

        if (notificationManager != null)
            notificationManager.notify(id, notificationBuilder.build());*/
    }
}
