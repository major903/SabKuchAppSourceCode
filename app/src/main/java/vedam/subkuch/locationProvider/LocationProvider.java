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
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

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
public enum LocationProvider {

    /**
     * creates a singleton of {@link LocationProvider}
     */
    locationProvider;

    private boolean isAddressRequested = false;
    private Activity activity;
    private LocationCallbacks locationCallbacks;
    private ScreenChangeListener screenChangeListener;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationCallback locationCallback;
    private CancellationTokenSource currentLocationCancellationToken;
    private boolean locationDelivered;
    private final Handler locationHandler = new Handler(Looper.getMainLooper());
    private final Runnable locationUpdateTimeout = this::stopLocationUpdates;

    private static final long MAX_CACHED_LOCATION_AGE_MS = 60_000L;
    private static final long CURRENT_LOCATION_TIMEOUT_MS = 10_000L;

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
     * Initializes the current fused location and settings clients.
     */
    private void init() {
        Context applicationContext = activity.getApplicationContext();
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext);
        }
        if (settingsClient == null) {
            settingsClient = LocationServices.getSettingsClient(applicationContext);
        }
        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        deliverLocation(locationResult.getLastLocation());
                    }
                }
            };
        }
    }

    private void disconnect() {
        cancelCurrentLocationRequest();
        stopLocationUpdates();
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
        resetLocationRequestState();

        if (locationCallbacks instanceof ScreenChangeListener) {
            this.locationCallbacks = locationCallbacks;
            this.screenChangeListener = (ScreenChangeListener) locationCallbacks;
            init();
            attemptLocationFetch();
        } else {
            throw new IllegalArgumentException("This Activity must implement LocationCallbacks and ScreenChangeLister to request Location");
        }

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
        locationRequest = new LocationRequest.Builder(2_000L)
                .setMinUpdateIntervalMillis(1_000L)
                .setMaxUpdates(1)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(activity, response -> requestCurrentLocation())
                .addOnFailureListener(activity, exception -> {
                    if (exception instanceof ResolvableApiException && locationCallbacks != null) {
                        locationCallbacks.onGpsOff((ResolvableApiException) exception);
                    } else if (checkGPSManually()) {
                        requestCurrentLocation();
                    } else {
                        FirebaseCrashlytics.getInstance().recordException(exception);
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
            FirebaseCrashlytics.getInstance().recordException(ex);
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
        if (!locationDelivered && fusedLocationClient != null && locationRequest != null) {
            locationHandler.removeCallbacks(locationUpdateTimeout);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                    Looper.getMainLooper());
            locationHandler.postDelayed(locationUpdateTimeout, CURRENT_LOCATION_TIMEOUT_MS);
        }

    }

    /**
     * Requests one location using the fused client. A recent cached estimate is returned immediately;
     * otherwise Google Play services computes a new high-accuracy fix.
     */
    private void requestCurrentLocation() {
        if (fusedLocationClient == null || locationDelivered) {
            return;
        }

        cancelCurrentLocationRequest();
        final CancellationTokenSource cancellationToken = new CancellationTokenSource();
        currentLocationCancellationToken = cancellationToken;

        CurrentLocationRequest request = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMaxUpdateAgeMillis(MAX_CACHED_LOCATION_AGE_MS)
                .setDurationMillis(CURRENT_LOCATION_TIMEOUT_MS)
                .build();

        fusedLocationClient.getCurrentLocation(request, cancellationToken.getToken())
                .addOnSuccessListener(activity, location -> {
                    if (cancellationToken != currentLocationCancellationToken || locationDelivered) {
                        return;
                    }

                    if (location != null) {
                        deliverLocation(location);
                    } else {
                        startLocationUpdates();
                    }
                })
                .addOnFailureListener(activity, exception -> {
                    if (cancellationToken == currentLocationCancellationToken && !locationDelivered) {
                        FirebaseCrashlytics.getInstance().recordException(exception);
                        startLocationUpdates();
                    }
                });
    }

    private synchronized void resetLocationRequestState() {
        locationDelivered = false;
        cancelCurrentLocationRequest();
        stopLocationUpdates();
    }

    private void cancelCurrentLocationRequest() {
        if (currentLocationCancellationToken != null) {
            currentLocationCancellationToken.cancel();
            currentLocationCancellationToken = null;
        }
    }

    private void stopLocationUpdates() {
        locationHandler.removeCallbacks(locationUpdateTimeout);
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private synchronized void deliverLocation(Location location) {
        if (location == null || locationDelivered) {
            return;
        }

        locationDelivered = true;
        cancelCurrentLocationRequest();
        stopLocationUpdates();

        if (locationCallbacks != null)
            locationCallbacks.onLocationChanged(location);

        if (!Geocoder.isPresent()) {
            UiUtil.showToast(activity, activity.getString(R.string.geocoder_not_available));
            return;
        }

        if (isAddressRequested)
            fetchAddress(location);

        disconnect();

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

}
