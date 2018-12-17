package vedam.subkuch.locationProvider;

import android.annotation.SuppressLint;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import vedam.subkuch.helpers.Constants;


/**
 * Created by nansari on 6/22/2016.
 * For project RelayServices
 */
@SuppressLint("ParcelCreator")
public class AddressResultReceiver extends ResultReceiver {

    AddressCallback addressCallback;

    private AddressResultReceiver(Handler handler) {
        super(handler);
    }

    public AddressResultReceiver(Handler handler, AddressCallback addressCallback) {
        this(handler);
        this.addressCallback = addressCallback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        // Display the address string
        // or an error message sent from the intent service.

        Address address = null;
        if (resultCode == Constants.SUCCESS_RESULT) {

            address = resultData.getParcelable(Constants.EXTRA_ADDRESS);

        }
        if (addressCallback != null)
            addressCallback.onAddressChanged(address);
    }

}
