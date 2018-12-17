package vedam.subkuch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Response;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityRegisterUserBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.CountriesResponse;
import vedam.subkuch.network.models.Country;
import vedam.subkuch.ui.jobs.AddJobsActivity;
import vedam.subkuch.ui.jobs.CitiesResponse;
import vedam.subkuch.ui.jobs.City;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class RegisterUserActivity extends BaseActivity {
    private ActivityRegisterUserBinding activityRegisterUserBinding;
    private String latitude;
    private String longitude;
    private String gender;
    private String countryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterUserBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_register_user);
        bindData();
        getCountries();
        requestLocation();

    }

    private void getCountries() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getCountries(this, onCountriesSuccessListener, CountriesResponse.class, onErrorListener);
    }

    private Response.Listener<CountriesResponse> onCountriesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setCountries(response.getCountries());
        } else {
            UiUtil.showToast(RegisterUserActivity.this, getString(R.string.err_occurred));
        }


    };

    private void setCountries(ArrayList<Country> countries) {

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        activityRegisterUserBinding.spCountry.setAdapter(adapter);
        activityRegisterUserBinding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = ((Country) parent.getItemAtPosition(position)).getCountryid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityRegisterUserBinding.spCountry.setSelection(getIndexOfIndia(countries));

    }

    private int getIndexOfIndia(ArrayList<Country> countries) {

        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getName().equalsIgnoreCase("India"))
                return i;
        }
        return 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }

    private void bindData() {

        activityRegisterUserBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                Intent intent = new Intent(RegisterUserActivity.this, VerificationActivity.class);
                intent.putExtra(Constants.EXTRA_FIRST_NAME, activityRegisterUserBinding.etFirstName.getText().toString());
                intent.putExtra(Constants.EXTRA_LAST_NAME, AppUtil.deNull(activityRegisterUserBinding.etLastName.getText()));
                intent.putExtra(Constants.EXTRA_MOBILE_NUMBER, activityRegisterUserBinding.etMobileNumber.getText().toString());
                intent.putExtra(Constants.EXTRA_EMAIL_ID, activityRegisterUserBinding.etEmail.getText().toString());
                intent.putExtra(Constants.EXTRA_DOB, activityRegisterUserBinding.etDob.getText().toString());
                intent.putExtra(Constants.EXTRA_GENDER, gender);
                intent.putExtra(Constants.EXTRA_COUNTRY_ID, countryId);
                intent.putExtra(Constants.EXTRA_LOCATION_LATITUDE, latitude);
                intent.putExtra(Constants.EXTRA_LOCATION_LONGITUDE, longitude);
                startActivity(intent);
            } else {
                UiUtil.showDialog(RegisterUserActivity.this, getString(errorMessage), true);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender_list));
        activityRegisterUserBinding.spGender.setAdapter(adapter);
        activityRegisterUserBinding.spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    gender = parent.getItemAtPosition(position).toString();
                else
                    gender = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityRegisterUserBinding.etFirstName.getText()))
            errorMessage = R.string.enter_first_name;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etEmail.getText()))
            errorMessage = R.string.enter_email;
        else if (!AppUtil.validateEmail(activityRegisterUserBinding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etMobileNumber.getText()))
            errorMessage = R.string.enter_mobile;
        else if (activityRegisterUserBinding.etMobileNumber.getText().toString().length() != 10 ||
                !AppUtil.isNumeric(activityRegisterUserBinding.etMobileNumber.getText().toString()))
            errorMessage = R.string.enter_mobile;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etDob.getText()))
            errorMessage = R.string.enter_dob;
        else if (!AppUtil.validateDob(activityRegisterUserBinding.etDob.getText().toString()))
            errorMessage = R.string.enter__valid_dob;
        else if (TextUtils.isEmpty(gender))
            errorMessage = R.string.select_a_gender;
        else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }
}
