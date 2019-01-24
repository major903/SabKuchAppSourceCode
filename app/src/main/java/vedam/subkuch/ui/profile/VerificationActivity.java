package vedam.subkuch.ui.profile;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.OtpResponse;
import vedam.subkuch.network.models.RegisterUserResponse;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.helpers.Constants.PERMISSIONS_REQUEST_SMS;
import static vedam.subkuch.utils.AppPrefs.PREFS_IF_USER_LOGGED_IN;
import static vedam.subkuch.utils.AppPrefs.PREFS_TOKEN;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_ID;
import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;


public class VerificationActivity extends BaseActivity {

    // UI references.
    private static EditText etOtp;
    private static String sentOtp;
    private static Context context;
    private static String latitude;
    private static String longitude;
    private static String firstName;
    private static String lastName;
    private static String mobileNumber;
    private static String emailId;
    private static String dob;
    private static String gender;
    private static String cityId;
    private static String countryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        context = VerificationActivity.this;

        setTitle(R.string.verification);

        Button btSubmit = findViewById(R.id.btSubmit);
        etOtp = findViewById(R.id.etOtp);

        latitude = getIntent().getStringExtra(Constants.EXTRA_LOCATION_LATITUDE);
        longitude = getIntent().getStringExtra(Constants.EXTRA_LOCATION_LONGITUDE);
        firstName = getIntent().getStringExtra(Constants.EXTRA_FIRST_NAME);
        lastName = getIntent().getStringExtra(Constants.EXTRA_LAST_NAME);
        mobileNumber = getIntent().getStringExtra(Constants.EXTRA_MOBILE_NUMBER);
        emailId = getIntent().getStringExtra(Constants.EXTRA_EMAIL_ID);
        dob = getIntent().getStringExtra(Constants.EXTRA_DOB);
        gender = getIntent().getStringExtra(Constants.EXTRA_GENDER);
        cityId = getIntent().getStringExtra(Constants.EXTRA_CITY_ID);
        countryId = getIntent().getStringExtra(Constants.EXTRA_COUNTRY_ID);

        btSubmit.setOnClickListener(v -> attemptVerification());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                    PERMISSIONS_REQUEST_SMS);
        } else {
            sendOtp();
        }
    }

    private void sendOtp() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.sendOtp(this, onOtpSuccessListener, OtpResponse.class, onErrorListener, Constants.COUNTRY_CODE, mobileNumber);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_SMS:
                // If request is cancelled, the result arrays are empty.
                sendOtp();

                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                            UiUtil.showToast(VerificationActivity.this, getString(R.string.sms_permission_denied));
                            // permission denied, boo! Disable the
                            // functionality that depends on this permission.
                        }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

    private static void displayOtp(String otp) {
        etOtp.setText(otp);

        sendVerifyOtpRequest(otp);
    }

    private static void sendVerifyOtpRequest(String otp) {

        if (sentOtp.equalsIgnoreCase(otp)) {
            registerUser();
        } else {
            Toast.makeText(context, context.getString(R.string.incorrect_otp), Toast.LENGTH_SHORT).show();
        }
    }

    private static void registerUser() {

        Map<String, String> request = new HashMap<>();
        request.put(Constants.FirstName, firstName);
        request.put(Constants.LastName, lastName);
        request.put(Constants.Mobile, mobileNumber);
        request.put(Constants.EMail, emailId);
        request.put(Constants.UserTypeId, "0");
        request.put(Constants.Latitude, latitude);
        request.put(Constants.Longitude, longitude);
        request.put(Constants.DOB, dob);
        request.put(Constants.Gender, gender);
        request.put(Constants.cityid, cityId);
        request.put(Constants.countryid, countryId);

        UiUtil.showProgressDialog(context, context.getString(R.string.please_wait));
        DataFetcher.registerUser(context, new Gson().toJson(request), onRegisterUserSuccessListener, RegisterUserResponse.class, onErrorListener);
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

    private static Response.Listener<RegisterUserResponse> onRegisterUserSuccessListener = new Response.Listener<RegisterUserResponse>() {
        @Override
        public void onResponse(RegisterUserResponse response) {

            UiUtil.cancelProgressDialog();
            if (response != null && response.getStatus().equals(Constants.STATUS_SUCCESS)) {
                SharedPreferences.Editor editor = AppPrefs.getInstance(context).getSharedPreferences().edit();
                editor.putBoolean(PREFS_IF_USER_LOGGED_IN, true);
                editor.putString(PREFS_USER_ID, response.getUserId());
                editor.putString(PREFS_TOKEN, response.getAuthenticationResult());
                editor.putString(PREFS_USER_NAME, getFullName());
                editor.apply();
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                UiUtil.showToast(context, context.getString(R.string.user_registered_successfully));
            } else
                UiUtil.showToast(context, context.getString(R.string.err_occurred));
        }
    };

    private static String getFullName() {
        String fullName = firstName + " " + lastName;
        return fullName.trim();
    }

    private static Response.ErrorListener onErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            LogUtils.LOGD("ERROR", error.getMessage());
            UiUtil.cancelProgressDialog();
            UiUtil.showToast(context, context.getString(R.string.err_occurred));

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }

    public static class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);


                        // verification code from sms
                        String otp = getVerificationCode(message);
                        displayOtp(otp);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        /**
         * Getting the OTP from sms message body
         * ':' is the separator of OTP from the message
         *
         * @param message Full OTP message
         * @return Returns verification code
         */
        private String getVerificationCode(String message) {
            String code;
            int index = message.indexOf("-");

            if (index != -1) {
                int start = index + 2;
                int length = 4;
                code = message.substring(start, start + length);
                return code;
            }

            return null;
        }
    }
}
