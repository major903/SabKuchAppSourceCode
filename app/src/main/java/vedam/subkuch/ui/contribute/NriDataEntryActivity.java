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
import vedam.subkuch.network.models.DataNriRequest;
import vedam.subkuch.network.models.NriListItem;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
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
    private TextView tvNoDataEntries;
    private LinearLayout llDataEntries;
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
        String userName = AppPrefs.getPrefsUserName(this).trim();
        ((TextView) findViewById(R.id.tv_name)).setText(getString(R.string.contribute_greeting,
                userName.isEmpty() ? getString(R.string.contributor) : userName));
        etName = findViewById(R.id.et_name);
        etNativePlace = findViewById(R.id.et_native_place);
        etMobile = findViewById(R.id.et_mobile);
        etDetails = findViewById(R.id.et_details);
        svForm = findViewById(R.id.sv_form);
        spCountry = findViewById(R.id.sp_country);
        btSubmit = findViewById(R.id.bt_submit);
        tvNoDataEntries = findViewById(R.id.tv_no_data_entries);
        llDataEntries = findViewById(R.id.ll_data_entries);
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

    private void bindCountrySpinner(List<RegistrationMasterOption> values) {
        ArrayList<RegistrationMasterOption> options = new ArrayList<>();
        options.add(RegistrationMasterOption.placeholder(getString(R.string.select_a_country)));
        options.addAll(values);
        spCountry.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        spCountry.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryId = position == 0 ? null
                        : ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId();
            }
        });
    }

    private int validateForm() {
        if (etName.getText().toString().trim().isEmpty()) return R.string.enter_name;
        if (countryId == null) return R.string.select_a_country;
        if (etMobile.getText().toString().trim().isEmpty()) return R.string.enter_mobile;
        return 0;
    }

    private void submitForm() {
        isSubmitting = true;
        btSubmit.setEnabled(false);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataNriRequest request = new DataNriRequest(getCurrentUserId(),
                etName.getText().toString().trim(), countryId,
                etNativePlace.getText().toString().trim(),
                etMobile.getText().toString().trim(),
                etDetails.getText().toString().trim());
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
