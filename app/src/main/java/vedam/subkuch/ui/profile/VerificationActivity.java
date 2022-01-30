package vedam.subkuch.ui.profile;

import static vedam.subkuch.utils.AppPrefs.PREFS_IF_USER_LOGGED_IN;
import static vedam.subkuch.utils.AppPrefs.PREFS_IS_REFERRAL_DONE;
import static vedam.subkuch.utils.AppPrefs.PREFS_TOKEN;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_ID;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.WebServices;
import vedam.subkuch.network.models.OtpResponse;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.ProfileResponse;
import vedam.subkuch.network.models.VerifyOtpResponse;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;


public class VerificationActivity extends BaseActivity {

    // UI references.
    private EditText etOtp;
    private Profile profile;
    private int noOfAttempts;
    private VerificationIdlingResource idlingResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        setTitle(R.string.verification);

        initUI();
        bindCallbacks();

        sendOtp();
    }

    private void initUI() {

        etOtp = findViewById(R.id.etOtp);

        profile = getIntent().getParcelableExtra(Constants.EXTRA_DATA);
    }

    private void bindCallbacks() {

        findViewById(R.id.btSubmit).setOnClickListener(v -> {
                    if (noOfAttempts <= 5)
                        attemptVerification();
                    else
                        UiUtil.showToast(this, getString(R.string.too_many_otp_attempts));
                }
        );

        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && s.length() == 4)
                    UiUtil.hideKeyBoard(VerificationActivity.this, etOtp);
            }
        });
    }

    private void sendOtp() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        String countryCode = AppPrefs.getInstance(this).getSharedPreferences().getString(Constants.EXTRA_COUNTRY_CODE, "91");
        DataFetcher.sendOtp(this, onOtpSuccessListener, OtpResponse.class, onErrorListener, countryCode, profile.getMobile());
    }

    private void attemptVerification() {
        // Reset errors.
        etOtp.setError(null);

        // Store values at the time of the verification attempt.
        String otp = etOtp.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(otp)) {
            etOtp.setError(getString(R.string.error_field_required));
            focusView = etOtp;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            sendVerifyOtpRequest(otp);

        }
    }

    private void sendVerifyOtpRequest(String otp) {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        String countryCode = AppPrefs.getInstance(this).getSharedPreferences().getString(Constants.EXTRA_COUNTRY_CODE, "91");
        DataFetcher.verifyOtp(this, onVerifyOtpSuccessListener, VerifyOtpResponse.class, onErrorListener, countryCode, profile.getMobile(), otp);
    }

    private void registerUser() {

        setIdleState(false, null);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        final String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        profile.setDeviceId(deviceId);
        DataFetcher.registerUser(this, new Gson().toJson(profile), onRegisterUserSuccessListener, ProfileResponse.class, onErrorListener);
    }

    private void setIdleState(boolean isIdleNow, OtpResponse response) {

        // The IdlingResource is null in production.
        if (idlingResource != null) {
            idlingResource.setIdleState(isIdleNow, response);
        }
    }

    private Response.Listener<OtpResponse> onOtpSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus().equals(Constants.STATUS_SUCCESS)) {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.otp_sent));
        } else {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
        }
        getIdlingResource();
        setIdleState(true, response);
    };

    private Response.Listener<VerifyOtpResponse> onVerifyOtpSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && Constants.TRUE.equals(response.getIsVerified())) {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.otp_verified));
            registerUser();
        } else {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
        }
    };

    private Response.Listener<ProfileResponse> onRegisterUserSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)
                && response.getReturnData() != null) {
            handleResponse(response.getReturnData().get(0));
        } else
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
    };

    private void handleResponse(Profile receivedProfile) {

        SharedPreferences.Editor editor = AppPrefs.getInstance(VerificationActivity.this).getSharedPreferences().edit();
        editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
        editor.putString(PREFS_USER_ID, receivedProfile.getProfileId());
        String bearer = "Bearer " + receivedProfile.getAuthToken();
        editor.putString(PREFS_TOKEN, bearer);
        editor.putString(PREFS_USER_NAME, AppUtil.getFullName(receivedProfile.getFirstName(), receivedProfile.getLastName()));
        WebServices.getInstance().setBearer(bearer);
        boolean isReferralDone = receivedProfile.getIsReferralDone();
        editor.putString(PREFS_IS_REFERRAL_DONE, String.valueOf(isReferralDone));
        editor.apply();
        Intent intent;
        /*if (!isReferralDone)
            intent = new Intent(VerificationActivity.this, ReferralActivity.class);
        else*/
            intent = new Intent(VerificationActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        UiUtil.showToast(VerificationActivity.this, getString(R.string.user_registered_successfully));
    }

//    private Response.ErrorListener onErrorListener = error -> {
//
//        LogUtils.LOGD("ERROR", error.getMessage());
//        UiUtil.cancelProgressDialog();
//        UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
//
//    };

    /**
     * Only called from test, creates and returns a new {@link VerificationIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public VerificationIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new VerificationIdlingResource();
        }
        return idlingResource;
    }
}
