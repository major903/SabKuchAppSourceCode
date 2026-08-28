package vedam.subkuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.ApiError;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.Response;
import vedam.subkuch.network.models.BaseResponse;
import vedam.subkuch.network.models.MenuPage;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.MenuCache;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MENU_PRELOADED = "vedam.subkuch.extra.MENU_PRELOADED";


    private ProgressBar progress;
    private TextView status;
    private Button retry;
    private boolean loadingMenu;
    private boolean navigating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_menu_bootstrap);

        progress = findViewById(R.id.progress_menu);
        status = findViewById(R.id.tv_menu_status);
        retry = findViewById(R.id.btn_retry_menu);
        retry.setOnClickListener(view -> loadAuthenticatedMenu());

        SharedPreferences preferences = AppPrefs.getInstance(this).getSharedPreferences();
        boolean isMarkedLoggedIn = preferences.getBoolean(AppPrefs.PREFS_IF_USER_LOGGED_IN, false);
        boolean hasSession = !TextUtils.isEmpty(preferences.getString(AppPrefs.PREFS_TOKEN, ""))
                && !TextUtils.isEmpty(preferences.getString(AppPrefs.PREFS_USER_ID, ""));

        if (isMarkedLoggedIn && hasSession) {
            loadAuthenticatedMenu();
        } else {
            resetIncompleteSession(preferences, isMarkedLoggedIn);
            openRegistration();
        }
    }

    private void loadAuthenticatedMenu() {
        if (loadingMenu || navigating) return;
        loadingMenu = true;
        progress.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
        status.setText(R.string.loading_menu);

        Type type = new TypeToken<BaseResponse<MenuPage>>() { }.getType();
        DataFetcher.getMenus(
                this,
                (Response.Listener<BaseResponse<MenuPage>>) this::onMenuLoaded,
                type,
                this::onMenuLoadFailed
        );
    }

    private void onMenuLoaded(BaseResponse<MenuPage> response) {
        loadingMenu = false;
        if (isFinishing() || isDestroyed()) return;
        MenuPage data = response == null ? null : response.getReturnData();
        if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE
                && data != null && data.getMenus() != null) {
            MenuCache.INSTANCE.save(this, data.getMenus());
            openHome();
            return;
        }

        String message = response == null ? null : response.getReturnMessage();
        showMenuLoadFailure(TextUtils.isEmpty(message)
                ? getString(R.string.menu_load_failed) : message);
    }

    private void onMenuLoadFailed(ApiError error) {
        loadingMenu = false;
        if (isFinishing() || isDestroyed()) return;
        showMenuLoadFailure(getString(R.string.menu_load_failed));
    }

    private void showMenuLoadFailure(String message) {
        progress.setVisibility(View.GONE);
        status.setText(message);
        retry.setVisibility(View.VISIBLE);
    }

    private void openHome() {
        if (navigating) return;
        navigating = true;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(EXTRA_MENU_PRELOADED, true);
        startActivity(intent);
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
