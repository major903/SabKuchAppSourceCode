package vedam.subkuch.ui.pin;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentPinBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.ui.matrimonial.ShowProfilesActivity;
import vedam.subkuch.ui.matrimonial.models.PinRequest;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinFragment extends BaseFragment {

    private FragmentPinBinding fragmentPinBinding;
    private int code;
    private String previousPin;
    private boolean isDating;
    public PinFragment() {
        // Required empty public constructor
    }

    public static PinFragment newInstance(Bundle extras) {

        PinFragment fragment = new PinFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getInt(Constants.EXTRA_CODE);
            previousPin = getArguments().getString(Constants.EXTRA_PIN);
            isDating = getArguments().getBoolean(Constants.EXTRA_IS_DATING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPinBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pin, container, false);
        return fragmentPinBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();
        bindCallbacks();
    }

    private void initUI() {

        switch (code) {
            case Constants.SET_PIN_CODE:
                fragmentPinBinding.tvTitle.setText(getString(R.string.set_pin));
                break;
            case Constants.RE_ENTER_PIN_CODE:
                fragmentPinBinding.tvTitle.setText(getString(R.string.re_enter_pin));
                break;
            case Constants.ENTER_PIN_CODE:
                fragmentPinBinding.tvTitle.setText(getString(R.string.enter_pin));
                fragmentPinBinding.tvWarning.setVisibility(View.GONE);
                fragmentPinBinding.tvWarning2.setVisibility(View.GONE);
                break;

        }
    }

    private void bindCallbacks() {

        fragmentPinBinding.etPin1.addTextChangedListener(generalTextWatcher);
        fragmentPinBinding.etPin2.addTextChangedListener(generalTextWatcher);
        fragmentPinBinding.etPin3.addTextChangedListener(generalTextWatcher);
        fragmentPinBinding.etPin4.addTextChangedListener(generalTextWatcher);

        fragmentPinBinding.btSubmit.setOnClickListener(v -> submit());
    }

    private void submit() {

        switch (code) {
            case Constants.SET_PIN_CODE:
                showReEnterScreen();
                break;
            case Constants.RE_ENTER_PIN_CODE:
                setPin();
                break;
            case Constants.ENTER_PIN_CODE:
                verifyPin();
                break;

        }
    }

    private void showReEnterScreen() {

        if (validateError()) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.EXTRA_CODE, Constants.RE_ENTER_PIN_CODE);
            bundle.putString(Constants.EXTRA_PIN, getPin());
            bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
            addFragmentWithAnimation(R.id.content_frame, PinFragment.newInstance(bundle), null, true);
        } else
            UiUtil.showToast(context, getString(R.string.please_enter_pin));
    }

    private void setPin() {
        if (checkPin()) {
            UiUtil.showProgressDialog(context, getString(R.string.please_wait));
            DataFetcher.setAccessPin(context, new Gson().toJson(getPinRequest()), onPinSuccessListener, GeneralResponse.class, onErrorListener);
        }
    }

    private void verifyPin() {
        if (validateError()) {
            UiUtil.showProgressDialog(context, getString(R.string.please_wait));
            DataFetcher.verifyAccessPin(context, new Gson().toJson(getPinRequest()), onPinSuccessListener, GeneralResponse.class, onErrorListener);
        } else
            UiUtil.showToast(context, getString(R.string.please_enter_pin));
    }

    private PinRequest getPinRequest() {
        PinRequest pinRequest = new PinRequest();
        String pin = getPin();
        String userId = AppPrefs.getPrefsUserId(context);
        pinRequest.setProfileId(userId);
        pinRequest.setAccessPin(pin);
        return pinRequest;
    }

    private String getPin() {

        return fragmentPinBinding.etPin1.getText().toString() + fragmentPinBinding.etPin2.getText().toString() +
                fragmentPinBinding.etPin3.getText().toString() + fragmentPinBinding.etPin4.getText().toString();
    }

    private boolean checkPin() {
        if (previousPin.equals(getPin()))
            return true;
        else {
            UiUtil.showToast(context, getString(R.string.pin_mismatch));
            return false;
        }
    }

    private Response.Listener<GeneralResponse> onPinSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                startActivity(new Intent(context, ShowProfilesActivity.class).putExtra(Constants.EXTRA_IS_DATING, isDating));
                getActivity().finish();
            } else
                UiUtil.showToast(context, getString(R.string.incorrect_pin));
    };


    @Override
    protected void parseAndShowError(VolleyError error) {
        String errorMessage = error.getMessage();
        if (!TextUtils.isEmpty(errorMessage))
            UiUtil.showToast(context, errorMessage);
        else
            super.parseAndShowError(error);
    }

    private boolean validateError() {

        return !TextUtils.isEmpty(fragmentPinBinding.etPin1.getText()) && !TextUtils.isEmpty(fragmentPinBinding.etPin2.getText())
                && !TextUtils.isEmpty(fragmentPinBinding.etPin3.getText()) && !TextUtils.isEmpty(fragmentPinBinding.etPin4.getText());
    }

    private TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (fragmentPinBinding.etPin1.getText().hashCode() == s.hashCode()) {
                setEditText(s, null, fragmentPinBinding.etPin2);
            } else if (fragmentPinBinding.etPin2.getText().hashCode() == s.hashCode()) {
                setEditText(s, fragmentPinBinding.etPin1, fragmentPinBinding.etPin3);
            } else if (fragmentPinBinding.etPin3.getText().hashCode() == s.hashCode()) {
                setEditText(s, fragmentPinBinding.etPin2, fragmentPinBinding.etPin4);
            } else if (fragmentPinBinding.etPin4.getText().hashCode() == s.hashCode()) {
                setEditText(s, fragmentPinBinding.etPin3, null);
                if (!TextUtils.isEmpty(s)) {
                    fragmentPinBinding.etPin4.clearFocus();
                    UiUtil.hideKeyBoard(context, fragmentPinBinding.etPin4);
                }
            }
        }

    };

    private void setEditText(Editable s, EditText et1, EditText et2) {

        if (TextUtils.isEmpty(s) && et1 != null)
            et1.requestFocus();
        else if (!TextUtils.isEmpty(s) && et2 != null)
            et2.requestFocus();
    }
}
