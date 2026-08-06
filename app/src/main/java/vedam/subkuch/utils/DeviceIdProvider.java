package vedam.subkuch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.util.UUID;

import vedam.subkuch.BuildConfig;

/**
 * Supplies the device identifier sent to the backend.
 *
 * <p>Release builds use Android's stable device ID. Debug builds use an ID generated
 * for this app installation, allowing test registrations without colliding with a
 * real account already associated with the physical device.</p>
 */
public final class DeviceIdProvider {

    private static final String PREFERENCES_NAME = "debug_device_id";
    private static final String KEY_DEBUG_DEVICE_ID = "value";

    private DeviceIdProvider() {
        // Utility class.
    }

    public static String getDeviceId(Context context) {
        if (!BuildConfig.DEBUG) {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        String deviceId = preferences.getString(KEY_DEBUG_DEVICE_ID, null);
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString();
            preferences.edit().putString(KEY_DEBUG_DEVICE_ID, deviceId).apply();
        }
        return deviceId;
    }
}
