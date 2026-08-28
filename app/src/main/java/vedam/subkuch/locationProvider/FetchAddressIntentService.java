package vedam.subkuch.locationProvider;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.content.IntentCompat;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.LogUtils;

/** Resolves a coordinate and returns the first matching address to the caller. */
public class FetchAddressIntentService extends Service {

    private static final String TAG = "TAG_FETCH_ADDRESS";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            executor.execute(() -> {
                handleIntent(intent);
                stopSelfResult(startId);
            });
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }

    private void handleIntent(Intent intent) {
        ResultReceiver receiver = IntentCompat.getParcelableExtra(
                intent, Constants.EXTRA_RECEIVER, ResultReceiver.class);

        if (receiver == null) {
            LogUtils.LOGE(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String errorMessage = "";
        double latitude = intent.getDoubleExtra(Constants.EXTRA_LOCATION_LATITUDE, 0.0);
        double longitude = intent.getDoubleExtra(Constants.EXTRA_LOCATION_LONGITUDE, 0.0);

        if (latitude == 0.0 && longitude == 0.0) {
            errorMessage = getString(R.string.no_location_data_provided);
            LogUtils.LOGE(TAG, errorMessage);
            deliverResultToReceiver(receiver, Constants.FAILURE_RESULT, null);
            return;
        }

        List<Address> addresses = null;
        try {
            addresses = getAddresses(geocoder, latitude, longitude);
        } catch (IOException ioException) {
            FirebaseCrashlytics.getInstance().recordException(ioException);
            errorMessage = getString(R.string.service_not_available);
            LogUtils.LOGE(TAG, errorMessage, ioException);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            FirebaseCrashlytics.getInstance().recordException(interruptedException);
            errorMessage = getString(R.string.service_not_available);
            LogUtils.LOGE(TAG, errorMessage, interruptedException);
        } catch (IllegalArgumentException illegalArgumentException) {
            FirebaseCrashlytics.getInstance().recordException(illegalArgumentException);
            errorMessage = getString(R.string.invalid_lat_long_used);
            LogUtils.LOGE(TAG, errorMessage + ". Latitude = " + latitude + ", Longitude = " + longitude,
                    illegalArgumentException);
        }

        if (addresses == null || addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                LogUtils.LOGE(TAG, errorMessage);
            }
            deliverResultToReceiver(receiver, Constants.FAILURE_RESULT, null);
        } else {
            deliverResultToReceiver(receiver, Constants.SUCCESS_RESULT, addresses.get(0));
        }
    }

    private List<Address> getAddresses(Geocoder geocoder, double latitude, double longitude)
            throws IOException, InterruptedException {
        if (android.os.Build.VERSION.SDK_INT < 33) {
            return getLegacyAddresses(geocoder, latitude, longitude);
        }

        AtomicReference<List<Address>> result = new AtomicReference<>();
        CountDownLatch completed = new CountDownLatch(1);
        geocoder.getFromLocation(latitude, longitude, 1, new Geocoder.GeocodeListener() {
            @Override
            public void onGeocode(List<Address> addresses) {
                result.set(addresses);
                completed.countDown();
            }

            @Override
            public void onError(String errorMessage) {
                completed.countDown();
            }
        });
        completed.await(10, TimeUnit.SECONDS);
        return result.get();
    }

    @SuppressWarnings("deprecation")
    private List<Address> getLegacyAddresses(Geocoder geocoder, double latitude, double longitude)
            throws IOException {
        return geocoder.getFromLocation(latitude, longitude, 1);
    }

    private void deliverResultToReceiver(ResultReceiver receiver, int resultCode, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRA_ADDRESS, address);
        receiver.send(resultCode, bundle);
    }
}
