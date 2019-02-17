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
import java.util.Stack;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityEditProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.ProfileResponse;
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
    /*private String countryId;
    private String countryCode;*/
    private String cityId;
    private Profile profile;
    private Stack<Object> requestStack = new Stack<>();
//    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditProfileBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_edit_profile);
        profile = new Profile();
        setTitle(getString(R.string.edit_profile));
        setToolbarBackButton();
        bindData();
//        requestStack.add(new Object());
        requestStack.add(new Object());
        UiUtil.showProgressDialog(this, getString(R.string.loading));
        getCities();
//        getCountries();
        requestLocation(true);
    }

    private void getCities() {

        DataFetcher.getCities(this, onCitiesSuccessListener, CitiesResponse.class, onErrorListener);
    }

    private Response.Listener<CitiesResponse> onCitiesSuccessListener = response -> {
        requestStack.pop();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            cities = response.getReturnData();
            setCities();
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
        checkFlagAndLoadUI();
    };

    private void setCities() {

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

    /*private void getCountries() {

        DataFetcher.getCountries(this, onCountriesSuccessListener, CountriesResponse.class, onErrorListener);
    }

    private Response.Listener<CountriesResponse> onCountriesSuccessListener = response -> {
        requestStack.pop();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            countries = response.getCountries();
            setCountries();
        } else {
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.err_occurred));
        }
        checkFlagAndLoadUI();
    };

    private void setCountries() {

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
        activityEditProfileBinding.spCountry.setSelection(getIndexOfIndia());

    }

    private int getIndexOfIndia() {

        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getName().equalsIgnoreCase(getString(R.string.india)))
                return i;
        }
        return 0;
    }*/

    private void checkFlagAndLoadUI() {
        UiUtil.cancelProgressDialog();
        if (requestStack.isEmpty()) {
            getProfile();
        }
    }

    private void getProfile() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getProfile(this, onProfileSuccessListener, ProfileResponse.class, onErrorListener);
    }

    private Response.Listener<ProfileResponse> onProfileSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) &&
                response.getReturnData() != null && response.getReturnData().size() > 0) {
            setValues(response.getReturnData().get(0));
        } else {
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.err_occurred));
        }
    };

    private void setValues(Profile profile) {
        UiUtil.setTextView(activityEditProfileBinding.etEmail, profile.getEMail());
        UiUtil.setTextView(activityEditProfileBinding.etFirstName, profile.getFirstName());
        UiUtil.setTextView(activityEditProfileBinding.etLastName, profile.getLastName());

        /*int indexOfCurrentCountry = getIndexOfCurrentCountry(profile.getCountryid());
        activityEditProfileBinding.spCountry.setSelection(indexOfCurrentCountry);*/

        int indexOfCurrentCity = getIndexOfCurrentCity(profile.getCityId());
        activityEditProfileBinding.spCity.setSelection(indexOfCurrentCity);

    }

//    private int getIndexOfCurrentCountry(String countryid) {
//
//        for (int i = 0; i < countries.size(); i++) {
//            Country country = countries.get(i);
//            if (country.getCountryid().equals(countryid))
//                return i;
//        }
//        return 0;
//    }

    private int getIndexOfCurrentCity(String cityId) {

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (!TextUtils.isEmpty(city.getCityid()) && city.getCityid().equals(cityId))
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
                profile.setProfileId(userId);
                profile.setFirstName(activityEditProfileBinding.etFirstName.getText().toString());
                profile.setLastName(AppUtil.deNull(activityEditProfileBinding.etLastName.getText()));
                profile.setEMail(activityEditProfileBinding.etEmail.getText().toString());
                profile.setCityId(cityId);
//                profile.setCountryid(countryId);
                profile.setLatitude(latitude);
                profile.setLongitude(longitude);
                /*AppPrefs.getInstance(this).getSharedPreferences().edit()
                        .putString(Constants.EXTRA_COUNTRY_CODE, countryCode).apply();*/
                updateUser();

            } else {
                UiUtil.showDialog(EditProfileActivity.this, getString(errorMessage), true);
            }
        });
    }

    private void updateUser() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.editProfile(this, new Gson().toJson(profile), onRegisterUserSuccessListener, ProfileResponse.class, onErrorListener);
    }

    private Response.Listener<ProfileResponse> onRegisterUserSuccessListener = response -> {

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
        String fullName = profile.getFirstName() + " " + profile.getLastName();
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
        /*else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;*/
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }
}
