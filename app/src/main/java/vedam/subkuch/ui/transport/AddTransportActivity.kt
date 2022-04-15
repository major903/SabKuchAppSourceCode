package vedam.subkuch.ui.transport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.android.volley.Response
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityAddTransportBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addTransport
import vedam.subkuch.network.DataFetcher.getLabourRequirement
import vedam.subkuch.network.DataFetcher.getVehicleType
import vedam.subkuch.network.models.BaseGetMasterModel
import vedam.subkuch.network.models.LabourRequirement
import vedam.subkuch.network.models.VehicleType
import vedam.subkuch.network.models.transport.AddTransportRequest
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.UiUtil
import java.util.*

class AddTransportActivity : BaseActivity() {
    private var activityAddTransportBinding: ActivityAddTransportBinding? = null
    private val requestStack = Stack<Any>()
    private var labourRequirements = ArrayList<LabourRequirement>()
    private var vehicleTypes = ArrayList<VehicleType>()
    private var vehicleTypeId: String? = null
    private var labourRequirementId: String? = null
    private var latLng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddTransportBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_transport)
        setToolbarBackButton()
        setTitle(R.string.add_booking)
        requestStack.add(Any())
        requestStack.add(Any())
        UiUtil.showProgressDialog(this, getString(R.string.loading))
        getLabourRequirement()
        getVehicleType()
        bindCallbacks()
    }

    private fun getLabourRequirement() {
        val type = object : TypeToken<BaseGetMasterModel<LabourRequirement?>?>() {}.type
        getLabourRequirement(this, onLabourSuccessListener, type, onErrorListener)
    }

    private fun getVehicleType(): Unit {
        val type = object : TypeToken<BaseGetMasterModel<VehicleType?>?>() {}.type
        getVehicleType(this, onVehicleTypeSuccessListener, type, onErrorListener)
    }

    private fun bindCallbacks() {
        activityAddTransportBinding!!.btSubmit.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) addTransport() else UiUtil.showDialog(
                this,
                getString(errorMessage),
                true
            )
        }
        activityAddTransportBinding!!.btAddLocation.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withGeolocApiKey(Constants.MAPS_API_KEY)
                .withGooglePlacesApiKey(Constants.MAPS_API_KEY)
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .build(this)

            startActivityForResult(locationPickerIntent, Constants.REQUEST_PLACE_PICKER)
        }
    }

    private fun addTransport() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val transportRequest = AddTransportRequest()
        transportRequest.userId = AppPrefs.getPrefsUserId(this)
        transportRequest.pickupLocation =
            activityAddTransportBinding!!.etPickUpLocation.text.toString()
        transportRequest.dropLocation = activityAddTransportBinding!!.etDestination.text.toString()
        transportRequest.itemType =
            activityAddTransportBinding!!.etTransportMaterial.text.toString()
        transportRequest.laborRequired = labourRequirementId
        transportRequest.vehicleTypeId = vehicleTypeId
        transportRequest.latitude = latLng!!.latitude.toString()
        transportRequest.longitude = latLng!!.longitude.toString()
        addTransport(
            this,
            Gson().toJson(transportRequest),
            onAddTransportSuccessListener,
            AddResponse::class.java,
            onErrorListener
        )
    }

    private val onAddTransportSuccessListener = Response.Listener { response: AddResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            UiUtil.showToast(this, getString(R.string.transport_added_successfully))
            setResult(RESULT_OK)
            finish()
        } else UiUtil.showToast(this, getString(R.string.err_occurred))
    }
    private val onLabourSuccessListener =
        Response.Listener { response: BaseGetMasterModel<LabourRequirement>? ->
            requestStack.pop()
            if (response != null && response.returnMessage == Constants.SUCCESS) {
                labourRequirements = response.returnData
                setLabourRequirements()
            } else {
                UiUtil.showToast(this, getString(R.string.err_occurred))
            }
            checkFlagAndLoadUI()
        }
    private val onVehicleTypeSuccessListener =
        Response.Listener { response: BaseGetMasterModel<VehicleType>? ->
            requestStack.pop()
            if (response != null && response.returnMessage == Constants.SUCCESS) {
                vehicleTypes = response.returnData
                setVehicleTypes()
            } else {
                UiUtil.showToast(this, getString(R.string.err_occurred))
            }
            checkFlagAndLoadUI()
        }

    private fun checkFlagAndLoadUI() {
        if (requestStack.isEmpty()) UiUtil.cancelProgressDialog()
    }

    fun setLabourRequirements() {
        val labourRequirement = LabourRequirement()
        labourRequirement.transportCoolieName = getString(R.string.select_labour_requirement)
        labourRequirements.add(0, labourRequirement)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, labourRequirements
        )
        activityAddTransportBinding!!.spLabourRequirement.adapter = adapter
        activityAddTransportBinding!!.spLabourRequirement.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    labourRequirementId =
                        (parent.getItemAtPosition(position) as LabourRequirement).transportCoolieId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        activityAddTransportBinding!!.spLabourRequirement.setSelection(0)
    }

    fun setVehicleTypes() {
        val vehicleType = VehicleType()
        vehicleType.transportTypeName = getString(R.string.select_vehicle_type)
        vehicleTypes.add(0, vehicleType)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, vehicleTypes
        )
        activityAddTransportBinding!!.spVehicleType.adapter = adapter
        activityAddTransportBinding!!.spVehicleType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    vehicleTypeId =
                        (parent.getItemAtPosition(position) as VehicleType).transportTypeId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        activityAddTransportBinding!!.spVehicleType.setSelection(0)
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(activityAddTransportBinding!!.etPickUpLocation.text)) errorMessage =
            R.string.enter_pick_up_location else if (latLng == null) errorMessage =
            R.string.add_a_location_on_map else if (TextUtils.isEmpty(
                activityAddTransportBinding!!.etDestination.text
            )
        ) errorMessage = R.string.enter_destination else if (TextUtils.isEmpty(
                activityAddTransportBinding!!.etTransportMaterial.text
            )
        ) errorMessage = R.string.enter_transport_material else if (TextUtils.isEmpty(
                labourRequirementId
            )
        ) errorMessage =
            R.string.select_labour_requirement else if (TextUtils.isEmpty(vehicleTypeId)) errorMessage =
            R.string.select_vehicle_type
        return errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                activityAddTransportBinding?.tvLocation?.text = data?.getStringExtra(LOCATION_ADDRESS)
                latLng = LatLng(data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0, data?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}