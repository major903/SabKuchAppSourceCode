package vedam.subkuch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

/**
 * Supplies the device identifier sent to the backend.
 *
 * <p>The ID is generated once per app installation and is not a hardware identifier.
 * This avoids exposing a device-level identifier to the backend.</p>
 */
public final class DeviceIdProvider {

    private static final String PREFERENCES_NAME = "app_device_id";
    private static final String KEY_DEBUG_DEVICE_ID = "value";

    private DeviceIdProvider() {
        // Utility class.
    }

    public static String getDeviceId(Context context) {
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
