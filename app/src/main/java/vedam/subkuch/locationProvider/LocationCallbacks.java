package vedam.subkuch.locationProvider;

import android.location.Location;

import com.google.android.gms.common.api.Status;

/**
 * Created by nansari on 6/22/2016.
 * For project RelayServices
 */
public interface LocationCallbacks extends AddressCallback {

    /**
     * Called when user has not provided permission to access location
     */
    void onNoLocationPermission();

    /**
     * Called when location is fetched from fused location provider
     *
     * @param location current location
     */
    void onLocationChanged(Location location);

    /**
     * Called when user's GPS is off.
     *
     * @param status gives detail about gps status
     */
    void onGpsOff(Status status);

}
