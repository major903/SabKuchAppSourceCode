package vedam.subkuch.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class AppPrefs {

    public static final String PREFS_IF_USER_LOGGED_IN = "PREFS_IF_USER_LOGGED_IN";
    public static final String PREFS_USER_NAME = "PREFS_USER_NAME";
    public static final String PREFS_USER_ID = "PREFS_USER_ID";
    public static final String PREFS_TOKEN = "PREFS_TOKEN";

    private static final String APP_PREFS = "Vedam.SubKuch";
    private static AppPrefs instance;
    private SharedPreferences sharedPreferences;

    private AppPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFS, Activity.MODE_PRIVATE);
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
