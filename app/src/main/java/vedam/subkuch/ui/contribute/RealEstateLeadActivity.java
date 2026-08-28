package vedam.subkuch.ui.contribute;

import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.DataEstateRequest;
import vedam.subkuch.network.models.EstateListItem;
import vedam.subkuch.network.models.EstateTypeListResponse;
import vedam.subkuch.network.models.EstateTypeOption;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

/** Form for the DataEstate/AddDataEstate contribution API. */
public class RealEstateLeadActivity extends BaseActivity {

    private static final String HISTORY_CACHE_PREFS = "estate_data_entry_cache";

    private TextInputEditText etName;
    private TextInputEditText etMobile;
    private TextInputEditText etLocation;
    private TextInputEditText etDetails;
    private ScrollView svForm;
    private Spinner spState;
    private Spinner spDistrict;
    private Spinner spEstateType;
    private Button btSubmit;
    private TextView tvNoDataEntries;
    private LinearLayout llDataEntries;
    private boolean hasCachedEntries;
    private String stateId;
    private String districtId;
    private Integer estateTypeId;
    private boolean isSubmitting;
    private int keyboardOverlayHeight;
    private final ArrayList<RegistrationMasterOption> districts = new ArrayList<>();
    private boolean districtAutoRefetchDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_lead);
        setTitle(R.string.submit_real_estate_lead);
        setToolbarBackButton();
        String userName = AppPrefs.getPrefsUserName(this).trim();
        ((TextView) findViewById(R.id.tv_name)).setText(getString(R.string.contribute_greeting,
                userName.isEmpty() ? getString(R.string.contributor) : userName));
        etName = findViewById(R.id.et_name);
        etMobile = findViewById(R.id.et_mobile);
        etLocation = findViewById(R.id.et_location);
        etDetails = findViewById(R.id.et_details);
        svForm = findViewById(R.id.sv_form);
        spState = findViewById(R.id.sp_state);
        spDistrict = findViewById(R.id.sp_district);
        spEstateType = findViewById(R.id.sp_estate_type);
        btSubmit = findViewById(R.id.bt_submit);
        tvNoDataEntries = findViewById(R.id.tv_no_data_entries);
        llDataEntries = findViewById(R.id.ll_data_entries);
        bindStateSpinner(new ArrayList<>());
        updateDistrictSpinner();
        bindEstateTypeSpinner(new ArrayList<>());
        bindSpinnerKeyboardDismissal();
        configureKeyboardScrolling();
        btSubmit.setOnClickListener(v -> {
            if (isSubmitting) return;
            int errorMessage = validateForm();
            if (errorMessage == 0) submitForm();
            else UiUtil.showDialog(this, getString(errorMessage), true);
        });
        loadMasterData();
        hasCachedEntries = showCachedEntries();
        loadEntries();
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
        RegistrationApiClient.getApi(this).getDataEstates(userId, null, null, 1, 10).enqueue(
                new Callback<DataEntryListResponse>() {
                    @Override
                    public void onResponse(Call<DataEntryListResponse> call,
                                           Response<DataEntryListResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<EstateListItem> entries = ContributionHistoryHelper
                                    .extractEntries(response.body(), EstateListItem.class);
                            ContributionHistoryHelper.writeCache(RealEstateLeadActivity.this,
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
        ArrayList<EstateListItem> entries = ContributionHistoryHelper.readCache(this,
                HISTORY_CACHE_PREFS, getCacheKey(userId),
                new TypeToken<ArrayList<EstateListItem>>() { }.getType());
        if (entries.isEmpty()) return false;
        displayEntries(entries);
        return true;
    }

    private void displayEntries(List<EstateListItem> entries) {
        ContributionHistoryHelper.displayEntries(this, llDataEntries, tvNoDataEntries, entries,
                (position, item) -> ContributionHistoryHelper.formatEntry(position,
                        item.getName(), item.getLocation(), item.getMobile()));
    }

    private String getCacheKey(int userId) {
        return "estate_entries_" + userId;
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
        spState.setOnTouchListener(listener);
        spDistrict.setOnTouchListener(listener);
        spEstateType.setOnTouchListener(listener);
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

    private void loadMasterData() {
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        ArrayList<RegistrationMasterOption> cachedStates =
                RegistrationMasterCache.getStates(this);
        boolean hasCachedStates = !cachedStates.isEmpty();
        if (hasCachedStates) bindStateSpinner(cachedStates);
        if (!hasCachedStates || !RegistrationMasterCache.areStatesFresh(this)) {
            RegistrationApiClient.getApi(this).getStates().enqueue(
                    new Callback<RegistrationMasterResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationMasterResponse> call,
                                               Response<RegistrationMasterResponse> response) {
                            if (isFinishing() || isDestroyed()) return;
                            if (response.isSuccessful() && response.body() != null
                                    && response.body().getReturnData() != null
                                    && !response.body().getReturnData().isEmpty()) {
                                RegistrationMasterCache.putStates(RealEstateLeadActivity.this,
                                        response.body().getReturnData());
                                bindStateSpinner(response.body().getReturnData());
                            } else if (!hasCachedStates) {
                                showRequestError(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationMasterResponse> call, Throwable throwable) {
                            if (!isFinishing() && !isDestroyed() && !hasCachedStates) {
                                showRequestError(throwable);
                            }
                        }
                    });
        }

        ArrayList<RegistrationMasterOption> cachedDistricts =
                RegistrationMasterCache.getDistricts(this);
        boolean hasCachedDistricts = !cachedDistricts.isEmpty();
        if (hasCachedDistricts) showDistricts(cachedDistricts);
        if (!hasCachedDistricts || !RegistrationMasterCache.areDistrictsFresh(this)) {
            fetchDistricts(hasCachedDistricts);
        }

        RegistrationApiClient.getApi(this).getEstateTypes().enqueue(
                new Callback<EstateTypeListResponse>() {
                    @Override
                    public void onResponse(Call<EstateTypeListResponse> call,
                                           Response<EstateTypeListResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getReturnData() != null
                                && !response.body().getReturnData().isEmpty()) {
                            bindEstateTypeSpinner(response.body().getReturnData());
                        } else {
                            showRequestError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<EstateTypeListResponse> call, Throwable throwable) {
                        if (!isFinishing() && !isDestroyed()) {
                            showRequestError(throwable);
                        }
                    }
                });
    }

    private void bindStateSpinner(List<RegistrationMasterOption> values) {
        spState.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                withPlaceholder(values, getString(R.string.select_a_state))));
        spState.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
                districtId = null;
                updateDistrictSpinner();
            }
        });
    }

    private void updateDistrictSpinner() {
        ArrayList<RegistrationMasterOption> filtered = filterDistricts();
        if (filtered.isEmpty() && stateId != null && !districts.isEmpty() && !districtAutoRefetchDone) {
            // A valid state produced no districts: the cached master list is likely
            // outdated. Re-fetch once per screen instead of leaving an empty dropdown.
            districtAutoRefetchDone = true;
            fetchDistricts(true);
        }
        spDistrict.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                withPlaceholder(filtered, getString(R.string.select_a_district))));
        spDistrict.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
            }
        });
    }

    private void fetchDistricts(boolean hasCachedDistricts) {
        RegistrationApiClient.getApi(this).getDistricts().enqueue(
                new Callback<RegistrationMasterResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationMasterResponse> call,
                                           Response<RegistrationMasterResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null
                                && response.body().getReturnData() != null
                                && !response.body().getReturnData().isEmpty()) {
                            RegistrationMasterCache.putDistricts(RealEstateLeadActivity.this,
                                    response.body().getReturnData());
                            showDistricts(response.body().getReturnData());
                        } else if (!hasCachedDistricts) {
                            showRequestError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationMasterResponse> call, Throwable throwable) {
                        if (!isFinishing() && !isDestroyed() && !hasCachedDistricts) {
                            showRequestError(throwable);
                        }
                    }
                });
    }

    private void showDistricts(List<RegistrationMasterOption> values) {
        districts.clear();
        districts.addAll(values);
        updateDistrictSpinner();
    }

    private ArrayList<RegistrationMasterOption> filterDistricts() {
        ArrayList<RegistrationMasterOption> filtered = new ArrayList<>();
        for (RegistrationMasterOption district : districts) {
            if (district.getStateId() == null || stateId != null
                    && stateId.equals(String.valueOf(district.getStateId()))) filtered.add(district);
        }
        return filtered;
    }

    private void bindEstateTypeSpinner(List<EstateTypeOption> values) {
        ArrayList<EstateTypeOption> options = new ArrayList<>();
        options.add(EstateTypeOption.placeholder(getString(R.string.select_estate_type)));
        options.addAll(values);
        spEstateType.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        spEstateType.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estateTypeId = position == 0 ? null
                        : ((EstateTypeOption) parent.getItemAtPosition(position)).getId();
            }
        });
    }

    private ArrayList<RegistrationMasterOption> withPlaceholder(List<RegistrationMasterOption> options,
                                                                String label) {
        ArrayList<RegistrationMasterOption> values = new ArrayList<>();
        values.add(RegistrationMasterOption.placeholder(label));
        values.addAll(options);
        return values;
    }

    private int validateForm() {
        if (stateId == null) return R.string.select_a_state;
        if (districtId == null) return R.string.select_a_district;
        if (estateTypeId == null) return R.string.select_estate_type;
        if (etName.getText().toString().trim().isEmpty()) return R.string.enter_name;
        if (etMobile.getText().toString().trim().isEmpty()) return R.string.enter_mobile;
        if (etLocation.getText().toString().trim().isEmpty()) return R.string.enter_location;
        return 0;
    }

    private void submitForm() {
        isSubmitting = true;
        btSubmit.setEnabled(false);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataEstateRequest request = new DataEstateRequest(getCurrentUserId(),
                Integer.parseInt(stateId), Integer.parseInt(districtId),
                etLocation.getText().toString().trim(),
                etName.getText().toString().trim(),
                etMobile.getText().toString().trim(),
                estateTypeId,
                etDetails.getText().toString().trim());
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.cancelProgressDialog();
            resetSubmittingState();
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        RegistrationApiClient.getApi(this).addDataEstate(request).enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                resetSubmittingState();
                if (response.isSuccessful() && ContributionResponseUtil.isSaved(response.body())) {
                    UiUtil.showToast(RealEstateLeadActivity.this, getString(R.string.data_submitted));
                    clearSubmittedFields();
                    loadEntries();
                } else {
                    String message = response.isSuccessful()
                            ? ContributionResponseUtil.getResponseMessage(response.body(),
                                    getString(R.string.err_occurred))
                            : ContributionResponseUtil.getErrorMessage(response,
                                    getString(R.string.err_occurred));
                    UiUtil.showToast(RealEstateLeadActivity.this, message);
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable throwable) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                resetSubmittingState();
                UiUtil.showToast(RealEstateLeadActivity.this, getString(R.string.connectionError));
            }
        });
    }

    /** Clears only the submitted values so the selected spinners remain ready for the next entry. */
    private void clearSubmittedFields() {
        etName.setText("");
        etMobile.setText("");
        etLocation.setText("");
        etDetails.setText("");
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
