package vedam.subkuch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityReferralBinding;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.ReferralRequest;
import vedam.subkuch.ui.home.HomeActivity;
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
        if (response != null) {
            startHomeScreen();
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private void startHomeScreen() {

        Intent intent = new Intent(ReferralActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
