package vedam.subkuch.ui.profile;

import static vedam.subkuch.utils.AppPrefs.PREFS_IF_USER_LOGGED_IN;
import static vedam.subkuch.utils.AppPrefs.PREFS_IS_REFERRAL_DONE;
import static vedam.subkuch.utils.AppPrefs.PREFS_TOKEN;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_ID;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.IntentCompat;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import vedam.subkuch.MainActivity;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.WebServices;
import vedam.subkuch.network.models.OtpResponse;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.RegistrationResponse;
import vedam.subkuch.network.models.RegistrationRequest;
import vedam.subkuch.network.models.VerifyOtpResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.DeviceIdProvider;
import vedam.subkuch.utils.UiUtil;


public class VerificationActivity extends BaseActivity {

    private static final int MAX_OTP_ATTEMPTS = 5;

    // UI references.
    private EditText etOtp;
    private View submitButton;
    private Profile profile;
    private int noOfAttempts;
    private VerificationIdlingResource idlingResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        setTitle(R.string.verification);
        setToolbarBackButton();
        findViewById(R.id.tv_toolbar_title).setVisibility(View.VISIBLE);
        ((android.widget.TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.verification);

        initUI();
        bindCallbacks();

        sendOtp();
    }

    private void initUI() {

        etOtp = findViewById(R.id.etOtp);
        submitButton = findViewById(R.id.btSubmit);

        profile = IntentCompat.getParcelableExtra(getIntent(), Constants.EXTRA_DATA, Profile.class);
    }

    private void bindCallbacks() {

        submitButton.setOnClickListener(v -> {
                    if (noOfAttempts < MAX_OTP_ATTEMPTS)
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

        if (!DataFetcher.isRegistrationApiConfigured()) {
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        setIdleState(false, null);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        final String deviceId = DeviceIdProvider.getDeviceId(this);
        RegistrationRequest request = new RegistrationRequest(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getGender(),
                profile.getDOB(),
                profile.getMobile(),
                profile.getEMail(),
                parseRequiredId(profile.getOccupationid()),
                profile.getOccupationOther(),
                deviceId,
                profile.getLatitude(),
                profile.getLongitude(),
                parseRequiredId(profile.getCityId()),
                parseRequiredId(getIntent().getStringExtra(Constants.EXTRA_STATE_ID)),
                parseRequiredId(getIntent().getStringExtra(Constants.EXTRA_LANGUAGE_ID)),
                parseRequiredId(profile.getCountryid()));
        DataFetcher.registerUser(this, new Gson().toJson(request), onRegisterUserSuccessListener,
                RegistrationResponse.class, onErrorListener);
    }

    private int parseRequiredId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
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
        if (response != null && response.isVerified()) {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.otp_verified));
            if (response.isExistingUser()) {
                loginExistingUser(response);
            } else {
                registerUser();
            }
        } else {
            noOfAttempts++;
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
        }
    };

    private void loginExistingUser(VerifyOtpResponse response) {
        String bearer = normalizeBearer(response.getAuthenticationResult());
        if (TextUtils.isEmpty(bearer) || TextUtils.isEmpty(response.getUserId())) {
            UiUtil.showToast(this, getString(R.string.err_occurred));
            return;
        }

        SharedPreferences.Editor editor = AppPrefs.getInstance(this).getSharedPreferences().edit();
        editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
        editor.putString(PREFS_USER_ID, response.getUserId());
        editor.putString(PREFS_TOKEN, bearer);
        editor.putString(PREFS_USER_NAME,
                AppUtil.getFullName(response.getFirstName(), response.getLastName()));
        editor.putString(PREFS_IS_REFERRAL_DONE, String.valueOf(true));
        editor.apply();

        WebServices.getInstance().setBearer(bearer);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        UiUtil.showToast(this, getString(R.string.user_logged_in_successfully));
    }

    private String normalizeBearer(String authenticationResult) {
        if (TextUtils.isEmpty(authenticationResult)) return null;
        String token = authenticationResult.trim();
        return token.regionMatches(true, 0, "Bearer ", 0, 7) ? token : "Bearer " + token;
    }

    private Response.Listener<RegistrationResponse> onRegisterUserSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && !TextUtils.isEmpty(response.getUserId())
                && !TextUtils.isEmpty(response.getAuthenticationResult())) {
            handleRegistrationResponse(response);
        } else {
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
        }
    };

    private void handleRegistrationResponse(RegistrationResponse response) {
        String bearer = normalizeBearer(response.getAuthenticationResult());
        if (TextUtils.isEmpty(bearer)) {
            UiUtil.showToast(this, getString(R.string.err_occurred));
            return;
        }

        SharedPreferences.Editor editor = AppPrefs.getInstance(this).getSharedPreferences().edit();
        editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
        editor.putString(PREFS_USER_ID, response.getUserId());
        editor.putString(PREFS_TOKEN, bearer);
        editor.putString(PREFS_USER_NAME,
                AppUtil.getFullName(profile.getFirstName(), profile.getLastName()));
        editor.putInt(AppPrefs.PREFS_USER_GENDER, getGenderCode(profile.getGender()));
        // A newly registered user still needs the Play install referrer linked.
        // If the server reports an existing account, do not allow it to be re-linked.
        editor.putString(PREFS_IS_REFERRAL_DONE,
                String.valueOf(response.isAlreadyRegistered()));
        editor.apply();

        WebServices.getInstance().setBearer(bearer);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        UiUtil.showToast(this, getString(R.string.user_registered_successfully));
    }

    private void handleResponse(Profile receivedProfile) {

        SharedPreferences.Editor editor = AppPrefs.getInstance(VerificationActivity.this).getSharedPreferences().edit();
        editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
        editor.putString(PREFS_USER_ID, receivedProfile.getProfileId());
        String bearer = "Bearer " + receivedProfile.getAuthToken();
        editor.putString(PREFS_TOKEN, bearer);
        editor.putString(PREFS_USER_NAME, AppUtil.getFullName(receivedProfile.getFirstName(), receivedProfile.getLastName()));
        editor.putInt(AppPrefs.PREFS_USER_GENDER, getGenderCode(receivedProfile.getGender()));
        WebServices.getInstance().setBearer(bearer);
        boolean isReferralDone = receivedProfile.getIsReferralDone();
        editor.putString(PREFS_IS_REFERRAL_DONE, String.valueOf(isReferralDone));
        editor.apply();
        Intent intent;
        /*if (!isReferralDone)
            intent = new Intent(VerificationActivity.this, ReferralActivity.class);
        else*/
            intent = new Intent(VerificationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        UiUtil.showToast(VerificationActivity.this, getString(R.string.user_registered_successfully));
    }

    private int getGenderCode(String gender) {
        if (gender == null) return 0;
        if ("1".equals(gender) || "male".equalsIgnoreCase(gender)) return 1;
        if ("2".equals(gender) || "female".equalsIgnoreCase(gender)) return 2;
        return 0;
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
