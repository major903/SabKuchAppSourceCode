package vedam.subkuch.ui.wallet;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityWithdrawalBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddWithdrawalRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class WithdrawalActivity extends BaseActivity {

    public static final String EXTRA_AVAILABLE_BALANCE = "available_balance";

    private ActivityWithdrawalBinding activityWithdrawalBinding;
    private int initialScrollBottomPadding;
    private boolean keyboardScrollActive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWithdrawalBinding = DataBindingUtil.setContentView(this, R.layout.activity_withdrawal);
        setTitle(R.string.withdrawal);
        setToolbarBackButton();
        String availableBalance = getIntent().getStringExtra(EXTRA_AVAILABLE_BALANCE);
        activityWithdrawalBinding.tvAvailableBalance.setText(
                TextUtils.isEmpty(availableBalance) ? "0" : availableBalance
        );
        initialScrollBottomPadding = activityWithdrawalBinding.withdrawalScroll.getPaddingBottom();
        configureKeyboardScrolling();
        bindCallbacks();
    }

    private void bindCallbacks() {

        activityWithdrawalBinding.btSubmit.setOnClickListener(v -> submit());
    }

    private void configureKeyboardScrolling() {
        View.OnFocusChangeListener submitFocusListener = (view, hasFocus) -> {
            if (!hasFocus) return;
            activityWithdrawalBinding.withdrawalScroll.postDelayed(
                    this::scrollSubmitIntoView,
                    300
            );
        };
        activityWithdrawalBinding.etWithdrawalAmount.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) resetScrollToTop();
        });
        activityWithdrawalBinding.etAccountName.setOnFocusChangeListener(submitFocusListener);
        activityWithdrawalBinding.etAccountNumber.setOnFocusChangeListener(submitFocusListener);
        activityWithdrawalBinding.etBankName.setOnFocusChangeListener(submitFocusListener);
        activityWithdrawalBinding.etIfscCode.setOnFocusChangeListener(submitFocusListener);
        activityWithdrawalBinding.withdrawalScroll.getViewTreeObserver().addOnGlobalLayoutListener(
                this::resetScrollWhenKeyboardCloses
        );
    }

    private void scrollSubmitIntoView() {
        activityWithdrawalBinding.withdrawalScroll.post(() -> {
            int keyboardSpace = Math.round(400 * getResources().getDisplayMetrics().density);
            activityWithdrawalBinding.withdrawalScroll.setPadding(
                    activityWithdrawalBinding.withdrawalScroll.getPaddingLeft(),
                    activityWithdrawalBinding.withdrawalScroll.getPaddingTop(),
                    activityWithdrawalBinding.withdrawalScroll.getPaddingRight(),
                    initialScrollBottomPadding + keyboardSpace
            );
            keyboardScrollActive = true;
            int targetScrollY = activityWithdrawalBinding.btSubmit.getTop()
                    - activityWithdrawalBinding.withdrawalScroll.getHeight() / 2;
            activityWithdrawalBinding.withdrawalScroll.smoothScrollTo(0, Math.max(0, targetScrollY));
        });
    }

    private void resetScrollWhenKeyboardCloses() {
        if (!keyboardScrollActive) return;
        Rect visibleFrame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);
        int coveredHeight = getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
        int keyboardThreshold = Math.round(200 * getResources().getDisplayMetrics().density);
        if (coveredHeight > keyboardThreshold) return;

        resetScrollToTop();
    }

    private void resetScrollToTop() {
        keyboardScrollActive = false;
        activityWithdrawalBinding.withdrawalScroll.setPadding(
                activityWithdrawalBinding.withdrawalScroll.getPaddingLeft(),
                activityWithdrawalBinding.withdrawalScroll.getPaddingTop(),
                activityWithdrawalBinding.withdrawalScroll.getPaddingRight(),
                initialScrollBottomPadding
        );
        activityWithdrawalBinding.withdrawalScroll.smoothScrollTo(0, 0);
    }

    private void submit() {
        if (!validateFields()) {
            return;
        }
        mWithdraw();
    }

    private void mWithdraw() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        int points = Integer.parseInt(activityWithdrawalBinding.etWithdrawalAmount.getText().toString().trim());
        AddWithdrawalRequest withdrawalRequest = new AddWithdrawalRequest(
                points,
                textOf(activityWithdrawalBinding.etAccountName.getText()),
                textOf(activityWithdrawalBinding.etAccountNumber.getText()),
                textOf(activityWithdrawalBinding.etBankName.getText()),
                textOf(activityWithdrawalBinding.etIfscCode.getText())
        );

        DataFetcher.addWithdrawal(this, new Gson().toJson(withdrawalRequest), onWithdrawalSuccessListener, AddResponse.class, onErrorListener);
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

    private boolean validateFields() {
        activityWithdrawalBinding.tilPoints.setError(null);
        activityWithdrawalBinding.tilAccountName.setError(null);
        activityWithdrawalBinding.tilAccountNumber.setError(null);
        activityWithdrawalBinding.tilBankName.setError(null);
        activityWithdrawalBinding.tilIfscCode.setError(null);

        String points = AppUtil.deNull(activityWithdrawalBinding.etWithdrawalAmount.getText()).trim();
        if (TextUtils.isEmpty(points)) {
            activityWithdrawalBinding.tilPoints.setError(getString(R.string.enter_withdrawal_amount));
            activityWithdrawalBinding.etWithdrawalAmount.requestFocus();
            return false;
        }
        try {
            int value = Integer.parseInt(points);
            if (value < 1 || value > 10) {
                activityWithdrawalBinding.tilPoints.setError(getString(R.string.enter_withdrawal_points));
                activityWithdrawalBinding.etWithdrawalAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException ignored) {
            activityWithdrawalBinding.tilPoints.setError(getString(R.string.enter_withdrawal_points));
            activityWithdrawalBinding.etWithdrawalAmount.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(textOf(activityWithdrawalBinding.etAccountName.getText()))) {
            activityWithdrawalBinding.tilAccountName.setError(getString(R.string.enter_account_name));
            activityWithdrawalBinding.etAccountName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(textOf(activityWithdrawalBinding.etAccountNumber.getText()))) {
            activityWithdrawalBinding.tilAccountNumber.setError(getString(R.string.enter_account_number));
            activityWithdrawalBinding.etAccountNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(textOf(activityWithdrawalBinding.etBankName.getText()))) {
            activityWithdrawalBinding.tilBankName.setError(getString(R.string.enter_bank_name));
            activityWithdrawalBinding.etBankName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(textOf(activityWithdrawalBinding.etIfscCode.getText()))) {
            activityWithdrawalBinding.tilIfscCode.setError(getString(R.string.enter_ifsc_code));
            activityWithdrawalBinding.etIfscCode.requestFocus();
            return false;
        }
        return true;
    }

    private String textOf(CharSequence value) {
        return AppUtil.deNull(value).trim();
    }
}
