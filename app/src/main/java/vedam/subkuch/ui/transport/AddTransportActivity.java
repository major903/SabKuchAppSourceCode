package vedam.subkuch.ui.transport;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Response;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Stack;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityAddTransportBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.transport.AddTransportRequest;
import vedam.subkuch.network.models.BaseGetMasterModel;
import vedam.subkuch.network.models.LabourRequirement;
import vedam.subkuch.network.models.VehicleType;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AddTransportActivity extends BaseActivity {

    private ActivityAddTransportBinding activityAddTransportBinding;
    private Stack<Object> requestStack = new Stack<>();
    private ArrayList<LabourRequirement> labourRequirements = new ArrayList<>();
    private ArrayList<VehicleType> vehicleTypes = new ArrayList<>();
    private String vehicleTypeId, labourRequirementId;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddTransportBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_transport);

        setToolbarBackButton();
        setTitle(R.string.add_booking);

        requestStack.add(new Object());
        requestStack.add(new Object());
        UiUtil.showProgressDialog(this, getString(R.string.loading));
        getLabourRequirement();
        getVehicleType();
        bindCallbacks();
    }

    private void getLabourRequirement() {
        Type type = new TypeToken<BaseGetMasterModel<LabourRequirement>>() {
        }.getType();
        DataFetcher.getLabourRequirement(this, onLabourSuccessListener, type, onErrorListener);
    }

    private void getVehicleType() {
        Type type = new TypeToken<BaseGetMasterModel<VehicleType>>() {
        }.getType();
        DataFetcher.getVehicleType(this, onVehicleTypeSuccessListener, type, onErrorListener);
    }

    private void bindCallbacks() {

        activityAddTransportBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0)
                addTransport();
            else
                UiUtil.showDialog(this, getString(errorMessage), true);
        });

        activityAddTransportBinding.btAddLocation.setOnClickListener(view -> {
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

    private void addTransport() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));

        AddTransportRequest transportRequest = new AddTransportRequest();
        transportRequest.setUserId(AppPrefs.getPrefsUserId(this));
        transportRequest.setPickupLocation(activityAddTransportBinding.etPickUpLocation.getText().toString());
        transportRequest.setDropLocation(activityAddTransportBinding.etDestination.getText().toString());
        transportRequest.setItemType(activityAddTransportBinding.etTransportMaterial.getText().toString());
        transportRequest.setLaborRequired(labourRequirementId);
        transportRequest.setVehicleTypeId(vehicleTypeId);
        transportRequest.setLatitude(String.valueOf(latLng.latitude));
        transportRequest.setLongitude(String.valueOf(latLng.longitude));

        DataFetcher.addTransport(this, new Gson().toJson(transportRequest), onAddTransportSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddTransportSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            UiUtil.showToast(this, getString(R.string.transport_added_successfully));
            setResult(Activity.RESULT_OK);
            finish();
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private Response.Listener<BaseGetMasterModel<LabourRequirement>> onLabourSuccessListener = response -> {
        requestStack.pop();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            labourRequirements = response.getReturnData();
            setLabourRequirements();
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
        checkFlagAndLoadUI();
    };

    private Response.Listener<BaseGetMasterModel<VehicleType>> onVehicleTypeSuccessListener = response -> {
        requestStack.pop();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            vehicleTypes = response.getReturnData();
            setVehicleTypes();
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
        }
        checkFlagAndLoadUI();
    };

    private void checkFlagAndLoadUI() {
        if (requestStack.isEmpty())
            UiUtil.cancelProgressDialog();
    }

    public void setLabourRequirements() {
        LabourRequirement labourRequirement = new LabourRequirement();
        labourRequirement.setTransportCoolieName(getString(R.string.select_labour_requirement));
        labourRequirements.add(0, labourRequirement);

        ArrayAdapter<LabourRequirement> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, labourRequirements);
        activityAddTransportBinding.spLabourRequirement.setAdapter(adapter);
        activityAddTransportBinding.spLabourRequirement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labourRequirementId = ((LabourRequirement) parent.getItemAtPosition(position)).getTransportCoolieId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activityAddTransportBinding.spLabourRequirement.setSelection(0);
    }

    public void setVehicleTypes() {

        VehicleType vehicleType = new VehicleType();
        vehicleType.setTransportTypeName(getString(R.string.select_vehicle_type));
        vehicleTypes.add(0, vehicleType);

        ArrayAdapter<VehicleType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, vehicleTypes);
        activityAddTransportBinding.spVehicleType.setAdapter(adapter);
        activityAddTransportBinding.spVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleTypeId = ((VehicleType) parent.getItemAtPosition(position)).getTransportTypeId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activityAddTransportBinding.spVehicleType.setSelection(0);
    }

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityAddTransportBinding.etPickUpLocation.getText()))
            errorMessage = R.string.enter_pick_up_location;
        else if (latLng == null)
            errorMessage = R.string.add_a_location_on_map;
        else if (TextUtils.isEmpty(activityAddTransportBinding.etDestination.getText()))
            errorMessage = R.string.enter_destination;
        else if (TextUtils.isEmpty(activityAddTransportBinding.etTransportMaterial.getText()))
            errorMessage = R.string.enter_transport_material;
        else if (TextUtils.isEmpty(labourRequirementId))
            errorMessage = R.string.select_labour_requirement;
        else if (TextUtils.isEmpty(vehicleTypeId))
            errorMessage = R.string.select_vehicle_type;
        return errorMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                activityAddTransportBinding.tvLocation.setText(place.getName());
                latLng = place.getLatLng();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
