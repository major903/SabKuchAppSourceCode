package vedam.subkuch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class AppPrefs {

    public static final String PREFS_IF_USER_LOGGED_IN = "PREFS_IF_USER_LOGGED_IN";
    public static final String PREFS_IS_REFERRAL_DONE = "PREFS_IS_REFERRAL_DONE";
    public static final String PREFS_USER_NAME = "PREFS_USER_NAME";
    public static final String PREFS_USER_ID = "PREFS_USER_ID";
    public static final String PREFS_TOKEN = "PREFS_TOKEN";
    public static final String PREFS_TOKEN_SENT_TO_SERVER = "PREFS_TOKEN_SENT_TO_SERVER";
    public static final String PREFS_APP_LANGUAGE_ID = "PREFS_APP_LANGUAGE_ID";
    public static final String PREFS_APP_LANGUAGE_NAME = "PREFS_APP_LANGUAGE_NAME";
    public static final String PREFS_HOME_FEATURES = "PREFS_HOME_FEATURES";
    public static final String PREFS_USER_GENDER = "PREFS_USER_GENDER";
    public static final String PREFS_USER_GENDER_CURRENT_API = "PREFS_USER_GENDER_CURRENT_API";

    private static final String APP_PREFS = "Vedam.SubKuch";
    private static AppPrefs instance;
    private SharedPreferences sharedPreferences;

    private AppPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
    }

    public static String getPrefsUserId(Context context) {
        return getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
    }


    public static String getPrefsToken(Context context) {
        return getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_TOKEN, "");
    }

    public static String getPrefsUserName(Context context) {
        return getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_NAME, "");
    }

    /** Returns 0 (any/unknown), 1 (male), or 2 (female). */
    public static int getPrefsUserGender(Context context) {
        return getInstance(context).getSharedPreferences().getInt(PREFS_USER_GENDER, 0);
    }

    public static boolean hasPrefsUserGender(Context context) {
        return getInstance(context).getSharedPreferences().contains(PREFS_USER_GENDER);
    }

    public static boolean isUserGenderFromCurrentApi(Context context) {
        return getInstance(context).getSharedPreferences().getBoolean(PREFS_USER_GENDER_CURRENT_API, false);
    }

    public static String getPrefsIsReferralDone(Context context) {
        return getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_IS_REFERRAL_DONE, "");
    }

    public static void setPrefsIsReferralDone(Context context, String value) {
        getInstance(context).getSharedPreferences().edit().putString(AppPrefs.PREFS_IS_REFERRAL_DONE, value).apply();
    }

    public static boolean getPrefsIsTokenSent(Context context) {
        return getInstance(context).getSharedPreferences().getBoolean(AppPrefs.PREFS_TOKEN_SENT_TO_SERVER, false);
    }

    public static String getPrefsHomeFeatures(Context context) {
        return getInstance(context).getSharedPreferences().getString(PREFS_HOME_FEATURES, "");
    }

    public static void setPrefsHomeFeatures(Context context, String value) {
        getInstance(context).getSharedPreferences().edit().putString(PREFS_HOME_FEATURES, value).apply();
    }

    public static void setPrefsIsTokenSent(Context context, boolean value) {
        getInstance(context).getSharedPreferences().edit().putBoolean(AppPrefs.PREFS_TOKEN_SENT_TO_SERVER, value).apply();
    }

    public static boolean getIsLoggedIn(Context context) {
        return getInstance(context).getSharedPreferences().getBoolean(AppPrefs.PREFS_IF_USER_LOGGED_IN, false);
    }

    public static AppPrefs getInstance(Context context) {
        if (instance == null) {
            instance = new AppPrefs(context);
        }
        return instance;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


}
