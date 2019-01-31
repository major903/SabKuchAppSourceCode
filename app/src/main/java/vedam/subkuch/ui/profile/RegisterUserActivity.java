package vedam.subkuch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.android.volley.Response;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityRegisterUserBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.CountriesResponse;
import vedam.subkuch.network.models.Country;
import vedam.subkuch.network.models.ProfileRequest;
import vedam.subkuch.ui.jobs.models.CitiesResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.uicomponent.DatePickerFragment;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class RegisterUserActivity extends BaseActivity implements DatePickerFragment.DateSetListener, DatePickerDialog.OnDateSetListener {
    private ActivityRegisterUserBinding activityRegisterUserBinding;
    private String latitude;
    private String longitude;
    private String gender;
    private String countryId;
    private String countryCode;
    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterUserBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_register_user);
        bindData();
        getCities();
        getCountries();
        requestLocation(true);

    }

    private void getCities() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getCities(this, onCitiesSuccessListener, CitiesResponse.class, onErrorListener);
    }

    private Response.Listener<CitiesResponse> onCitiesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setCities(response.getReturnData());
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }


    };

    private void setCities(ArrayList<City> cities) {

        City city = new City();
        city.setName(getString(R.string.select_a_city));
        cities.add(0, city);

        ArrayAdapter<City> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cities);
        activityRegisterUserBinding.spCity.setAdapter(adapter);
        activityRegisterUserBinding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = ((City) parent.getItemAtPosition(position)).getCityid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activityRegisterUserBinding.spCity.setSelection(0);
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
                android.R.layout.simple_spinner_dropdown_item, countries);
        activityRegisterUserBinding.spCountry.setAdapter(adapter);
        activityRegisterUserBinding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = ((Country) parent.getItemAtPosition(position)).getCountryid();
                countryCode = ((Country) parent.getItemAtPosition(position)).getCountrycode();
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

                ProfileRequest profileRequest = new ProfileRequest();
                profileRequest.setFirstName(activityRegisterUserBinding.etFirstName.getText().toString());
                profileRequest.setLastName(AppUtil.deNull(activityRegisterUserBinding.etLastName.getText()));
                profileRequest.setMobile(activityRegisterUserBinding.etMobileNumber.getText().toString());
                profileRequest.setEMail(activityRegisterUserBinding.etEmail.getText().toString());
                profileRequest.setDOB(activityRegisterUserBinding.etDob.getText().toString());
                profileRequest.setGender(gender);
                profileRequest.setCityId(cityId);
                profileRequest.setCountryid(countryId);
                profileRequest.setLatitude(latitude);
                profileRequest.setLongitude(longitude);
                AppPrefs.getInstance(this).getSharedPreferences().edit()
                        .putString(Constants.EXTRA_COUNTRY_CODE, countryCode).apply();
                intent.putExtra(Constants.EXTRA_DATA, profileRequest);
                startActivity(intent);
            } else {
                UiUtil.showDialog(RegisterUserActivity.this, getString(errorMessage), true);
            }
        });

        activityRegisterUserBinding.etDob.setOnClickListener(v -> {
            showDatePickerDialog();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender_list));
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

    public void showDatePickerDialog() {

        long millis = System.currentTimeMillis() - 378683112000L;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        new SpinnerDatePickerDialogBuilder()
                .context(this)
                .callback(this)
                .spinnerTheme(R.style.DatePickerTheme)
                .maxDate(mYear, mMonth, mDay)
                .defaultDate(mYear, mMonth, mDay)
                .build()
                .show();

//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), getString(R.string.date_picker));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(AppUtil.getZeroedString(day)).append("/").append(AppUtil.getZeroedString(month + 1))
//                .append("/").append(year);
//        activityRegisterUserBinding.etDob.setText(stringBuilder);
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
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AppUtil.getZeroedString(dayOfMonth)).append("/").append(AppUtil.getZeroedString(monthOfYear + 1))
                .append("/").append(year);
        activityRegisterUserBinding.etDob.setText(stringBuilder);
    }
}
