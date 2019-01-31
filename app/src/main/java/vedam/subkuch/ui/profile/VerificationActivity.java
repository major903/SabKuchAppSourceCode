package vedam.subkuch.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.OtpResponse;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.ProfileResponse;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.AppPrefs.PREFS_IF_USER_LOGGED_IN;
import static vedam.subkuch.utils.AppPrefs.PREFS_TOKEN;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_ID;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;


public class VerificationActivity extends BaseActivity {

    // UI references.
    private EditText etOtp;
    private String sentOtp;
    private Profile profile;
    private int noOfAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        setTitle(R.string.verification);

        Button btSubmit = findViewById(R.id.btSubmit);
        etOtp = findViewById(R.id.etOtp);

        profile = getIntent().getParcelableExtra(Constants.EXTRA_DATA);

        btSubmit.setOnClickListener(v -> {
                    if (noOfAttempts <= 5)
                        attemptVerification();
                    else
                        UiUtil.showToast(this, getString(R.string.too_many_otp_attempts));
                }
        );

        sendOtp();
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

        if (sentOtp.equalsIgnoreCase(otp)) {
            registerUser();
        } else {
            noOfAttempts++;
            Toast.makeText(this, getString(R.string.incorrect_otp), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.registerUser(this, new Gson().toJson(profile), onRegisterUserSuccessListener, ProfileResponse.class, onErrorListener);
    }

    private Response.Listener<OtpResponse> onOtpSuccessListener = new Response.Listener<OtpResponse>() {
        @Override
        public void onResponse(OtpResponse response) {

            UiUtil.cancelProgressDialog();
            if (response != null && response.getStatus().equals(Constants.STATUS_SUCCESS)) {
                sentOtp = response.getOTP();
                UiUtil.showToast(VerificationActivity.this, getString(R.string.otp_sent));
            } else {
                UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
            }


        }
    };

    private Response.Listener<ProfileResponse> onRegisterUserSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)
                && response.getReturnData() != null) {
            SharedPreferences.Editor editor = AppPrefs.getInstance(VerificationActivity.this).getSharedPreferences().edit();
            editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
            editor.putString(PREFS_USER_ID, response.getReturnData().get(0).getProfileId());
            editor.putString(PREFS_TOKEN, "Bearer " + response.getReturnData().get(0).getAuthToken());
            editor.putString(PREFS_USER_NAME, getFullName());
            editor.apply();
            Intent intent = new Intent(VerificationActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            UiUtil.showToast(VerificationActivity.this, getString(R.string.user_registered_successfully));
        } else
            UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
    };

    private String getFullName() {
        String fullName = profile.getFirstName() + " " + profile.getLastName();
        return fullName.trim();
    }

//    private Response.ErrorListener onErrorListener = error -> {
//
//        LogUtils.LOGD("ERROR", error.getMessage());
//        UiUtil.cancelProgressDialog();
//        UiUtil.showToast(VerificationActivity.this, getString(R.string.err_occurred));
//
//    };
}
