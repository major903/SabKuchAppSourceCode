package vedam.subkuch.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import vedam.subkuch.network.models.RegistrationMasterOption;

/**
 * Persistent app-level cache for registration master data.
 *
 * <p>The API explicitly disables HTTP caching, so these values are kept in a
 * separate preferences file. Cached values remain available when stale or
 * offline; freshness only determines whether the app should refresh them.</p>
 */
public final class RegistrationMasterCache {
    private static final String PREFS_NAME = "registration_master_cache";
    private static final String STATES = "states";
    private static final String DISTRICTS = "districts";
    private static final String LANGUAGES = "languages";
    private static final long MAX_AGE_MILLIS = TimeUnit.HOURS.toMillis(24);
    private static final Type OPTION_LIST_TYPE =
            new TypeToken<ArrayList<RegistrationMasterOption>>() { }.getType();

    private RegistrationMasterCache() {
    }

    public static ArrayList<RegistrationMasterOption> getDistricts(Context context) {
        return read(context, DISTRICTS);
    }

    public static ArrayList<RegistrationMasterOption> getStates(Context context) {
        return read(context, STATES);
    }

    public static ArrayList<RegistrationMasterOption> getLanguages(Context context) {
        return read(context, LANGUAGES);
    }

    public static void putDistricts(Context context, List<RegistrationMasterOption> districts) {
        write(context, DISTRICTS, districts);
    }

    public static void putStates(Context context, List<RegistrationMasterOption> states) {
        write(context, STATES, states);
    }

    public static void putLanguages(Context context, List<RegistrationMasterOption> languages) {
        write(context, LANGUAGES, languages);
    }

    public static boolean areDistrictsFresh(Context context) {
        return isFresh(context, DISTRICTS);
    }

    public static boolean areStatesFresh(Context context) {
        return isFresh(context, STATES);
    }

    public static boolean areLanguagesFresh(Context context) {
        return isFresh(context, LANGUAGES);
    }

    private static ArrayList<RegistrationMasterOption> read(Context context, String key) {
        SharedPreferences preferences = preferences(context);
        String json = preferences.getString(dataKey(key), null);
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            ArrayList<RegistrationMasterOption> values = new Gson().fromJson(json, OPTION_LIST_TYPE);
            return values == null ? new ArrayList<>() : values;
        } catch (JsonSyntaxException | ClassCastException exception) {
            preferences.edit().remove(dataKey(key)).remove(timestampKey(key)).apply();
            return new ArrayList<>();
        }
    }

    private static void write(
            Context context,
            String key,
            List<RegistrationMasterOption> values
    ) {
        if (values == null || values.isEmpty()) {
            return;
        }

        preferences(context).edit()
                .putString(dataKey(key), new Gson().toJson(values, OPTION_LIST_TYPE))
                .putLong(timestampKey(key), System.currentTimeMillis())
                .apply();
    }

    private static boolean isFresh(Context context, String key) {
        long savedAt = preferences(context).getLong(timestampKey(key), 0L);
        long age = System.currentTimeMillis() - savedAt;
        return savedAt > 0L && age >= 0L && age < MAX_AGE_MILLIS;
    }

    private static SharedPreferences preferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static String dataKey(String key) {
        return key + "_data";
    }

    private static String timestampKey(String key) {
        return key + "_saved_at";
    }
}
