package vedam.subkuch.ui.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityWithdrawalBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.WithdrawalRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class WithdrawalActivity extends BaseActivity {

    private ActivityWithdrawalBinding activityWithdrawalBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWithdrawalBinding = DataBindingUtil.setContentView(this, R.layout.activity_withdrawal);
        setTitle(R.string.withdrawal);
        setToolbarBackButton();
        bindCallbacks();
    }

    private void bindCallbacks() {

        activityWithdrawalBinding.btSubmit.setOnClickListener(v -> submit());
    }

    private void submit() {

        int errorMessage = validateErrorMessage();
        if (errorMessage == 0)
            withdraw();
        else
            UiUtil.showDialog(this, getString(errorMessage), true);
    }

    private void withdraw() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setAmount(activityWithdrawalBinding.etWithdrawalAmount.getText().toString());
        withdrawalRequest.setVendorCode(activityWithdrawalBinding.etVendorCode.getText().toString());

        DataFetcher.withdraw(this, new Gson().toJson(withdrawalRequest), onWithdrawalSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onWithdrawalSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        String errorMessage = getString(R.string.err_occurred);
        if (response != null && !TextUtils.isEmpty(response.getReturnMessage()))
            errorMessage = response.getReturnMessage();

        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            UiUtil.showToast(this, getString(R.string.withdrawal_done_successfully));
            setResult(Activity.RESULT_OK);
            finish();
        } else
            UiUtil.showToast(this, errorMessage);
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(AppUtil.deNull(activityWithdrawalBinding.etWithdrawalAmount.getText()).trim()))
            errorMessage = R.string.enter_withdrawal_amount;
        else if (TextUtils.isEmpty(AppUtil.deNull(activityWithdrawalBinding.etVendorCode.getText()).trim()))
            errorMessage = R.string.enter_vendor_code;
        return errorMessage;
    }
}
