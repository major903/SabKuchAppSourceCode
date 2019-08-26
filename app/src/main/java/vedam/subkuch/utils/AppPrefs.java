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

    public static String getPrefsIsReferralDone(Context context) {
        return getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_IS_REFERRAL_DONE, "");
    }

    public static void setPrefsIsReferralDone(Context context, String value) {
        getInstance(context).getSharedPreferences().edit().putString(AppPrefs.PREFS_IS_REFERRAL_DONE, value).apply();
    }

    public static boolean getPrefsIsTokenSent(Context context) {
        return getInstance(context).getSharedPreferences().getBoolean(AppPrefs.PREFS_TOKEN_SENT_TO_SERVER, false);
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
