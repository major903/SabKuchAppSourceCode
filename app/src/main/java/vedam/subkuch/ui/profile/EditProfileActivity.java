package vedam.subkuch.ui.profile;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityEditProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.CountriesResponse;
import vedam.subkuch.network.models.Country;
import vedam.subkuch.network.models.ProfileRequest;
import vedam.subkuch.network.models.RegisterUserResponse;
import vedam.subkuch.ui.jobs.models.CitiesResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding activityEditProfileBinding;
    private String latitude;
    private String longitude;
    private String countryId;
    private String countryCode;
    private String cityId;
    private ProfileRequest profileRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditProfileBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_edit_profile);
        profileRequest = new ProfileRequest();
        setToolbarBackButton();
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
        activityEditProfileBinding.spCity.setAdapter(adapter);
        activityEditProfileBinding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = ((City) parent.getItemAtPosition(position)).getCityid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activityEditProfileBinding.spCity.setSelection(0);
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
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.err_occurred));
        }
    };

    private void setCountries(ArrayList<Country> countries) {

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, countries);
        activityEditProfileBinding.spCountry.setAdapter(adapter);
        activityEditProfileBinding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = ((Country) parent.getItemAtPosition(position)).getCountryid();
                countryCode = ((Country) parent.getItemAtPosition(position)).getCountrycode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityEditProfileBinding.spCountry.setSelection(getIndexOfIndia(countries));

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

        activityEditProfileBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
                profileRequest.setProfileId(userId);
                profileRequest.setFirstName(activityEditProfileBinding.etFirstName.getText().toString());
                profileRequest.setLastName(AppUtil.deNull(activityEditProfileBinding.etLastName.getText()));
                profileRequest.setEMail(activityEditProfileBinding.etEmail.getText().toString());
                profileRequest.setCityId(cityId);
                profileRequest.setCountryid(countryId);
                profileRequest.setLatitude(latitude);
                profileRequest.setLongitude(longitude);
                AppPrefs.getInstance(this).getSharedPreferences().edit()
                        .putString(Constants.EXTRA_COUNTRY_CODE, countryCode).apply();
                updateUser();

            } else {
                UiUtil.showDialog(EditProfileActivity.this, getString(errorMessage), true);
            }
        });
    }

    private void updateUser() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.editProfile(this, new Gson().toJson(profileRequest), onRegisterUserSuccessListener, RegisterUserResponse.class, onErrorListener);
    }

    private Response.Listener<RegisterUserResponse> onRegisterUserSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)
                && response.getReturnData() != null) {
            SharedPreferences.Editor editor = AppPrefs.getInstance(EditProfileActivity.this).getSharedPreferences().edit();
            editor.putString(PREFS_USER_NAME, getFullName());
            editor.apply();
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.profile_updated_successfully));
            finish();
        } else
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.err_occurred));
    };

    private String getFullName() {
        String fullName = profileRequest.getFirstName() + " " + profileRequest.getLastName();
        return fullName.trim();
    }

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityEditProfileBinding.etFirstName.getText()))
            errorMessage = R.string.enter_first_name;
        else if (TextUtils.isEmpty(activityEditProfileBinding.etEmail.getText()))
            errorMessage = R.string.enter_email;
        else if (!AppUtil.validateEmail(activityEditProfileBinding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }
}
