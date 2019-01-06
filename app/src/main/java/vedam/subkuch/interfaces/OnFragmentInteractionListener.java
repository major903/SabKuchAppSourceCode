package vedam.subkuch.interfaces;

import android.content.Intent;
import android.location.Address;
import android.location.Location;

import vedam.subkuch.locationProvider.AddressCallback;
import vedam.subkuch.locationProvider.LocationCallbacks;


/**
 * Created by nansari on 6/20/2016.
 * For project RelayServices
 */
public interface OnFragmentInteractionListener {

    /**
     * Finishes parent activity
     */
    void finishActivity();

    /**
     * You can request for location from any Activity or fragment using this method and get the location in
     * {@link LocationCallbacks#onLocationChanged(Location)}
     */
    void requestLocation(boolean shouldLocation);

    /**
     * You can request for address from any Activity or fragment using this method and get the address in
     * {@link AddressCallback#onAddressChanged(Address)}
     */
    void requestAddress(boolean shouldLocation);

    /**
     * You can set the result for an Activity in the fragment using this method. It automatically finishes the parent activity,
     *
     * @param result result code
     * @param data   Extra data in Intent
     */
    void setFragmentResult(int result, Intent data);
}
