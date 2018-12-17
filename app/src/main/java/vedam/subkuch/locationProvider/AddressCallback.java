package vedam.subkuch.locationProvider;

import android.location.Address;

/**
 * Created by nansari on 7/29/2016.
 * For project RelayServices
 */
public interface AddressCallback {
    /**
     * Called when user requests for an Address and address is found using {@link android.location.Geocoder}
     *
     * @param googleAddress provides {@link Address}
     */
    void onAddressChanged(Address googleAddress);
}
