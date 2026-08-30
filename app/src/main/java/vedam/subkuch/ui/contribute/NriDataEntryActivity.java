package vedam.subkuch.ui.contribute;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.DataNriRequest;
import vedam.subkuch.network.models.NriListItem;
import vedam.subkuch.network.models.Profile;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

/** Form for the DataNRI/AddDataNRI contribution API. */
public class NriDataEntryActivity extends BaseActivity {

    private static final String HISTORY_CACHE_PREFS = "nri_data_entry_cache";

    private TextInputEditText etName;
    private TextInputEditText etNativePlace;
    private TextInputEditText etMobile;
    private TextInputEditText etDetails;
    private ScrollView svForm;
    private Spinner spCountry;
    private Button btSubmit;
    private TextView tvGreeting;
    private TextView tvNotice;
    private TextView tvNoDataEntries;
    private LinearLayout llDataEntries;
    private CountryCodePicker ccpCountryCode;
    private String countryCode;
    private boolean syncingCountryPicker;
    private final ArrayList<RegistrationMasterOption> countryOptions = new ArrayList<>();
    private boolean hasCachedEntries;
    private Integer countryId;
    private boolean isSubmitting;
    private int keyboardOverlayHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nri_data_entry);
        setTitle(R.string.submit_nri_data);
        setToolbarBackButton();
        tvGreeting = findViewById(R.id.tv_name);
        tvNotice = findViewById(R.id.tv_notice);
        String contribNotice = getIntent().getStringExtra(ContributeActivity.EXTRA_CONTRIB_DETAIL);
        if (TextUtils.isEmpty(contribNotice)) {
            contribNotice = getSharedPreferences(ContributeActivity.CONTRIB_PREFS, MODE_PRIVATE)
                    .getString("key_detail_" + ContributeActivity.CONTRIB_ID_NRI, null);
        }
        if (!TextUtils.isEmpty(contribNotice) && tvNotice != null) {
            tvNotice.setText(contribNotice);
        }
        setGreeting("");
        loadGreeting();
        etName = findViewById(R.id.et_name);
        etNativePlace = findViewById(R.id.et_native_place);
        etMobile = findViewById(R.id.et_mobile);
        etDetails = findViewById(R.id.et_details);
        svForm = findViewById(R.id.sv_form);
        spCountry = findViewById(R.id.sp_country);
        btSubmit = findViewById(R.id.bt_submit);
        tvNoDataEntries = findViewById(R.id.tv_no_data_entries);
        llDataEntries = findViewById(R.id.ll_data_entries);
        ccpCountryCode = findViewById(R.id.ccp_country_code);
        configurePhoneCountryCodePicker();
        bindCountrySpinner(new ArrayList<>());
        bindSpinnerKeyboardDismissal();
        configureKeyboardScrolling();
        btSubmit.setOnClickListener(v -> {
            if (isSubmitting) return;
            int errorMessage = validateForm();
            if (errorMessage == 0) submitForm();
            else UiUtil.showDialog(this, getString(errorMessage), true);
        });
        loadCountries();
        hasCachedEntries = showCachedEntries();
        loadEntries();
    }

    /** Loads the greeting from the current user-profile API instead of saved display data. */
    private void loadGreeting() {
        if (!RegistrationApiClient.isConfigured()) return;
        RegistrationApiClient.getApi(this).getCurrentUserProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (isFinishing() || isDestroyed() || !response.isSuccessful()
                        || response.body() == null) return;
                Profile profile = response.body();
                setGreeting(AppUtil.getFullName(profile.getFirstName(), profile.getLastName()));
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable throwable) {
                // Leave the generic greeting in place when the profile service is unavailable.
            }
        });
    }

    private void setGreeting(String name) {
        String displayName = TextUtils.isEmpty(name) ? getString(R.string.contributor) : name;
        tvGreeting.setText(getString(R.string.contribute_greeting, displayName));
    }

    private void loadEntries() {
        int userId = getCurrentUserId();
        if (!hasCachedEntries) {
            tvNoDataEntries.setText(R.string.loading_data_entries);
            tvNoDataEntries.setVisibility(View.VISIBLE);
        }
        if (userId == 0 || !RegistrationApiClient.isConfigured()) {
            if (!hasCachedEntries) displayEntries(new ArrayList<>());
            return;
        }
        RegistrationApiClient.getApi(this).getDataNris(userId, 1, 10).enqueue(
                new Callback<DataEntryListResponse>() {
                    @Override
                    public void onResponse(Call<DataEntryListResponse> call,
                                           Response<DataEntryListResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<NriListItem> entries = ContributionHistoryHelper
                                    .extractEntries(response.body(), NriListItem.class);
                            ContributionHistoryHelper.writeCache(NriDataEntryActivity.this,
                                    HISTORY_CACHE_PREFS, getCacheKey(getCurrentUserId()), entries);
                            displayEntries(entries);
                        } else if (!hasCachedEntries) {
                            displayEntries(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onFailure(Call<DataEntryListResponse> call, Throwable throwable) {
                        if (!isFinishing() && !isDestroyed() && !hasCachedEntries) {
                            displayEntries(new ArrayList<>());
                        }
                    }
                });
    }

    private boolean showCachedEntries() {
        int userId = getCurrentUserId();
        if (userId == 0) return false;
        ArrayList<NriListItem> entries = ContributionHistoryHelper.readCache(this,
                HISTORY_CACHE_PREFS, getCacheKey(userId),
                new TypeToken<ArrayList<NriListItem>>() { }.getType());
        if (entries.isEmpty()) return false;
        displayEntries(entries);
        return true;
    }

    private void displayEntries(List<NriListItem> entries) {
        ContributionHistoryHelper.displayEntries(this, llDataEntries, tvNoDataEntries, entries,
                (position, item) -> ContributionHistoryHelper.formatEntry(position,
                        item.getName(), item.getNativePlace(), item.getMobileNumber()));
    }

    private String getCacheKey(int userId) {
        return "nri_entries_" + userId;
    }

    /** Dismisses the keyboard when a dropdown is opened, like the registration screen. */
    private void bindSpinnerKeyboardDismissal() {
        View.OnTouchListener listener = (view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                dismissKeyboard();
            } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                view.performClick();
            }
            return false;
        };
        spCountry.setOnTouchListener(listener);
    }

    /**
     * Edge-to-edge (targetSdk 35+) stops the window from resizing for the keyboard, so the
     * form gets bottom padding matching the keyboard height and the submit button is
     * scrolled above it. Same approach as DataEntryActivity.
     */
    private void configureKeyboardScrolling() {
        ViewCompat.setOnApplyWindowInsetsListener(svForm, (view, insets) -> {
            int imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            int navigationBottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            keyboardOverlayHeight = Math.max(0, imeBottom - navigationBottom);
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(),
                    view.getPaddingRight(), keyboardOverlayHeight);
            if (keyboardOverlayHeight > 0 && getCurrentFocus() instanceof TextInputEditText) {
                view.postDelayed(this::showSubmitAboveKeyboard, 350);
            }
            return insets;
        });
    }

    private void showSubmitAboveKeyboard() {
        if (keyboardOverlayHeight == 0) return;
        int visibleBottom = svForm.getHeight() - keyboardOverlayHeight;
        int targetScrollY = btSubmit.getBottom() - visibleBottom
                + getResources().getDimensionPixelSize(R.dimen.margin_16dp);
        svForm.smoothScrollTo(0, Math.max(0, targetScrollY));
    }

    private void dismissKeyboard() {
        View focusedView = getCurrentFocus();
        UiUtil.hideKeyBoard(this, focusedView != null ? focusedView : btSubmit);
        if (focusedView != null) {
            focusedView.clearFocus();
        }
    }

    private void loadCountries() {
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        ArrayList<RegistrationMasterOption> cached = RegistrationMasterCache.getCountries(this);
        boolean hasCached = !cached.isEmpty();
        if (hasCached) bindCountrySpinner(cached);
        if (hasCached && RegistrationMasterCache.areCountriesFresh(this)) return;
        RegistrationApiClient.getApi(this).getCountries().enqueue(
                new Callback<RegistrationMasterResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationMasterResponse> call,
                                           Response<RegistrationMasterResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getReturnData() != null
                                && !response.body().getReturnData().isEmpty()) {
                            RegistrationMasterCache.putCountries(NriDataEntryActivity.this,
                                    response.body().getReturnData());
                            bindCountrySpinner(response.body().getReturnData());
                        } else if (!hasCached) {
                            showRequestError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationMasterResponse> call, Throwable throwable) {
                        if (!isFinishing() && !isDestroyed() && !hasCached) {
                            showRequestError(throwable);
                        }
                    }
                });
    }

    private void configurePhoneCountryCodePicker() {
        if (ccpCountryCode == null) return;
        ccpCountryCode.setDefaultCountryUsingNameCode("AE");
        ccpCountryCode.resetToDefaultCountry();
        ccpCountryCode.setOnCountryChangeListener(() -> {
            if (syncingCountryPicker) return;
            String selectedName = ccpCountryCode.getSelectedCountryName();
            String selectedNameCode = ccpCountryCode.getSelectedCountryNameCode();
            String selectedCode = ccpCountryCode.getSelectedCountryCode();

            // 1. First attempt: match by exact country name or name code
            for (int index = 1; index < countryOptions.size(); index++) {
                RegistrationMasterOption country = countryOptions.get(index);
                if (country.getName() != null && (
                        country.getName().equalsIgnoreCase(selectedName) ||
                        country.getName().equalsIgnoreCase(selectedNameCode))) {
                    applyCountrySelection(index, country);
                    return;
                }
            }

            // 2. Second attempt: match by partial country name
            for (int index = 1; index < countryOptions.size(); index++) {
                RegistrationMasterOption country = countryOptions.get(index);
                if (country.getName() != null && (
                        country.getName().toLowerCase(Locale.ROOT).contains(selectedName.toLowerCase(Locale.ROOT)) ||
                        selectedName.toLowerCase(Locale.ROOT).contains(country.getName().toLowerCase(Locale.ROOT)))) {
                    applyCountrySelection(index, country);
                    return;
                }
            }

            // 3. Fallback: match by dial code
            for (int index = 1; index < countryOptions.size(); index++) {
                RegistrationMasterOption country = countryOptions.get(index);
                if (selectedCode.equals(normalizeCountryCode(country.getCountryCode()))) {
                    applyCountrySelection(index, country);
                    return;
                }
            }
        });
    }

    private void applyCountrySelection(int index, RegistrationMasterOption country) {
        try {
            syncingCountryPicker = true;
            spCountry.setSelection(index);
            countryId = country.getId();
            countryCode = country.getCountryCode();
        } finally {
            syncingCountryPicker = false;
        }
    }

    private void syncPhoneCountryCodePicker(String countryName, String value) {
        if (ccpCountryCode == null) return;
        try {
            syncingCountryPicker = true;
            boolean set = false;
            if (!TextUtils.isEmpty(countryName)) {
                String iso = getIsoCodeForCountryName(countryName);
                if (!TextUtils.isEmpty(iso)) {
                    ccpCountryCode.setCountryForNameCode(iso);
                    set = true;
                }
            }
            if (!set && !TextUtils.isEmpty(value)) {
                String code = normalizeCountryCode(value);
                ccpCountryCode.setCountryForPhoneCode(Integer.parseInt(code));
            }
        } catch (Exception ignored) {
        } finally {
            syncingCountryPicker = false;
        }
    }

    private String getIsoCodeForCountryName(String countryName) {
        if (TextUtils.isEmpty(countryName)) return null;
        if ("United States".equalsIgnoreCase(countryName) || "USA".equalsIgnoreCase(countryName)) return "US";
        if ("United Kingdom".equalsIgnoreCase(countryName) || "UK".equalsIgnoreCase(countryName)) return "GB";
        if ("United Arab Emirates".equalsIgnoreCase(countryName) || "UAE".equalsIgnoreCase(countryName)) return "AE";
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale.Builder().setRegion(iso).build();
            if (l.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName)
                    || l.getDisplayCountry().equalsIgnoreCase(countryName)) {
                return iso;
            }
        }
        return null;
    }

    private String normalizeCountryCode(String value) {
        if (TextUtils.isEmpty(value)) return "971";
        return value.startsWith("+") ? value.substring(1) : value;
    }

    private void bindCountrySpinner(List<RegistrationMasterOption> values) {
        ArrayList<RegistrationMasterOption> options = new ArrayList<>();
        options.add(RegistrationMasterOption.placeholder(getString(R.string.select_a_country)));
        options.addAll(values);
        countryOptions.clear();
        countryOptions.addAll(options);
        Integer previousCountryId = countryId;
        spCountry.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        spCountry.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (syncingCountryPicker) return;
                RegistrationMasterOption country = position == 0 ? null
                        : ((RegistrationMasterOption) parent.getItemAtPosition(position));
                countryId = country == null ? null : country.getId();
                countryCode = country == null ? null : country.getCountryCode();
                if (country != null) {
                    syncPhoneCountryCodePicker(country.getName(), countryCode);
                }
            }
        });
        if (previousCountryId != null) {
            for (int i = 1; i < options.size(); i++) {
                if (options.get(i).getId() == previousCountryId) {
                    spCountry.setSelection(i);
                    break;
                }
            }
        }
    }

    private int validateForm() {
        if (etName.getText().toString().trim().isEmpty()) return R.string.enter_name;
        if (countryId == null) return R.string.select_a_country;
        String mobile = etMobile.getText().toString().trim().replaceAll("[^0-9]", "");
        if (mobile.length() != 10) {
            return R.string.enter_mobile;
        }
        return 0;
    }

    private void submitForm() {
        isSubmitting = true;
        btSubmit.setEnabled(false);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        String cleanMobile = etMobile.getText().toString().trim().replaceAll("[^0-9]", "");
        if (cleanMobile.length() > 10 && cleanMobile.startsWith("0")) {
            cleanMobile = cleanMobile.substring(1);
        }
        DataNriRequest request = new DataNriRequest(getCurrentUserId(),
                etName.getText().toString().trim(), countryId,
                etNativePlace.getText().toString().trim(),
                cleanMobile,
                etDetails != null && etDetails.getText() != null ? etDetails.getText().toString().trim() : "");
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.cancelProgressDialog();
            resetSubmittingState();
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        RegistrationApiClient.getApi(this).addDataNri(request).enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                resetSubmittingState();
                if (response.isSuccessful() && ContributionResponseUtil.isSaved(response.body())) {
                    UiUtil.showToast(NriDataEntryActivity.this, getString(R.string.data_submitted));
                    clearSubmittedFields();
                    loadEntries();
                } else {
                    String message = response.isSuccessful()
                            ? ContributionResponseUtil.getResponseMessage(response.body(),
                                    getString(R.string.err_occurred))
                            : ContributionResponseUtil.getErrorMessage(response,
                                    getString(R.string.err_occurred));
                    UiUtil.showToast(NriDataEntryActivity.this, message);
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable throwable) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                resetSubmittingState();
                UiUtil.showToast(NriDataEntryActivity.this, getString(R.string.connectionError));
            }
        });
    }

    private void clearSubmittedFields() {
        etName.setText("");
        etNativePlace.setText("");
        etMobile.setText("");
        if (etDetails != null) etDetails.setText("");
        if (spCountry != null && spCountry.getAdapter() != null && spCountry.getAdapter().getCount() > 0) {
            spCountry.setSelection(0);
        }
        countryId = null;
        if (ccpCountryCode != null) {
            ccpCountryCode.resetToDefaultCountry();
        }
        etName.requestFocus();
    }

    private void resetSubmittingState() {
        isSubmitting = false;
        btSubmit.setEnabled(true);
    }

    private int getCurrentUserId() {
        try { return Integer.parseInt(AppPrefs.getPrefsUserId(this)); }
        catch (NumberFormatException ignored) { return 0; }
    }

    private void showRequestError(Response<?> response) {
        UiUtil.showToast(this, ContributionResponseUtil.getErrorMessage(response,
                getString(R.string.err_occurred)));
    }

    private void showRequestError(Throwable throwable) {
        UiUtil.showToast(this, getString(R.string.connectionError));
    }

    private abstract static class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) { }
    }
}
