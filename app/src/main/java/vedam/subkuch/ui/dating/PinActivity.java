package vedam.subkuch.ui.dating;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.ui.dating.models.PinRequest;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class PinActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle(R.string.app_name);
        setToolbarBackButton();

        isPinSet();
    }

    private void isPinSet() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        PinRequest pinRequest = new PinRequest();
        String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        pinRequest.setProfileId(userId);
        pinRequest.setAccessPin("1");
        DataFetcher.verifyAccessPin(this, new Gson().toJson(pinRequest), onPinSuccessListener, GeneralResponse.class, onErrorListener);
    }

    @Override
    protected void parseAndShowError(VolleyError error) {
        if (error.networkResponse.statusCode == 400) {
            String errorMessage = new String(error.networkResponse.data);
            if (getString(R.string.pin_not_set).equalsIgnoreCase(errorMessage))
                showSetScreen();
            else
                showEnterScreen();
        }
    }

    private void showSetScreen() {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_CODE, Constants.SET_PIN_CODE);
        addFragment(R.id.content_frame, PinFragment.newInstance(bundle));
    }

    private void showEnterScreen() {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_CODE, Constants.ENTER_PIN_CODE);
        addFragment(R.id.content_frame, PinFragment.newInstance(bundle));
    }

    private Response.Listener<GeneralResponse> onPinSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        showEnterScreen();
    };

}
