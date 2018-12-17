package vedam.subkuch.locationProvider;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.LogUtils;

/**
 * Created by nansari on 6/22/2016.
 */
public class FetchAddressIntentService extends IntentService {

    public final String TAG = "TAG_FETCH_ADDRESS";
    protected ResultReceiver mReceiver;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FetchAddressIntentService() {
        super("Fetch Address");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mReceiver = intent.getParcelableExtra(Constants.EXTRA_RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            LogUtils.LOGE(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        // Get the location passed to this service through an extra.
        double latitude = intent.getDoubleExtra(Constants.EXTRA_LOCATION_LATITUDE, 0.0);
        double longitude = intent.getDoubleExtra(Constants.EXTRA_LOCATION_LONGITUDE, 0.0);

        // Make sure that the location data was really sent over through an extra. If it wasn't,
        // send an error error message and return.
        if (latitude == 0.0 && longitude == 0.0) {
            errorMessage = getString(R.string.no_location_data_provided);
            LogUtils.LOGE(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
            return;
        }

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
 Crashlytics.logException(ioException);
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            LogUtils.LOGE(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
 Crashlytics.logException(illegalArgumentException);
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            LogUtils.LOGE(TAG, errorMessage + ". " +
                    "Latitude = " + latitude +
                    ", Longitude = " +
                    longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                LogUtils.LOGE(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, null);
        } else {
            Address address = addresses.get(0);

            deliverResultToReceiver(Constants.SUCCESS_RESULT, address);
        }
    }

    private void deliverResultToReceiver(int resultCode, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_ADDRESS, address);
        mReceiver.send(resultCode, bundle);
    }

}
