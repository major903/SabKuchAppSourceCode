package vedam.subkuch.ui.needs

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.activity.result.contract.ActivityResultContracts
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import vedam.subkuch.network.Response
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityAddNeedBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.DataFetcher.addNeed
import vedam.subkuch.network.DataFetcher.getProviders
import vedam.subkuch.network.models.needs.AddNeedRequest
import vedam.subkuch.network.models.needs.Provider
import vedam.subkuch.network.models.needs.ProviderResponse
import vedam.subkuch.network.models.wallet.WalletResponse
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.shopping.show
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil

class AddNeedActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    private var categoryId: String? = null
    private var binding: ActivityAddNeedBinding? = null
    private var latLng: LatLng? = null
    private var walletResponse: WalletResponse? = null

    private val locationPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding?.tvLocation?.text = result.data?.getStringExtra(LOCATION_ADDRESS)
                latLng = LatLng(
                    result.data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0,
                    result.data?.getDoubleExtra(LONGITUDE, 0.0) ?: 0.0
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_need)
        setToolbarBackButton()
        setTitle(R.string.add_a_need)
        bindCallbacks()
        getCategories()
        getWalletDetails()
    }

    private fun getWalletDetails() {
        UiUtil.showProgressDialog(this, R.string.please_wait)
        DataFetcher.getWalletDetails(
            this,
            onWalletSuccessListener,
            WalletResponse::class.java,
            onErrorListener
        )
    }

    private val onWalletSuccessListener = Response.Listener { response: WalletResponse? ->
        walletResponse = response
        loadUI()
    }

    private fun loadUI() {
        UiUtil.cancelProgressDialog()
        val balance =
            (walletResponse?.returnData?.wallet?.availableBalance?.split(".")?.get(1))?.trim()
                ?.toIntOrNull() ?: 0
        if (balance >= 10) {
            binding?.tvMessage?.show()
            binding?.tvMessage?.text = getString(R.string.yes_money_needs, balance)
            binding?.scrollView?.show()
            binding?.btSubmit?.show()
        } else {
            binding?.tvMessage?.show()
            val ss = SpannableString(getString(R.string.no_money_needs, balance))
            val clickableSpan: ClickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    AppUtil.openUrl(this@AddNeedActivity, "https://vedam-it.com/sabkuch.html")
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = true
                }
            }
            ss.setSpan(
                clickableSpan,
                ss.indexOf("Click"),
                ss.indexOf("Click") + 10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding?.tvMessage?.movementMethod = LinkMovementMethod.getInstance()
            binding?.tvMessage?.text = ss
            binding?.tvMessage?.highlightColor = Color.TRANSPARENT
        }
    }

    override fun moneyWithdrawn() {
        addNeed()
    }

    private fun bindCallbacks() {
        binding!!.btSubmit.setOnClickListener { v: View? ->
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) withdraw()
            else UiUtil.showDialog(
                this,
                getString(errorMessage),
                true
            )
        }
        binding!!.btAddLocation.setOnClickListener {
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

            locationPickerLauncher.launch(locationPickerIntent)
        }
    }

    private fun getCategories() {
//        UiUtil.showProgressDialog(this, R.string.please_wait)
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
        binding!!.spCategory.adapter = adapter
        binding!!.spCategory.onItemSelectedListener = this
        binding!!.spCategory.setSelection(0)
    }

    private fun addNeed() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val needRequest = AddNeedRequest()
        needRequest.userId = AppPrefs.getPrefsUserId(this)
        needRequest.needProviderId = categoryId
        needRequest.workLocation = binding!!.etWorkLocation.text.toString()
        needRequest.workDetails = binding!!.etWorkDetails.text.toString()
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
                binding!!.etWorkLocation.text
            )
        ) errorMessage = R.string.enter_work_location else if (latLng == null) errorMessage =
            R.string.add_a_location_on_map else if (TextUtils.isEmpty(
                binding!!.etWorkDetails.text
            )
        ) errorMessage = R.string.enter_work_details
        return errorMessage
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (parent.id == R.id.sp_category) {
            categoryId = (parent.getItemAtPosition(position) as Provider).providerId
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
