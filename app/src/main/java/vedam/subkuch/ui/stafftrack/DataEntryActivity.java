package vedam.subkuch.ui.stafftrack;

import android.os.Bundle;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityDataEntryBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.DataEntryRequest;
import vedam.subkuch.network.models.DataEntryListItem;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.RegistrationMasterOption;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

/** Form for the DataEntry/AddDataEntry contribution API. */
public class DataEntryActivity extends BaseActivity {

    private static final String DATA_ENTRY_CACHE_PREFS = "data_entry_cache";
    private ActivityDataEntryBinding binding;
    private String stateId;
    private String districtId;
    private boolean isSubmitting;
    private boolean hasCachedDataEntries;
    private int imeBottomInset;
    private int navigationBottomInset;
    private final ArrayList<RegistrationMasterOption> districts = new ArrayList<>();
    private boolean districtAutoRefetchDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_entry);
        setTitle(R.string.contribute);
        setToolbarBackButton();
        String userName = AppPrefs.getPrefsUserName(this).trim();
        binding.tvName.setText(getString(R.string.contribute_greeting,
                userName.isEmpty() ? getString(R.string.contributor) : userName));
        bindStateSpinner(new ArrayList<>());
        updateDistrictSpinner();
        configureKeyboardScrolling();
        binding.btSubmit.setOnClickListener(v -> {
            if (isSubmitting) return;
            int errorMessage = validateDataEntry();
            if (errorMessage == 0) submitDataEntry();
            else UiUtil.showDialog(this, getString(errorMessage), true);
        });
        loadMasterData();
        hasCachedDataEntries = showCachedDataEntries();
        loadDataEntries();
    }

    private void configureKeyboardScrolling() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.svDataEntry, (view, insets) -> {
            imeBottomInset = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            navigationBottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            // Edge-to-edge (targetSdk 35+) plus adjustNothing means the window never resizes
            // for the keyboard; pad the form so the submit button can scroll above it.
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(),
                    Math.max(0, imeBottomInset - navigationBottomInset));
            if (imeBottomInset > 0 && (binding.etCompanyName.hasFocus()
                    || binding.etMobile1.hasFocus() || binding.etMobile2.hasFocus())) {
                showSubmitAboveKeyboard();
            }
            return insets;
        });
        View.OnFocusChangeListener textFieldFocusListener = (view, hasFocus) -> {
            if (!hasFocus) return;
            ViewCompat.requestApplyInsets(binding.svDataEntry);
            binding.svDataEntry.postDelayed(this::showSubmitAboveKeyboard, 350);
        };
        binding.etCompanyName.setOnFocusChangeListener(textFieldFocusListener);
        binding.etLocation.setOnFocusChangeListener(textFieldFocusListener);
        binding.etMobile1.setOnFocusChangeListener(textFieldFocusListener);
        binding.etMobile2.setOnFocusChangeListener(textFieldFocusListener);
    }

    private void showSubmitAboveKeyboard() {
        binding.svDataEntry.post(() -> {
            int keyboardOverlayHeight = Math.max(0, imeBottomInset - navigationBottomInset);
            if (keyboardOverlayHeight == 0) return;
            int visibleBottom = binding.svDataEntry.getHeight() - keyboardOverlayHeight;
            int targetScrollY = binding.btSubmit.getBottom() - visibleBottom
                    + getResources().getDimensionPixelSize(R.dimen.margin_16dp)
                    + binding.btSubmit.getHeight() * 2;
            binding.svDataEntry.smoothScrollTo(0, Math.max(0, targetScrollY));
        });
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
                            if (response.isSuccessful() && response.body() != null) {
                                setStates(response.body());
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
            RegistrationApiClient.getApi(this).getDistricts().enqueue(
                    new Callback<RegistrationMasterResponse>() {
                        @Override
                        public void onResponse(Call<RegistrationMasterResponse> call,
                                               Response<RegistrationMasterResponse> response) {
                            if (isFinishing() || isDestroyed()) return;
                            if (response.isSuccessful() && response.body() != null) {
                                setDistricts(response.body());
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
    }

    private void loadDataEntries() {
        int userId = getCurrentUserId();
        if (!hasCachedDataEntries) {
            binding.tvNoDataEntries.setText(R.string.loading_data_entries);
            binding.tvNoDataEntries.setVisibility(View.VISIBLE);
        }
        if (userId == 0 || !RegistrationApiClient.isConfigured()) {
            if (!hasCachedDataEntries) displayDataEntries(new ArrayList<>());
            return;
        }
        RegistrationApiClient.getApi(this).getUniqueDataEntries(userId, 1, 10).enqueue(
                new Callback<DataEntryListResponse>() {
                    @Override
                    public void onResponse(Call<DataEntryListResponse> call,
                                           Response<DataEntryListResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            showDataEntries(response.body());
                        } else if (!hasCachedDataEntries) {
                            displayDataEntries(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onFailure(Call<DataEntryListResponse> call, Throwable throwable) {
                        if (!isFinishing() && !isDestroyed() && !hasCachedDataEntries) {
                            displayDataEntries(new ArrayList<>());
                        }
                    }
                });
    }

    private void showDataEntries(DataEntryListResponse response) {
        List<DataEntryListItem> entries = getDataEntryItems(response);
        saveDataEntries(entries);
        displayDataEntries(entries);
    }

    private boolean showCachedDataEntries() {
        int userId = getCurrentUserId();
        if (userId == 0) return false;
        String cachedJson = getSharedPreferences(DATA_ENTRY_CACHE_PREFS, MODE_PRIVATE)
                .getString(getCacheKey(userId), null);
        if (cachedJson == null) return false;
        try {
            java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<
                    ArrayList<DataEntryListItem>>() { }.getType();
            ArrayList<DataEntryListItem> entries = new Gson().fromJson(cachedJson, listType);
            if (entries == null) return false;
            displayDataEntries(entries);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private void saveDataEntries(List<DataEntryListItem> entries) {
        int userId = getCurrentUserId();
        if (userId == 0) return;
        getSharedPreferences(DATA_ENTRY_CACHE_PREFS, MODE_PRIVATE).edit()
                .putString(getCacheKey(userId), new Gson().toJson(entries))
                .apply();
    }

    private String getCacheKey(int userId) {
        return "unique_data_entries_" + userId;
    }

    private void displayDataEntries(List<DataEntryListItem> entries) {
        binding.llDataEntries.removeAllViews();
        if (entries == null || entries.isEmpty()) {
            binding.tvNoDataEntries.setText(R.string.no_data_entered);
            binding.tvNoDataEntries.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvNoDataEntries.setVisibility(View.GONE);
        for (int index = 0; index < entries.size(); index++) {
            TextView entryView = new TextView(this);
            entryView.setText(formatDataEntry(index + 1, entries.get(index)));
            entryView.setTextColor(ContextCompat.getColor(this, R.color.form_text_primary));
            entryView.setTextSize(14);
            entryView.setLineSpacing(0, 1.15f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (index > 0) params.topMargin = getResources().getDimensionPixelSize(R.dimen.margin_8dp);
            entryView.setLayoutParams(params);
            binding.llDataEntries.addView(entryView);
        }
    }

    private List<DataEntryListItem> getDataEntryItems(DataEntryListResponse response) {
        ArrayList<DataEntryListItem> entries = new ArrayList<>();
        if (response == null || response.getReturnData() == null) return entries;
        JsonElement returnData = response.getReturnData();
        JsonArray array = getDataEntryArray(returnData);
        if (array == null) return entries;
        Gson gson = new Gson();
        for (JsonElement item : array) {
            if (item != null && item.isJsonObject()) {
                entries.add(gson.fromJson(item, DataEntryListItem.class));
            }
        }
        return entries;
    }

    private JsonArray getDataEntryArray(JsonElement returnData) {
        if (returnData.isJsonArray()) return returnData.getAsJsonArray();
        if (!returnData.isJsonObject()) return null;
        JsonObject dataObject = returnData.getAsJsonObject();
        String[] listKeys = {"Data", "data", "Items", "items", "Records", "records"};
        for (String key : listKeys) {
            JsonElement value = dataObject.get(key);
            if (value != null && value.isJsonArray()) return value.getAsJsonArray();
        }
        for (java.util.Map.Entry<String, JsonElement> entry : dataObject.entrySet()) {
            if (entry.getValue().isJsonArray()) return entry.getValue().getAsJsonArray();
        }
        return null;
    }

    private String formatDataEntry(int position, DataEntryListItem entry) {
        StringBuilder value = new StringBuilder(position + ") ");
        appendEntryValue(value, entry.getCompanyName());
        appendEntryValue(value, entry.getLocation());
        appendEntryValue(value, entry.getMobile1());
        appendEntryValue(value, entry.getMobile2());
        return value.toString();
    }

    private void appendEntryValue(StringBuilder builder, String value) {
        if (value == null || value.trim().isEmpty()) return;
        if (builder.length() > 3) builder.append(", ");
        builder.append(value.trim());
    }

    private void setStates(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        RegistrationMasterCache.putStates(this, response.getReturnData());
        bindStateSpinner(response.getReturnData());
    }

    private void setDistricts(RegistrationMasterResponse response) {
        if (!hasMasterData(response)) return;
        RegistrationMasterCache.putDistricts(this, response.getReturnData());
        showDistricts(response.getReturnData());
    }

    private void showDistricts(List<RegistrationMasterOption> values) {
        districts.clear();
        districts.addAll(values);
        updateDistrictSpinner();
    }

    private boolean hasMasterData(RegistrationMasterResponse response) {
        return response != null && response.getReturnData() != null && !response.getReturnData().isEmpty();
    }

    private void bindStateSpinner(List<RegistrationMasterOption> values) {
        ArrayList<RegistrationMasterOption> options = withPlaceholder(values,
                getString(R.string.select_a_state));
        binding.spState.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        binding.spState.setOnItemSelectedListener(new SimpleItemSelectedListener() {
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
            // outdated (e.g. districts added server-side after the cache was written).
            // Re-fetch once per screen instead of leaving an empty dropdown.
            districtAutoRefetchDone = true;
            forceReloadDistricts();
        }
        ArrayList<RegistrationMasterOption> options = withPlaceholder(filtered,
                getString(R.string.select_a_district));
        binding.spDistrict.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options));
        binding.spDistrict.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtId = position == 0 ? null : String.valueOf(
                        ((RegistrationMasterOption) parent.getItemAtPosition(position)).getId());
            }
        });
    }

    private void forceReloadDistricts() {
        RegistrationApiClient.getApi(this).getDistricts().enqueue(
                new Callback<RegistrationMasterResponse>() {
                    @Override
                    public void onResponse(Call<RegistrationMasterResponse> call,
                                           Response<RegistrationMasterResponse> response) {
                        if (isFinishing() || isDestroyed()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            setDistricts(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrationMasterResponse> call, Throwable throwable) {
                        // Keep showing cached data.
                    }
                });
    }

    private ArrayList<RegistrationMasterOption> filterDistricts() {
        ArrayList<RegistrationMasterOption> filtered = new ArrayList<>();
        for (RegistrationMasterOption district : districts) {
            if (district.getStateId() == null || stateId != null
                    && stateId.equals(String.valueOf(district.getStateId()))) filtered.add(district);
        }
        return filtered;
    }

    private ArrayList<RegistrationMasterOption> withPlaceholder(List<RegistrationMasterOption> options,
                                                                  String label) {
        ArrayList<RegistrationMasterOption> values = new ArrayList<>();
        values.add(RegistrationMasterOption.placeholder(label));
        values.addAll(options);
        return values;
    }

    private void submitDataEntry() {
        isSubmitting = true;
        binding.btSubmit.setEnabled(false);
        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataEntryRequest request = new DataEntryRequest(getCurrentUserId(), Integer.parseInt(districtId),
                binding.etCompanyName.getText().toString().trim(),
                binding.etLocation.getText().toString().trim(),
                binding.etMobile1.getText().toString().trim(),
                binding.etMobile2.getText().toString().trim());
        if (!RegistrationApiClient.isConfigured()) {
            UiUtil.cancelProgressDialog();
            resetSubmittingState();
            UiUtil.showToast(this, getString(R.string.registration_api_not_configured));
            return;
        }
        RegistrationApiClient.getApi(this).addDataEntry(request).enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                if (response.isSuccessful() && response.body() != null) {
                    onDataEntrySaved(response.body());
                } else {
                    resetSubmittingState();
                    String message = getDataEntryErrorMessage(response);
                    if (isDataAlreadyExists(message)) showDataAlreadyExistsDialog();
                    else UiUtil.showToast(DataEntryActivity.this, message);
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable throwable) {
                if (isFinishing() || isDestroyed()) return;
                UiUtil.cancelProgressDialog();
                resetSubmittingState();
                UiUtil.showToast(DataEntryActivity.this, getDataEntryErrorMessage(throwable));
            }
        });
    }

    private int getCurrentUserId() {
        try { return Integer.parseInt(AppPrefs.getPrefsUserId(this)); }
        catch (NumberFormatException ignored) { return 0; }
    }

    private void onDataEntrySaved(AddResponse response) {
        if (isDataEntrySaved(response)) {
            UiUtil.showToast(this, getBoldSubmissionMessage());
            clearSubmittedFields();
            resetSubmittingState();
            loadDataEntries();
        } else {
            resetSubmittingState();
            String message = getResponseMessage(response);
            if (isDataAlreadyExists(message)) showDataAlreadyExistsDialog();
            else UiUtil.showToast(this, message);
        }
    }

    /** Clears only the submitted values so the selected state and district remain ready for the next entry. */
    private void clearSubmittedFields() {
        binding.etCompanyName.setText("");
        binding.etLocation.setText("");
        binding.etMobile1.setText("");
        binding.etMobile2.setText("");
        binding.etCompanyName.requestFocus();
    }

    private boolean isDataAlreadyExists(String message) {
        return message != null
                && message.trim().toLowerCase(Locale.ROOT).contains("already exist");
    }

    private void showDataAlreadyExistsDialog() {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            focusedView.clearFocus();
        }
        clearSubmittedFields();

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_duplicate_data, null);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        MaterialButton actionButton = dialogView.findViewById(R.id.bt_duplicate_data_ok);
        actionButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void resetSubmittingState() {
        isSubmitting = false;
        binding.btSubmit.setEnabled(true);
    }

    private SpannableString getBoldSubmissionMessage() {
        SpannableString message = new SpannableString(getString(R.string.data_submitted));
        message.setSpan(new StyleSpan(Typeface.BOLD), 0, message.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return message;
    }

    private boolean isDataEntrySaved(AddResponse response) {
        if (response == null) return false;
        if (response.isSuccess() || response.isStatus()
                || response.getReturnCode() == 1 || response.getReturnCode() == 200) return true;
        return isPositiveSaveMessage(response.getReturnMessage())
                || isPositiveSaveMessage(response.getMessage());
    }

    private boolean isPositiveSaveMessage(String message) {
        if (message == null) return false;
        String normalized = message.trim().toLowerCase(Locale.ROOT);
        return Constants.SUCCESS.equalsIgnoreCase(normalized)
                || normalized.contains("successfully")
                || normalized.contains("data saved")
                || normalized.contains("data added")
                || normalized.contains("data submitted");
    }

    private String getResponseMessage(AddResponse response) {
        if (response != null && response.getReturnMessage() != null
                && !response.getReturnMessage().trim().isEmpty()) return response.getReturnMessage();
        if (response != null && response.getMessage() != null
                && !response.getMessage().trim().isEmpty()) return response.getMessage();
        return getString(R.string.err_occurred);
    }

    private String getDataEntryErrorMessage(Response<?> response) {
        if (response != null && response.errorBody() != null) {
            try {
                String body = response.errorBody().string().trim();
                JsonObject errorBody = JsonParser.parseString(body).getAsJsonObject();
                String[] messageFields = {"ReturnMessage", "message", "Message", "title", "detail"};
                for (String field : messageFields) {
                    JsonElement value = errorBody.get(field);
                    if (value != null && value.isJsonPrimitive()
                            && !value.getAsString().trim().isEmpty()) return value.getAsString();
                }
            } catch (Exception ignored) {
                // Use the standard message when the server response is not JSON.
            }
        }
        return getString(R.string.err_occurred);
    }

    private String getDataEntryErrorMessage(Throwable throwable) {
        return getString(R.string.connectionError);
    }

    private void showRequestError(Response<?> response) {
        UiUtil.showToast(this, getDataEntryErrorMessage(response));
    }

    private void showRequestError(Throwable throwable) {
        UiUtil.showToast(this, getDataEntryErrorMessage(throwable));
    }

    private int validateDataEntry() {
        if (stateId == null) return R.string.select_a_state;
        if (districtId == null) return R.string.select_a_district;
        if (binding.etCompanyName.getText().toString().trim().isEmpty()) return R.string.enter_company_name;
        if (binding.etLocation.getText().toString().trim().isEmpty()) return R.string.enter_location;
        if (binding.etMobile1.getText().toString().trim().isEmpty()) return R.string.enter_mobile;
        return 0;
    }

    private abstract static class SimpleItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override public void onNothingSelected(AdapterView<?> parent) { }
    }
}
