package vedam.subkuch.ui.profile;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import vedam.subkuch.MainActivity;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityReferralBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.ReferralRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class ReferralActivity extends BaseActivity {

    private ActivityReferralBinding activityReferralBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReferralBinding = DataBindingUtil.setContentView(this, R.layout.activity_referral);
        setTitle(R.string.refer_earn);
        bindCallbacks();
    }

    private void bindCallbacks() {

        activityReferralBinding.btSubmit.setOnClickListener(v -> submit());
    }

    private void submit() {

//        if (!TextUtils.isEmpty(activityReferralBinding.etReferralCode.getText())) {
        addReferralCode();
//        } else
//            startHomeScreen();
    }

    private void addReferralCode() {

        UiUtil.showProgressDialog(this, R.string.please_wait);
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setProfileId(AppPrefs.getPrefsUserId(this));
        referralRequest.setReferredBy(AppUtil.deNull(activityReferralBinding.etReferralCode.getText()).trim());
        DataFetcher.addReferral(this, new Gson().toJson(referralRequest), onAddReferralSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddReferralSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            startHomeScreen();
        } else if (response != null && !TextUtils.isEmpty(response.getReturnMessage())) {
            UiUtil.showToast(this, response.getReturnMessage());
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private void startHomeScreen() {

        Intent intent = new Intent(ReferralActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
