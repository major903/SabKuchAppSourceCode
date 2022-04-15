package vedam.subkuch.ui.needs

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
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityAddNeedBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addNeed
import vedam.subkuch.network.DataFetcher.getProviders
import vedam.subkuch.network.models.needs.AddNeedRequest
import vedam.subkuch.network.models.needs.Provider
import vedam.subkuch.network.models.needs.ProviderResponse
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.UiUtil
import java.util.*

class AddNeedActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var categoryId: String? = null
    private var activityAddNeedBinding: ActivityAddNeedBinding? = null
    private var latLng: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddNeedBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_need)
        setToolbarBackButton()
        setTitle(R.string.add_a_need)
        bindCallbacks()
        getCategories()
    }

    private fun bindCallbacks() {
        activityAddNeedBinding!!.btSubmit.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) addNeed() else UiUtil.showDialog(
                this,
                getString(errorMessage),
                true
            )
        }
        activityAddNeedBinding!!.btAddLocation.setOnClickListener {
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

    private fun getCategories() {
        UiUtil.showProgressDialog(this, R.string.please_wait)
        getProviders(
            this,
            onProviderSuccessListener,
            ProviderResponse::class.java,
            onErrorListener
        )
    }

    private val onProviderSuccessListener = Response.Listener { response: ProviderResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            setCategories(response.returnData)
        } else UiUtil.showToast(this, getString(R.string.no_data))
    }

    private fun setCategories(categories: ArrayList<Provider>) {
        val provider = Provider()
        provider.providers = getString(R.string.select_a_category)
        categories.add(0, provider)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, categories
        )
        activityAddNeedBinding!!.spCategory.adapter = adapter
        activityAddNeedBinding!!.spCategory.onItemSelectedListener = this
        activityAddNeedBinding!!.spCategory.setSelection(0)
    }

    private fun addNeed() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val needRequest = AddNeedRequest()
        needRequest.userId = AppPrefs.getPrefsUserId(this)
        needRequest.needProviderId = categoryId
        needRequest.workLocation = activityAddNeedBinding!!.etWorkLocation.text.toString()
        needRequest.workDetails = activityAddNeedBinding!!.etWorkDetails.text.toString()
        needRequest.latitude = latLng!!.latitude.toString()
        needRequest.longitude = latLng!!.longitude.toString()
        addNeed(
            this,
            Gson().toJson(needRequest),
            onAddNeedSuccessListener,
            AddResponse::class.java,
            onErrorListener
        )
    }

    private val onAddNeedSuccessListener = Response.Listener { response: AddResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && !TextUtils.isEmpty(response.returnMessage)) {
            UiUtil.showToast(this, response.returnMessage)
            setResult(RESULT_OK)
            finish()
        } else UiUtil.showToast(this, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(categoryId)) errorMessage =
            R.string.select_a_category else if (TextUtils.isEmpty(
                activityAddNeedBinding!!.etWorkLocation.text
            )
        ) errorMessage = R.string.enter_work_location else if (latLng == null) errorMessage =
            R.string.add_a_location_on_map else if (TextUtils.isEmpty(
                activityAddNeedBinding!!.etWorkDetails.text
            )
        ) errorMessage = R.string.enter_work_details
        return errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                activityAddNeedBinding?.tvLocation?.text = data?.getStringExtra(LOCATION_ADDRESS)
                latLng = LatLng(
                    data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0,
                    data?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0
                )
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (parent.id == R.id.sp_category) {
            categoryId = (parent.getItemAtPosition(position) as Provider).providerId
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}