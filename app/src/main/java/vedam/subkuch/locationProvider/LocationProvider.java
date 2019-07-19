package vedam.subkuch.locationProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.ScreenChangeListener;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

/**
 * Created by nansari on 6/20/2016.
 */
public enum LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * creates a singleton of {@link LocationProvider}
     */
    locationProvider;

    private static GoogleApiClient mGoogleApiClient;
    private boolean isAddressRequested = false;
    private Activity activity;
    private LocationCallbacks locationCallbacks;
    private ScreenChangeListener screenChangeListener;
    private LocationRequest mLocationRequest;
    private static Object object = new Object();

    LocationProvider() {

    }

    /**
     * @return Singleton Instance of Location provider
     */
    public static LocationProvider getInstance() {

        /*if(locationProvider == null){
            synchronized (LocationProvider.class) {
                if(locationProvider == null){
                    locationProvider = new LocationProvider();
                }
            }
        }*/
        return locationProvider;
    }

    /**
     * Initializes location callback and the Google API client
     */
    private void init() {

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    /**
     * method which connects the Google API client.
     */
    private void connect() {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        else
            attemptLocationFetch();
    }

    /**
     * method which disconnects the Google API client.
     */
    private void disconnect() {

        mGoogleApiClient.disconnect();

    }

    /**
     * Initializes the LocationProvider and then requests location from the LocationProvider by connecting.
     *
     * @param locationCallbacks The activity which requests the location and implements {@link LocationCallbacks}
     */
    public synchronized void requestLocation(LocationCallbacks locationCallbacks) {
        isAddressRequested = false;
        request(locationCallbacks);
    }

    /**
     * Initializes the LocationProvider and then requests BusinessAddress from the LocationProvider by connecting.
     *
     * @param locationCallbacks The activity which requests the BusinessAddress and implements {@link LocationCallbacks}
     */
    public synchronized void requestAddress(LocationCallbacks locationCallbacks) {
        LogUtils.LOGD("LocationProvider", "address requested");
        isAddressRequested = true;
        request(locationCallbacks);
    }

    private void request(LocationCallbacks locationCallbacks) {

        this.activity = (Activity) locationCallbacks;

        if (locationCallbacks instanceof ScreenChangeListener) {
            this.locationCallbacks = locationCallbacks;
            this.screenChangeListener = (ScreenChangeListener) locationCallbacks;
            init();
            connect();
//            createLocationRequest();
        } else {
            throw new IllegalArgumentException("This Activity must implement LocationCallbacks and ScreenChangeLister to request Location");
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        attemptLocationFetch();

    }

    private void attemptLocationFetch() {
        LogUtils.LOGD(BaseActivity.TAG, "Connected");

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (!AppUtil.checkPermissions(activity, permissions)) {
            if (locationCallbacks != null)
                locationCallbacks.onNoLocationPermission();
        } else {

            createLocationRequest();

        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();

                //final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        startLocationUpdates();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        if (locationCallbacks != null)
                            locationCallbacks.onGpsOff(status);
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        if (checkGPSManually())
                            startLocationUpdates();
                        break;
                }
            }
        });
    }

    /**
     * @return if GPS is enabled or not
     */
    private boolean checkGPSManually() {

        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Crashlytics.logException(ex);
            ex.printStackTrace();
        }

        if (!gps_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage(activity.getResources().getString(R.string.gps_network_not_enabled))
                    .setTitle(activity.getString(R.string.app_name))
                    .setCancelable(false);
            dialog.setPositiveButton(activity.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    if (screenChangeListener != null)
                        screenChangeListener.handleActivityIntent(myIntent);
                    //get gps
                }
            });

            dialog.show();
            return false;
        } else
            return true;
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

//        toast("Fetching your current location. This may take time.");
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onLocationChanged(Location location) {

        if (locationCallbacks != null)
            locationCallbacks.onLocationChanged(location);

        if (!Geocoder.isPresent()) {
            UiUtil.showToast(activity, activity.getString(R.string.geocoder_not_available));
            return;
        }

        if (!isAddressRequested)
            disconnect();
        else {
            if (location != null)
                fetchAddress(location);
        }

    }

    private void fetchAddress(Location location) {

        //Pass main looper so that the result is called on main thread
        AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler(Looper.getMainLooper()), locationCallbacks);

        Intent intent = new Intent(activity, FetchAddressIntentService.class);
        intent.putExtra(Constants.EXTRA_RECEIVER, mResultReceiver);
        intent.putExtra(Constants.EXTRA_LOCATION_LATITUDE, location.getLatitude());
        intent.putExtra(Constants.EXTRA_LOCATION_LONGITUDE, location.getLongitude());

        if (screenChangeListener != null)
            screenChangeListener.handleServiceIntent(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        LogUtils.LOGD("LocationProvider", "suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogUtils.LOGD("LocationProvider", "failed");
    }
}
