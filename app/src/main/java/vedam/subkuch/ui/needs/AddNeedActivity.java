package vedam.subkuch.ui.needs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityAddNeedBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.needs.AddNeedRequest;
import vedam.subkuch.network.models.needs.Provider;
import vedam.subkuch.network.models.needs.ProviderResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AddNeedActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private String categoryId;
    private ActivityAddNeedBinding activityAddNeedBinding;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_need);
        setToolbarBackButton();
        setTitle(R.string.add_a_need);
        bindCallbacks();
        getCategories();
    }

    private void bindCallbacks() {

        activityAddNeedBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0)
                addNeed();
            else
                UiUtil.showDialog(this, getString(errorMessage), true);
        });

        activityAddNeedBinding.btAddLocation.setOnClickListener(view -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(this), Constants.REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });
    }

    private void getCategories() {
        UiUtil.showProgressDialog(this, R.string.please_wait);
        DataFetcher.getProviders(this, onProviderSuccessListener, ProviderResponse.class, onErrorListener);

    }

    private Response.Listener<ProviderResponse> onProviderSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setCategories(response.getReturnData());
        } else
            UiUtil.showToast(this, getString(R.string.no_data));
    };

    private void setCategories(ArrayList<Provider> categories) {

        Provider provider = new Provider();
        provider.setProviders(getString(R.string.select_a_category));
        categories.add(0, provider);

        ArrayAdapter<Provider> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        activityAddNeedBinding.spCategory.setAdapter(adapter);
        activityAddNeedBinding.spCategory.setOnItemSelectedListener(this);
        activityAddNeedBinding.spCategory.setSelection(0);
    }

    private void addNeed() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));

        AddNeedRequest needRequest = new AddNeedRequest();
        needRequest.setUserId(AppPrefs.getPrefsUserId(this));
        needRequest.setNeedProviderId(categoryId);
        needRequest.setWorkLocation(activityAddNeedBinding.etWorkLocation.getText().toString());
        needRequest.setWorkDetails(activityAddNeedBinding.etWorkDetails.getText().toString());
        needRequest.setLatitude(String.valueOf(latLng.latitude));
        needRequest.setLongitude(String.valueOf(latLng.longitude));

        DataFetcher.addNeed(this, new Gson().toJson(needRequest), onAddNeedSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddNeedSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && !TextUtils.isEmpty(response.getReturnMessage())) {
            UiUtil.showToast(this, response.getReturnMessage());
            setResult(Activity.RESULT_OK);
            finish();
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(categoryId))
            errorMessage = R.string.select_a_category;
        else if (TextUtils.isEmpty(activityAddNeedBinding.etWorkLocation.getText()))
            errorMessage = R.string.enter_work_location;
        else if (latLng == null)
            errorMessage = R.string.add_a_location_on_map;
        else if (TextUtils.isEmpty(activityAddNeedBinding.etWorkDetails.getText()))
            errorMessage = R.string.enter_work_details;
        return errorMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                activityAddNeedBinding.tvLocation.setText(place.getName());
                latLng = place.getLatLng();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_category) {
            categoryId = ((Provider) parent.getItemAtPosition(position)).getProviderId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
