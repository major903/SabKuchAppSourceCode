package vedam.subkuch.base;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.Status;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnFragmentInteractionListener;
import vedam.subkuch.interfaces.ScreenChangeListener;
import vedam.subkuch.locationProvider.LocationCallbacks;
import vedam.subkuch.locationProvider.LocationProvider;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.TargetScreen;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.helpers.Constants.PERMISSION_REQUEST_READ_LOCATION;
import static vedam.subkuch.helpers.Constants.REQUEST_CHECK_SETTINGS;


/**
 * Created by msharm6 on 6/12/2016.
 */
public abstract class BaseActivity extends AppCompatActivity implements ScreenChangeListener, OnFragmentInteractionListener,
        LocationCallbacks {

    public static final String TAG = "BaseActivity";
    private Toolbar toolbar;
    private boolean isAddressRequested;
    private boolean shouldForce;

    protected Response.ErrorListener onErrorListener = error -> {

        LogUtils.LOGD("ERROR", error.getMessage());
        onErrorReceived(error);

    };

    protected void onErrorReceived(VolleyError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(this, this.getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(this, this.getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(this, getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError || (error.networkResponse != null &&
                error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED)) {
            logout();
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void parseAndShowError(VolleyError error) {

        UiUtil.showToast(this, getString(R.string.err_occurred));
    }

    @Override
    public void logout() {
        AppPrefs.getInstance(this).getSharedPreferences().edit().clear().apply();
        int flags = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
        startActivity(new Intent(this, RegisterUserActivity.class).addFlags(flags));
        UiUtil.showToast(this, getString(R.string.err_unauthorized));
    }

    @Override
    public void setTitle(@NonNull CharSequence title) {

        boolean isToolbarSet = true;

        if (toolbar == null)
            isToolbarSet = setToolbar();

        if (isToolbarSet) {
            TextView toolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
            toolbarTitle.setText(title);
        }

    }

    @Override
    public void setTitle(int resourceId) {

        boolean isToolbarSet = true;

        if (toolbar == null)
            isToolbarSet = setToolbar();

        if (isToolbarSet) {
            TextView toolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
            toolbarTitle.setText(getString(resourceId));
        }

    }

    /**
     * Set back button on Toolbar
     */
    protected void setToolbarBackButton() {

        if (toolbar == null)
            setToolbar();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AnalyticsManager.setupGoogleAnalyticsForActivity(this, this.getClass().getName());
    }

    private boolean setToolbar() {
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            return true;
        }
        return false;
    }

    public Toolbar getToolbar() {
        if (toolbar == null)
            setToolbar();
        return toolbar;
    }

    public void replaceFragment(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                                @AnimRes int enterAnim, @AnimRes int exitAnim,
                                @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }

        ft.replace(containerId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    public void replaceFragment(final int containerId, Fragment fragment) {

        replaceFragment(containerId, fragment, null, false, 0, 0, 0, 0);
    }

    protected void addFragmentWithAnimation(final int containerId, Fragment fragment, String tag, boolean addToBackStack) {

        addFragment(containerId, fragment, tag, addToBackStack,
                R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void addFragment(final int containerId, Fragment fragment) {

        addFragment(containerId, fragment, null, false, 0, 0, 0, 0);
    }

    public void addFragmentWithStateLoss(final int containerId, Fragment fragment) {

        addFragmentWithStateLoss(containerId, fragment, null, false, 0, 0, 0, 0);
    }

    public void addFragment(final int containerId, Fragment fragment, String tag) {

        addFragment(containerId, fragment, tag, false, 0, 0, 0, 0);
    }

    public void addFragment(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                            @AnimRes int enterAnim, @AnimRes int exitAnim,
                            @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }
        ft.add(containerId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();

    }

    public void addFragmentWithStateLoss(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                            @AnimRes int enterAnim, @AnimRes int exitAnim,
                            @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }
        ft.add(containerId, fragment, tag);
        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onScreenChange(TargetScreen targetScreen, Bundle bundle, boolean finishCurrentActivity, int flags) {
        Intent intent = new Intent(getApplicationContext(), targetScreen.getTargetScreenClass());

        if (bundle != null) intent.putExtras(bundle);

        if (flags != 0)
            intent.addFlags(flags);

        startActivity(intent);
        if (finishCurrentActivity) finish();
    }

    @Override
    public void handleActivityIntent(Intent intent) {

        startActivity(intent);
    }

    @Override
    public void handleServiceIntent(Intent intent) {

        startService(intent);
    }

    @Override
    public void handleActivityForResultIntent(Intent intent, int requestCode) {

        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Request user's current location and delivers the result in the {@link #onLocationChanged(Location)} callback
     */
    @Override
    public void requestLocation(boolean shouldForce) {
        isAddressRequested = false;
        this.shouldForce = shouldForce;
        requestLocationProvider();
    }

    /**
     * Request user's current address and delivers the result in the {@link #onAddressChanged(Address)} callback
     */
    @Override
    public void requestAddress(boolean shouldForce) {
        isAddressRequested = true;
        this.shouldForce = shouldForce;
        requestLocationProvider();
    }

    @Override
    public void setFragmentResult(int result, Intent data) {
        setResult(result, data);
        finishActivity();
    }


    @Override
    public void finishActivity() {
        finish();
    }

    //Start Location

    private void requestLocationProvider() {
        LocationProvider locationProvider = LocationProvider.getInstance();
        if (isAddressRequested)
            locationProvider.requestAddress(this);
        else
            locationProvider.requestLocation(this);
    }

    @Override
    public void onNoLocationPermission() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            UiUtil.showDialog(BaseActivity.this, getString(R.string.allow_location_permission),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    PERMISSION_REQUEST_READ_LOCATION);
                        }
                    }, false);
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_READ_LOCATION);
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onAddressChanged(Address address) {
    }

    @Override
    public void onGpsOff(Status status) {
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            status.startResolutionForResult(
                    this,
                    REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            Crashlytics.logException(e);
            // Ignore the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                if (resultCode == RESULT_OK) {
                    requestLocationProvider();
                } else
                    finish();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                requestLocationProvider();

            } else {
                if (shouldForce) {
                    startAppSettings();
                    finish();
                }
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
