package vedam.subkuch.ui.pin;

import android.os.Bundle;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.ui.matrimonial.models.PinRequest;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class PinActivity extends BaseActivity {

    private boolean isDating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle(R.string.app_name);
        setToolbarBackButton();
        isDating = getIntent().getBooleanExtra(Constants.EXTRA_IS_DATING, false);
        isPinSet();
    }

    private void isPinSet() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        PinRequest pinRequest = new PinRequest();
        String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        pinRequest.setProfileId(userId);
        pinRequest.setAccessPin("1234");
        DataFetcher.verifyAccessPin(this, new Gson().toJson(pinRequest), onPinSuccessListener, GeneralResponse.class, onErrorListener);
    }

    private void showSetScreen() {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_CODE, Constants.SET_PIN_CODE);
        bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        addFragmentWithStateLoss(R.id.content_frame, PinFragment.newInstance(bundle));
    }

    private void showEnterScreen() {

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_CODE, Constants.ENTER_PIN_CODE);
        bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        addFragmentWithStateLoss(R.id.content_frame, PinFragment.newInstance(bundle));
    }

    private Response.Listener<GeneralResponse> onPinSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && Constants.CODE_PIN_NOT_SET == response.getReturnCode())
            showSetScreen();
        else
            showEnterScreen();
    };

}
