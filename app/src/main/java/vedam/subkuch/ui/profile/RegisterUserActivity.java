package vedam.subkuch.ui.profile;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.hbb20.CountryCodePicker;
import vedam.subkuch.network.Response;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityRegisterUserBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.DateTimeUtils;
import vedam.subkuch.utils.UiUtil;

public class RegisterUserActivity extends BaseActivity {
    private static final int MINIMUM_AGE_YEARS = 12;
    private static final int EARLIEST_BIRTH_YEAR = 1900;
    private static final String DOB_PICKER_TAG = "date_of_birth_picker";

    private ActivityRegisterUserBinding activityRegisterUserBinding;
    private String latitude;
    private String longitude;
    private String gender;
    private String countryId;
    private String stateId;
    private String cityId;
    private String languageId;
    private String countryCode;
    private boolean syncingCountryPicker;
    private final ArrayList<RegistrationMasterOption> countryOptions = new ArrayList<>();
    private final ArrayList<RegistrationMasterOption> states = new ArrayList<>();
    private final ArrayList<RegistrationMasterOption> districts = new ArrayList<>();
    private boolean districtAutoRefetchDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegisterUserBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_register_user);
        configurePhoneCountryCodePicker();
        bindData();
        bindSelectionKeyboardDismissal();
        initializeMasterSpinners();
        loadMasterData();
        requestLocation(true);

    }

    private void loadMasterData() {
        if (!DataFetcher.isRegistrationApiConfigured()) {
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        DataFetcher.getRegistrationCountries(this, this::setCountries,
                RegistrationMasterResponse.class, onErrorListener);
        DataFetcher.getRegistrationStates(this, this::setStates,
                RegistrationMasterResponse.class, onErrorListener);

        ArrayList<RegistrationMasterOption> cachedDistricts =
                RegistrationMasterCache.getDistricts(this);
        boolean hasCachedDistricts = !cachedDistricts.isEmpty();
        if (hasCachedDistricts) {
            showDistricts(cachedDistricts);
        }
        if (!hasCachedDistricts || !RegistrationMasterCache.areDistrictsFresh(this)) {
            DataFetcher.getRegistrationDistricts(this, this::setDistricts,
                    RegistrationMasterResponse.class, error -> {
                        if (!hasCachedDistricts) onErrorReceived(error);
                    });
        }

        ArrayList<RegistrationMasterOption> cachedLanguages =
                RegistrationMasterCache.getLanguages(this);
        boolean hasCachedLanguages = !cachedLanguages.isEmpty();
        if (hasCachedLanguages) {
            showLanguages(cachedLanguages);
        }
        if (!hasCachedLanguages || !RegistrationMasterCache.areLanguagesFresh(this)) {
            DataFetcher.getRegistrationLanguages(this, this::setLanguages,
                    RegistrationMasterResponse.class, error -> {
                        if (!hasCachedLanguages) onErrorReceived(error);
                    });
        }
    }

    private void initializeMasterSpinners() {
        bindCountrySpinner(new ArrayList<>());
        updateStatesForCountry();
        updateDistrictsForState();
        activityRegisterUserBinding.spLanguage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, withPlaceholder(new ArrayList<>(),
                getString(R.string.select_app_language))));
    }

    private boolean hasMasterData(RegistrationMasterResponse response) {
        return response != null && response.getReturnData() != null && !response.getReturnData().isEmpty();
    }

    private void setCountries(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        bindCountrySpinner(response.getReturnData());
    }

    private void setStates(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        states.clear();
        states.addAll(response.getReturnData());
        updateStatesForCountry();
    }

    private void setDistricts(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        RegistrationMasterCache.putDistricts(this, response.getReturnData());
        showDistricts(response.getReturnData());
    }

    private void showDistricts(List<RegistrationMasterOption> values) {
        districts.clear();
        districts.addAll(values);
        updateDistrictsForState();
    }

    private void setLanguages(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        RegistrationMasterCache.putLanguages(this, response.getReturnData());
        showLanguages(response.getReturnData());
    }

    private void showLanguages(List<RegistrationMasterOption> values) {
        ArrayList<RegistrationMasterOption> options = withPlaceholder(
                values, getString(R.string.select_app_language));
        activityRegisterUserBinding.spLanguage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        activityRegisterUserBinding.spLanguage.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
                if (position == 0) return;
                RegistrationMasterOption language = (RegistrationMasterOption) parent.getItemAtPosition(position);
                AppPrefs.getInstance(RegisterUserActivity.this).getSharedPreferences().edit()
                        .putString(AppPrefs.PREFS_APP_LANGUAGE_ID, languageId)
                        .putString(AppPrefs.PREFS_APP_LANGUAGE_NAME, language.getName())
                .apply();
            }
        });
        activityRegisterUserBinding.spLanguage.setSelection(findOptionPosition(options, "English"));
    }

    private void bindCountrySpinner(List<RegistrationMasterOption> countries) {
        ArrayList<RegistrationMasterOption> options = withPlaceholder(countries, getString(R.string.select_a_country));
        countryOptions.clear();
        countryOptions.addAll(options);
        activityRegisterUserBinding.spCountry.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        activityRegisterUserBinding.spCountry.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RegistrationMasterOption country = position == 0 ? null :
                        (RegistrationMasterOption) parent.getItemAtPosition(position);
                countryId = country == null ? null : String.valueOf(country.getId());
                countryCode = country == null ? null : country.getCountryCode();
                syncPhoneCountryCodePicker(countryCode);
                stateId = null;
                cityId = null;
                updateStatesForCountry();
            }
        });
        activityRegisterUserBinding.spCountry.setSelection(findOptionPosition(options, "India"));
    }

    private void configurePhoneCountryCodePicker() {
        CountryCodePicker picker = activityRegisterUserBinding.ccpCountryCode;
        picker.setDefaultCountryUsingNameCode("IN");
        picker.resetToDefaultCountry();
        picker.setOnCountryChangeListener(() -> {
            if (syncingCountryPicker) return;
            String selectedCode = picker.getSelectedCountryCode();
            for (int index = 1; index < countryOptions.size(); index++) {
                RegistrationMasterOption country = countryOptions.get(index);
                if (selectedCode.equals(normalizeCountryCode(country.getCountryCode()))) {
                    activityRegisterUserBinding.spCountry.setSelection(index);
                    return;
                }
            }
        });
    }

    private void syncPhoneCountryCodePicker(String value) {
        String code = normalizeCountryCode(value);
        try {
            syncingCountryPicker = true;
            activityRegisterUserBinding.ccpCountryCode.setCountryForPhoneCode(Integer.parseInt(code));
        } catch (NumberFormatException ignored) {
            // Keep the currently selected dial code if the registration API sends an invalid one.
        } finally {
            syncingCountryPicker = false;
        }
    }

    private void updateStatesForCountry() {
        ArrayList<RegistrationMasterOption> options = withPlaceholder(filterByCountry(states),
                getString(R.string.select_a_state));
        activityRegisterUserBinding.spState.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        activityRegisterUserBinding.spState.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
                cityId = null;
                updateDistrictsForState();
            }
        });
    }

    private void updateDistrictsForState() {
        ArrayList<RegistrationMasterOption> filtered = filterDistricts();
        if (filtered.isEmpty() && stateId != null && !districts.isEmpty() && !districtAutoRefetchDone) {
            // A valid state produced no districts: the cached master list is likely
            // outdated (e.g. districts added server-side after the cache was written).
            // Re-fetch once per screen instead of leaving an empty dropdown.
            districtAutoRefetchDone = true;
            forceReloadDistricts();
        }
        ArrayList<RegistrationMasterOption> options = withPlaceholder(filtered,
                getString(R.string.select_a_district));
        activityRegisterUserBinding.spCity.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        activityRegisterUserBinding.spCity.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
            }
        });
    }

    private void forceReloadDistricts() {
        DataFetcher.getRegistrationDistricts(this, this::setDistricts,
                RegistrationMasterResponse.class, error -> { /* keep showing cached data */ });
    }

    private ArrayList<RegistrationMasterOption> filterByCountry(List<RegistrationMasterOption> options) {
        ArrayList<RegistrationMasterOption> filtered = new ArrayList<>();
        for (RegistrationMasterOption option : options) {
            if (countryId != null && (option.getCountryId() == null || countryId.equals(String.valueOf(option.getCountryId())))) {
                filtered.add(option);
            }
        }
        return filtered;
    }

    private ArrayList<RegistrationMasterOption> filterDistricts() {
        ArrayList<RegistrationMasterOption> filtered = new ArrayList<>();
        for (RegistrationMasterOption option : districts) {
            boolean countryMatches = option.getCountryId() == null || countryId != null && countryId.equals(String.valueOf(option.getCountryId()));
            boolean stateMatches = option.getStateId() == null || stateId != null && stateId.equals(String.valueOf(option.getStateId()));
            if (countryMatches && stateMatches) filtered.add(option);
        }
        return filtered;
    }

    private ArrayList<RegistrationMasterOption> withPlaceholder(List<RegistrationMasterOption> options, String label) {
        ArrayList<RegistrationMasterOption> values = new ArrayList<>();
        values.add(RegistrationMasterOption.placeholder(label));
        values.addAll(options);
        return values;
    }

    private int findOptionPosition(List<RegistrationMasterOption> options, String name) {
        for (int index = 1; index < options.size(); index++) {
            if (name.equalsIgnoreCase(options.get(index).getName())) return index;
        }
        return 0;
    }

    private abstract static class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) { }
    }

    /*private void getCountries() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getCountries(this, onCountriesSuccessListener, CountriesResponse.class, onErrorListener);
    }*/

    /*private Response.Listener<CountriesResponse> onCountriesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setCountries(response.getCountries());
        } else {
            UiUtil.showToast(RegisterUserActivity.this, getString(R.string.err_occurred));
        }
    };*/

    /*private void setCountries(ArrayList<Country> countries) {

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

    }*/

    /*private int getIndexOfIndia(ArrayList<Country> countries) {

        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getName().equalsIgnoreCase("India"))
                return i;
        }
        return 0;
    }*/

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

                Profile profile = new Profile();
                profile.setFirstName(AppUtil.capitalize(activityRegisterUserBinding.etFirstName.getText().toString()));
                profile.setLastName(AppUtil.capitalize(activityRegisterUserBinding.etLastName.getText().toString()));
                profile.setMobile(activityRegisterUserBinding.etMobileNumber.getText().toString());
                profile.setEMail(activityRegisterUserBinding.etEmail.getText().toString());
                profile.setOccupationOther("");
                profile.setOccupationid("0");
                profile.setDOB(DateTimeUtils.getFormattedDate(activityRegisterUserBinding.etDob.getText().toString(),
                        DateTimeUtils.DATE_FORMAT_3, DateTimeUtils.DEFAULT_DATE_FORMAT));
                profile.setGender(gender);
                profile.setCityId(cityId);
                profile.setCountryid(countryId);
                profile.setLatitude(latitude);
                profile.setLongitude(longitude);
                AppPrefs.getInstance(this).getSharedPreferences().edit()
                        .putString(Constants.EXTRA_COUNTRY_CODE,
                                activityRegisterUserBinding.ccpCountryCode.getSelectedCountryCode()).apply();
                intent.putExtra(Constants.EXTRA_DATA, profile);
                intent.putExtra(Constants.EXTRA_STATE_ID, stateId);
                intent.putExtra(Constants.EXTRA_LANGUAGE_ID, languageId);
                startActivity(intent);
            } else {
                UiUtil.showDialog(RegisterUserActivity.this, getString(errorMessage), true);
            }
        });

        activityRegisterUserBinding.etDob.setOnClickListener(v -> {
            dismissKeyboard();
            showDatePickerDialog();
        });
        activityRegisterUserBinding.tilDob.setEndIconOnClickListener(v -> {
            dismissKeyboard();
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

    private void bindSelectionKeyboardDismissal() {
        View.OnTouchListener listener = (view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                dismissKeyboard();
            } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                view.performClick();
            }
            return false;
        };
        activityRegisterUserBinding.spCountry.setOnTouchListener(listener);
        activityRegisterUserBinding.spState.setOnTouchListener(listener);
        activityRegisterUserBinding.spCity.setOnTouchListener(listener);
        activityRegisterUserBinding.spGender.setOnTouchListener(listener);
        activityRegisterUserBinding.spLanguage.setOnTouchListener(listener);
    }

    private void dismissKeyboard() {
        View focusedView = getCurrentFocus();
        UiUtil.hideKeyBoard(this, focusedView != null
                ? focusedView : activityRegisterUserBinding.getRoot());
        if (focusedView != null) {
            focusedView.clearFocus();
        }
    }

    public void showDatePickerDialog() {
        Calendar localToday = Calendar.getInstance();
        Calendar maximumDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        maximumDate.clear();
        maximumDate.set(
                localToday.get(Calendar.YEAR),
                localToday.get(Calendar.MONTH),
                localToday.get(Calendar.DAY_OF_MONTH));
        maximumDate.add(Calendar.YEAR, -MINIMUM_AGE_YEARS);

        Calendar minimumDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        minimumDate.clear();
        minimumDate.set(EARLIEST_BIRTH_YEAR, Calendar.JANUARY, 1);

        Long existingSelection = parseDateOfBirth(
                activityRegisterUserBinding.etDob.getText().toString());
        long openAt = existingSelection != null
                ? existingSelection
                : maximumDate.getTimeInMillis();

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setStart(minimumDate.getTimeInMillis())
                .setEnd(maximumDate.getTimeInMillis())
                .setOpenAt(openAt)
                .setValidator(DateValidatorPointBackward.before(maximumDate.getTimeInMillis()))
                .build();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date_of_birth)
                .setCalendarConstraints(constraints);
        if (existingSelection != null) {
            builder.setSelection(existingSelection);
        }

        MaterialDatePicker<Long> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection ->
                activityRegisterUserBinding.etDob.setText(formatDateOfBirth(selection)));
        datePicker.show(getSupportFragmentManager(), DOB_PICKER_TAG);
    }

    private Long parseDateOfBirth(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        SimpleDateFormat formatter = createDateOfBirthFormatter();
        formatter.setLenient(false);
        try {
            Date parsedDate = formatter.parse(value);
            return parsedDate != null ? parsedDate.getTime() : null;
        } catch (ParseException ignored) {
            return null;
        }
    }

    private String formatDateOfBirth(long selection) {
        return createDateOfBirthFormatter().format(new Date(selection));
    }

    private SimpleDateFormat createDateOfBirthFormatter() {
        SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtils.DATE_FORMAT_3, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter;
    }

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityRegisterUserBinding.etFirstName.getText()))
            errorMessage = R.string.enter_given_name;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etLastName.getText()))
            errorMessage = R.string.enter_surname;
        else if (activityRegisterUserBinding.etFirstName.getText().length() < 3)
            errorMessage = R.string.minimum_3_characters_first_name;
        else if (!AppUtil.isStringOnlyAlphabet(activityRegisterUserBinding.etFirstName.getText().toString()) ||
                !AppUtil.isStringName(activityRegisterUserBinding.etLastName.getText().toString()))
            errorMessage = R.string.no_special_characters_allowed_in_name;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etEmail.getText()))
            errorMessage = R.string.enter_email;
        else if (!AppUtil.validateEmail(activityRegisterUserBinding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (TextUtils.isEmpty(activityRegisterUserBinding.etMobileNumber.getText()))
            errorMessage = R.string.enter_mobile;
        else if (activityRegisterUserBinding.etMobileNumber.getText().toString().length() < 6 ||
                activityRegisterUserBinding.etMobileNumber.getText().toString().length() > 15 ||
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
        else if (TextUtils.isEmpty(stateId))
            errorMessage = R.string.select_a_state;
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_district;
        else if (TextUtils.isEmpty(languageId))
            errorMessage = R.string.select_app_language;
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude))
            errorMessage = R.string.submit_after_location;
        return errorMessage;
    }

    private String normalizeCountryCode(String value) {
        if (TextUtils.isEmpty(value)) return "91";
        return value.startsWith("+") ? value.substring(1) : value;
    }

}
