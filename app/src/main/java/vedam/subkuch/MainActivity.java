package vedam.subkuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.utils.AppPrefs;

public class MainActivity extends AppCompatActivity {
    private boolean navigating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = AppPrefs.getInstance(this).getSharedPreferences();
        boolean isMarkedLoggedIn = preferences.getBoolean(AppPrefs.PREFS_IF_USER_LOGGED_IN, false);
        boolean hasSession = !TextUtils.isEmpty(preferences.getString(AppPrefs.PREFS_TOKEN, ""))
                && !TextUtils.isEmpty(preferences.getString(AppPrefs.PREFS_USER_ID, ""));

        if (isMarkedLoggedIn && hasSession) {
            openHome();
        } else {
            resetIncompleteSession(preferences, isMarkedLoggedIn);
            openRegistration();
        }
    }

    private void openHome() {
        if (navigating) return;
        navigating = true;
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void openRegistration() {
        if (navigating) return;
        navigating = true;
        startActivity(new Intent(this, RegisterUserActivity.class));
        finish();
    }

    private void resetIncompleteSession(SharedPreferences preferences, boolean isMarkedLoggedIn) {
        if (!isMarkedLoggedIn) return;
        preferences.edit()
                .putBoolean(AppPrefs.PREFS_IF_USER_LOGGED_IN, false)
                .remove(AppPrefs.PREFS_TOKEN)
                .remove(AppPrefs.PREFS_USER_ID)
                .apply();
    }
}
