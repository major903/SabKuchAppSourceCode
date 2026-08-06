package vedam.subkuch.ui.profile;

import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityEditProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.network.models.UpdateUserRequest;
import vedam.subkuch.network.models.UpdateUserResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.DeviceIdProvider;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.AppPrefs.PREFS_USER_NAME;

public class EditProfileActivity extends BaseActivity {

    private ActivityEditProfileBinding activityEditProfileBinding;
    private String latitude;
    private String longitude;
    private String countryId = "1";
    /*private String countryCode;*/
    private String stateId;
    private String districtId;
    private String languageId;
    private Profile profile;
    private boolean hasDistrictData;
    private boolean hasStateData;
    private boolean hasLanguageData;
//    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<RegistrationMasterOption> states = new ArrayList<>();
    private ArrayList<RegistrationMasterOption> districts = new ArrayList<>();
    private boolean restoringProfileLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditProfileBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_edit_profile);
        initUI();
        bindCallbacks();
        loadStates();
        loadDistricts();
        loadLanguages();
        getProfile();
//        getCountries();
        requestLocation(true);
    }

    private void initUI() {

        profile = new Profile();
        setTitle(getString(R.string.edit_profile));
        setToolbarBackButton();
        activityEditProfileBinding.etFirstName.setKeyListener(null);
        activityEditProfileBinding.etFirstName.setEnabled(false);
    }

    private void loadStates() {
        DataFetcher.getRegistrationStates(this, onStatesSuccessListener,
                RegistrationMasterResponse.class, error -> {
                    if (!hasStateData) {
                        onErrorReceived(error);
                    }
                });
    }

    private void loadDistricts() {
        ArrayList<RegistrationMasterOption> cachedDistricts =
                RegistrationMasterCache.getDistricts(this);
        boolean hasCachedDistricts = !cachedDistricts.isEmpty();
        if (hasCachedDistricts) {
            showDistricts(cachedDistricts);
        }
        if (hasCachedDistricts && RegistrationMasterCache.areDistrictsFresh(this)) {
            return;
        }
        DataFetcher.getRegistrationDistricts(this, onDistrictsSuccessListener,
                RegistrationMasterResponse.class, error -> {
                    if (!hasCachedDistricts) {
                        onErrorReceived(error);
                    }
                });
    }

    private void loadLanguages() {
        ArrayList<RegistrationMasterOption> cachedLanguages =
                RegistrationMasterCache.getLanguages(this);
        boolean hasCachedLanguages = !cachedLanguages.isEmpty();
        if (hasCachedLanguages) {
            setLanguages(cachedLanguages);
        }
        if (hasCachedLanguages && RegistrationMasterCache.areLanguagesFresh(this)) {
            return;
        }
        DataFetcher.getRegistrationLanguages(this, onLanguagesSuccessListener,
                RegistrationMasterResponse.class, error -> {
                    if (!hasCachedLanguages) {
                        onErrorReceived(error);
                    }
                });
    }

    private Response.Listener<RegistrationMasterResponse> onDistrictsSuccessListener = response -> {
        if (response != null && Constants.SUCCESS.equals(response.getReturnMessage())
                && response.getReturnData() != null && !response.getReturnData().isEmpty()) {
            RegistrationMasterCache.putDistricts(this, response.getReturnData());
            showDistricts(response.getReturnData());
        } else if (!hasDistrictData) {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
    };

    private Response.Listener<RegistrationMasterResponse> onStatesSuccessListener = response -> {
        if (response != null && Constants.SUCCESS.equals(response.getReturnMessage())
                && response.getReturnData() != null && !response.getReturnData().isEmpty()) {
            showStates(response.getReturnData());
        } else if (!hasStateData) {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
    };

    private Response.Listener<RegistrationMasterResponse> onLanguagesSuccessListener = response -> {
        if (response != null && Constants.SUCCESS.equals(response.getReturnMessage())
                && response.getReturnData() != null && !response.getReturnData().isEmpty()) {
            RegistrationMasterCache.putLanguages(this, response.getReturnData());
            setLanguages(response.getReturnData());
        } else if (!hasLanguageData) {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
    };

    private void showDistricts(ArrayList<RegistrationMasterOption> values) {
        hasDistrictData = true;
        districts = new ArrayList<>(values);
        applyProfileStateSelection();
        updateDistrictsForState();
    }

    private void showStates(ArrayList<RegistrationMasterOption> values) {
        hasStateData = true;
        states = new ArrayList<>(values);
        states.add(0, RegistrationMasterOption.placeholder(getString(R.string.select_a_state)));

        ArrayAdapter<RegistrationMasterOption> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, states);
        activityEditProfileBinding.spState.setAdapter(adapter);
        activityEditProfileBinding.spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RegistrationMasterOption state =
                        (RegistrationMasterOption) parent.getItemAtPosition(position);
                String selectedStateId = state.getId() == 0 ? null : String.valueOf(state.getId());
                if (!restoringProfileLocation && !TextUtils.equals(stateId, selectedStateId)) {
                    districtId = null;
                }
                stateId = selectedStateId;
                updateDistrictsForState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        applyProfileStateSelection();
    }

    private void updateDistrictsForState() {
        if (!hasDistrictData || !hasStateData) return;
        String selectedDistrictId = districtId;
        ArrayList<RegistrationMasterOption> options = new ArrayList<>();
        options.add(RegistrationMasterOption.placeholder(getString(R.string.select_a_city)));
        for (RegistrationMasterOption district : districts) {
            boolean countryMatches = district.getCountryId() == null
                    || countryId.equals(String.valueOf(district.getCountryId()));
            boolean stateMatches = district.getStateId() == null
                    || stateId != null && stateId.equals(String.valueOf(district.getStateId()));
            if (countryMatches && stateMatches) {
                options.add(district);
            }
        }

        ArrayAdapter<RegistrationMasterOption> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options);
        activityEditProfileBinding.spCity.setAdapter(adapter);
        activityEditProfileBinding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RegistrationMasterOption district =
                        (RegistrationMasterOption) parent.getItemAtPosition(position);
                districtId = district.getId() == 0 ? null : String.valueOf(district.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                districtId = null;
            }
        });

        activityEditProfileBinding.spCity.setSelection(getIndexOfCurrentCity(selectedDistrictId, options));
    }

    private void setLanguages(ArrayList<RegistrationMasterOption> languages) {
        String selectedLanguageId = languageId;
        hasLanguageData = true;
        ArrayList<RegistrationMasterOption> options = new ArrayList<>();
        options.add(RegistrationMasterOption.placeholder(getString(R.string.select_app_language)));
        options.addAll(languages);
        ArrayAdapter<RegistrationMasterOption> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options);
        activityEditProfileBinding.spLanguage.setAdapter(adapter);
        activityEditProfileBinding.spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    languageId = null;
                    return;
                }
                RegistrationMasterOption language = (RegistrationMasterOption) parent.getItemAtPosition(position);
                languageId = String.valueOf(language.getId());
                AppPrefs.getInstance(EditProfileActivity.this).getSharedPreferences().edit()
                        .putString(AppPrefs.PREFS_APP_LANGUAGE_ID, languageId)
                        .putString(AppPrefs.PREFS_APP_LANGUAGE_NAME, language.getName())
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                languageId = null;
            }
        });

        String savedLanguageId = TextUtils.isEmpty(selectedLanguageId)
                ? AppPrefs.getInstance(this).getSharedPreferences()
                        .getString(AppPrefs.PREFS_APP_LANGUAGE_ID, "")
                : selectedLanguageId;
        int selectedPosition = 0;
        for (int i = 1; i < options.size(); i++) {
            if (String.valueOf(options.get(i).getId()).equals(savedLanguageId)) {
                selectedPosition = i;
                break;
            }
        }
        activityEditProfileBinding.spLanguage.setSelection(selectedPosition);
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

    private void getProfile() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getUserProfile(this, onProfileSuccessListener, Profile.class, error -> {
            UiUtil.cancelProgressDialog();
            onErrorReceived(error);
        });
    }

    private Response.Listener<Profile> onProfileSuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (response != null && !TextUtils.isEmpty(response.getProfileId())) {
            setValues(response);
        } else {
            UiUtil.showToast(EditProfileActivity.this, getString(R.string.err_occurred));
        }
    };

    private void setValues(Profile profile) {
        this.profile = profile;
        if (!TextUtils.isEmpty(profile.getCountryid())) {
            countryId = profile.getCountryid();
        }
        UiUtil.setTextViewWithVisibility(activityEditProfileBinding.etEmail, profile.getEMail());
        UiUtil.setTextViewWithVisibility(activityEditProfileBinding.etFirstName, profile.getFirstName());
        UiUtil.setTextViewWithVisibility(activityEditProfileBinding.etLastName, profile.getLastName());
        if (activityEditProfileBinding.etLastName.getText() != null)
            activityEditProfileBinding.etLastName.setSelection(activityEditProfileBinding.etLastName.getText().length());
        /*int indexOfCurrentCountry = getIndexOfCurrentCountry(profile.getCountryid());
        activityEditProfileBinding.spCountry.setSelection(indexOfCurrentCountry);*/

        districtId = profile.getCityId();
        applyProfileStateSelection();
    }

    private void applyProfileStateSelection() {
        if (profile == null || !hasStateData || districts.isEmpty()
                || activityEditProfileBinding.spState.getAdapter() == null) return;
        String profileDistrictId = profile.getCityId();
        for (RegistrationMasterOption district : districts) {
            if (TextUtils.equals(profileDistrictId, String.valueOf(district.getId()))
                    && district.getStateId() != null) {
                String profileStateId = String.valueOf(district.getStateId());
                for (int i = 1; i < states.size(); i++) {
                    if (profileStateId.equals(String.valueOf(states.get(i).getId()))) {
                        stateId = profileStateId;
                        restoringProfileLocation = true;
                        activityEditProfileBinding.spState.setSelection(i);
                        restoringProfileLocation = false;
                        return;
                    }
                }
            }
        }
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

    private int getIndexOfCurrentCity(String cityId, ArrayList<RegistrationMasterOption> options) {

        for (int i = 0; i < options.size(); i++) {
            RegistrationMasterOption district = options.get(i);
            if (district.getId() != 0 && String.valueOf(district.getId()).equals(cityId))
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

    private void bindCallbacks() {

        activityEditProfileBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                profile.setFirstName(activityEditProfileBinding.etFirstName.getText().toString());
                profile.setLastName(AppUtil.deNull(activityEditProfileBinding.etLastName.getText()));
                profile.setEMail(activityEditProfileBinding.etEmail.getText().toString());
                profile.setCityId(districtId);
                profile.setCountryid(countryId);
//                AppPrefs.getInstance(this).getSharedPreferences().edit()
//                        .putString(Constants.EXTRA_COUNTRY_CODE, countryCode).apply();
                updateUser();

            } else {
                UiUtil.showDialog(EditProfileActivity.this, getString(errorMessage), true);
            }
        });
    }

    private void updateUser() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        int userId = parseRequiredId(AppPrefs.getPrefsUserId(this));
        if (userId == 0) {
            userId = parseRequiredId(profile.getProfileId());
        }
        String deviceId = TextUtils.isEmpty(profile.getDeviceId())
                ? DeviceIdProvider.getDeviceId(this) : profile.getDeviceId();
        UpdateUserRequest request = new UpdateUserRequest(
                userId,
                profile.getFirstName(),
                profile.getLastName(),
                AppUtil.deNull(profile.getDOB()),
                AppUtil.deNull(profile.getMobile()),
                parseRequiredId(profile.getOccupationid()),
                AppUtil.deNull(profile.getOccupationOther()),
                profile.getUserTypeId(),
                deviceId,
                parseCoordinate(latitude, profile.getLatitude()),
                parseCoordinate(longitude, profile.getLongitude()),
                parseRequiredId(districtId),
                parseRequiredId(countryId));
        DataFetcher.updateUser(this, new Gson().toJson(request), onRegisterUserSuccessListener,
                UpdateUserResponse.class, onErrorListener);
    }

    private int parseRequiredId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private double parseCoordinate(String currentValue, String savedValue) {
        String value = TextUtils.isEmpty(currentValue) ? savedValue : currentValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
            return 0.0;
        }
    }

    private Response.Listener<UpdateUserResponse> onRegisterUserSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus() == 1
                && Constants.SUCCESS.equalsIgnoreCase(response.getMessage())) {
            SharedPreferences.Editor editor = AppPrefs.getInstance(EditProfileActivity.this).getSharedPreferences().edit();
            editor.putString(PREFS_USER_NAME, getFullName());
            editor.putInt(AppPrefs.PREFS_USER_GENDER, getGenderCode(profile.getGender()));
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

    private int getGenderCode(String gender) {
        if (gender == null) return 0;
        if ("1".equals(gender) || "male".equalsIgnoreCase(gender)) return 1;
        if ("2".equals(gender) || "female".equalsIgnoreCase(gender)) return 2;
        return 0;
    }

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityEditProfileBinding.etFirstName.getText()))
            errorMessage = R.string.enter_given_name;
        else if (TextUtils.isEmpty(activityEditProfileBinding.etLastName.getText()))
            errorMessage = R.string.enter_surname;
        else if (activityEditProfileBinding.etFirstName.getText().length() < 3)
            errorMessage = R.string.minimum_3_characters_first_name;
        else if (!AppUtil.isStringOnlyAlphabet(activityEditProfileBinding.etFirstName.getText().toString()) ||
                !AppUtil.isStringName(activityEditProfileBinding.etLastName.getText().toString()))
            errorMessage = R.string.no_special_characters_allowed_in_name;
        else if (TextUtils.isEmpty(activityEditProfileBinding.etEmail.getText()))
            errorMessage = R.string.enter_email;
        else if (!AppUtil.validateEmail(activityEditProfileBinding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (TextUtils.isEmpty(stateId))
            errorMessage = R.string.select_a_state;
        else if (TextUtils.isEmpty(districtId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(languageId))
            errorMessage = R.string.select_app_language;
        /*else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;*/
        else if ((TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude))
                && (TextUtils.isEmpty(profile.getLatitude()) || TextUtils.isEmpty(profile.getLongitude())))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }
}
