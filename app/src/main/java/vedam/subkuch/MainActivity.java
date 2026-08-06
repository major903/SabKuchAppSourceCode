package vedam.subkuch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.utils.AppPrefs;

public class  MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppPrefs.getInstance(this).getSharedPreferences()
                .getBoolean(AppPrefs.PREFS_IF_USER_LOGGED_IN, false)) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, RegisterUserActivity.class));
        }
        finish();
    }
}
