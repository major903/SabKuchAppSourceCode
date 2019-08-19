package vedam.subkuch.ui.needs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityAddNeedBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.needs.AddNeedRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AddNeedActivity extends BaseActivity {

    private ActivityAddNeedBinding activityAddNeedBinding;
    private LatLng latLng;
    private String needProviderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_need);
        setToolbarBackButton();
        setTitle(R.string.add_booking);

        getExtras();
        bindCallbacks();
    }

    private void getExtras() {
        if (getIntent() != null)
            needProviderId = getIntent().getStringExtra(Constants.EXTRA_ID);
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

    private void addNeed() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));

        AddNeedRequest needRequest = new AddNeedRequest();
        needRequest.setUserId(AppPrefs.getPrefsUserId(this));
        needRequest.setNeedProviderId(needProviderId);
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
        if (TextUtils.isEmpty(activityAddNeedBinding.etWorkLocation.getText()))
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
}
