package vedam.subkuch.ui.wallet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigDecimal;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityTransferBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.Response;
import vedam.subkuch.network.models.TransferRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class TransferActivity extends BaseActivity {

    public static final String EXTRA_AVAILABLE_BALANCE = "available_balance";

    private ActivityTransferBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer);
        setTitle(R.string.transfer_funds);
        setToolbarBackButton();
        String availableBalance = getIntent().getStringExtra(EXTRA_AVAILABLE_BALANCE);
        if (TextUtils.isEmpty(availableBalance)) {
            availableBalance = "0";
        }
        binding.tvAvailableBalance.setText(availableBalance);
        binding.btSubmit.setOnClickListener(view -> submit());
        View.OnFocusChangeListener inputFocusListener = (view, hasFocus) -> {
            if (hasFocus) {
                keepSubmitVisible();
            }
        };
        binding.etCoins.setOnFocusChangeListener(inputFocusListener);
        binding.etReceiverPhoneNumber.setOnFocusChangeListener(inputFocusListener);
    }

    private void submit() {
        if (!validateFields()) {
            return;
        }

        hideKeyboard();
        showTransferConfirmation();
    }

    private void showTransferConfirmation() {
        String receiverPhoneNumber = textOf(binding.etReceiverPhoneNumber);
        BigDecimal coins = new BigDecimal(textOf(binding.etCoins));
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.review_transfer)
                .setMessage(getString(
                        R.string.transfer_confirmation_message,
                        receiverPhoneNumber,
                        coins.stripTrailingZeros().toPlainString()
                ))
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(
                        R.string.confirm_transfer,
                        (dialog, which) -> performTransfer(receiverPhoneNumber, coins)
                )
                .show();
    }

    private void hideKeyboard() {
        View focusedView = getCurrentFocus();
        if (focusedView == null) {
            return;
        }
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
        focusedView.clearFocus();
    }

    private void keepSubmitVisible() {
        binding.transferScroll.postDelayed(() -> {
            Rect submitBounds = new Rect();
            binding.btSubmit.getDrawingRect(submitBounds);
            binding.transferScroll.offsetDescendantRectToMyCoords(
                    binding.btSubmit,
                    submitBounds
            );
            int bottomPadding = Math.round(
                    16 * getResources().getDisplayMetrics().density
            );
            int scrollY = Math.max(
                    0,
                    submitBounds.bottom - binding.transferScroll.getHeight() + bottomPadding
            );
            binding.transferScroll.smoothScrollTo(0, scrollY);
        }, 300);
    }

    private void performTransfer(String receiverPhoneNumber, BigDecimal coins) {
        TransferRequest request = new TransferRequest();
        request.setReceiverPhoneNumber(receiverPhoneNumber);
        request.setCoins(coins);

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.transferFunds(
                this,
                new Gson().toJson(request),
                onTransferSuccessListener,
                AddResponse.class,
                onErrorListener
        );
    }

    private final Response.Listener<AddResponse> onTransferSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (isSuccessful(response)) {
            UiUtil.showToast(this, getString(R.string.transfer_done_successfully));
            setResult(Activity.RESULT_OK);
            finish();
            return;
        }

        String message = getString(R.string.err_occurred);
        if (response != null && !TextUtils.isEmpty(response.getReturnMessage())) {
            message = response.getReturnMessage();
        } else if (response != null && !TextUtils.isEmpty(response.getMessage())) {
            message = response.getMessage();
        }
        UiUtil.showToast(this, message);
    };

    private boolean isSuccessful(AddResponse response) {
        return response != null && (
                response.isSuccess()
                        || response.isStatus()
                        || response.getReturnCode() == 1
                        || Constants.SUCCESS.equalsIgnoreCase(response.getReturnMessage())
        );
    }

    private boolean validateFields() {
        String receiverPhoneNumber = textOf(binding.etReceiverPhoneNumber);
        String coins = textOf(binding.etCoins);
        binding.tilReceiverPhoneNumber.setError(null);
        binding.tilCoins.setError(null);
        if (TextUtils.isEmpty(coins)) {
            binding.tilCoins.setError(getString(R.string.enter_coins_to_transfer));
            binding.etCoins.requestFocus();
            return false;
        }
        try {
            if (new BigDecimal(coins).compareTo(BigDecimal.ZERO) <= 0) {
                binding.tilCoins.setError(getString(R.string.enter_valid_transfer_amount));
                binding.etCoins.requestFocus();
                return false;
            }
        } catch (NumberFormatException ignored) {
            binding.tilCoins.setError(getString(R.string.enter_valid_transfer_amount));
            binding.etCoins.requestFocus();
            return false;
        }
        if (receiverPhoneNumber.length() < 6 || receiverPhoneNumber.length() > 15
                || !AppUtil.isNumeric(receiverPhoneNumber)) {
            binding.tilReceiverPhoneNumber.setError(getString(R.string.enter_receiver_phone_number));
            binding.etReceiverPhoneNumber.requestFocus();
            return false;
        }
        return true;
    }

    private String textOf(android.widget.EditText editText) {
        return AppUtil.deNull(editText.getText()).trim();
    }
}
