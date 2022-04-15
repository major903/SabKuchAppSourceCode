package vedam.subkuch.ui.classifieds

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
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
import vedam.subkuch.base.BaseAddImagesFragment
import vedam.subkuch.databinding.FragmentAddClassifiedBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addClassified
import vedam.subkuch.network.DataFetcher.getCities
import vedam.subkuch.network.DataFetcher.getClassifiedSubCategories
import vedam.subkuch.network.DataFetcher.getClassifiedsCategories
import vedam.subkuch.network.DataFetcher.uploadClassifiedImage
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.DataPart
import vedam.subkuch.network.models.GeneralResponse
import vedam.subkuch.network.models.classifieds.*
import vedam.subkuch.ui.jobs.models.CitiesResponse
import vedam.subkuch.ui.jobs.models.City
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

class AddClassifiedFragment : BaseAddImagesFragment(), AdapterView.OnItemSelectedListener {
    private var binding: FragmentAddClassifiedBinding? = null
    private var latLng: LatLng? = null
    private var categoryId: String? = null
    private var subcategoryId: String? = null
    private var cityId: String? = null
    private var successMessage: String? = null
    private var isProperty = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for context fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_classified, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagesLayout(view, 1)
        getCategories()
        getCities()
        bindCallbacks()
    }

    private fun getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        val type = object : TypeToken<ClassifiedResponse<ClassifiedCategory?>?>() {}.type
        getClassifiedsCategories(context, onCategorySuccessListener, type, onErrorListener)
    }

    private val onCategorySuccessListener =
        Response.Listener { response: ClassifiedResponse<ClassifiedCategory>? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                setCategories(response.returnData)
            } else UiUtil.showToast(context, getString(R.string.no_data))
        }

    private fun setCategories(categories: ArrayList<ClassifiedCategory>) {
        val category = ClassifiedCategory()
        category.category = getString(R.string.select_a_category)
        categories.add(0, category)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, categories
        )
        binding!!.spCategory.adapter = adapter
        binding!!.spCategory.onItemSelectedListener = this
        binding!!.spCategory.setSelection(0)
    }

    private fun getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        val type = object : TypeToken<ClassifiedResponse<ClassifiedSubCategory?>?>() {}.type
        getClassifiedSubCategories(
            context,
            onSubCategorySuccessListener,
            type,
            onErrorListener,
            categoryId
        )
    }

    private val onSubCategorySuccessListener =
        Response.Listener { response: ClassifiedResponse<ClassifiedSubCategory>? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                setSubcategories(response.returnData)
            } else UiUtil.showToast(context, getString(R.string.err_occurred))
        }

    private fun setSubcategories(subCategories: ArrayList<ClassifiedSubCategory>) {
        val subCategory = ClassifiedSubCategory()
        subCategory.subCategory = getString(R.string.select_a_sub_category)
        subCategories.add(0, subCategory)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, subCategories
        )
        binding!!.spSubCategory.adapter = adapter
        binding!!.spSubCategory.onItemSelectedListener = this
        binding!!.spSubCategory.setSelection(0)
    }

    private fun getCities() {
        UiUtil.showProgressDialog(context, getString(R.string.loading))
        getCities(context, onCitiesSuccessListener, CitiesResponse::class.java, onErrorListener)
    }

    private val onCitiesSuccessListener = Response.Listener { response: CitiesResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
            setCities(response.returnData)
        } else {
            UiUtil.showToast(context, getString(R.string.err_occurred))
        }
    }

    private fun setCities(cities: ArrayList<City>) {
        val city = City()
        city.name = getString(R.string.select_a_city)
        cities.add(0, city)
        val adapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item, cities
        )
        binding!!.spCity.adapter = adapter
        binding!!.spCity.onItemSelectedListener = this
        binding!!.spCity.setSelection(0)
    }

    private fun bindCallbacks() {
        binding!!.btAddLocation.setOnClickListener { view: View? ->
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
                .build(context)

            startActivityForResult(locationPickerIntent, Constants.REQUEST_PLACE_PICKER)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.done, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) {
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                submit()
            } else UiUtil.showDialog(context, getString(errorMessage), true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submit() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val classifiedRequest = Classified()
        classifiedRequest.categoryId = categoryId
        classifiedRequest.subCategoryId = subcategoryId
        classifiedRequest.userId = AppPrefs.getPrefsUserId(context)
        classifiedRequest.cityId = cityId
        classifiedRequest.locality = binding!!.etLocality.text.toString()
        classifiedRequest.rate = binding!!.etRate.text.toString()
        classifiedRequest.about = binding!!.etAbout.text.toString()
        classifiedRequest.title = binding!!.etTitle.text.toString()
        classifiedRequest.dailyDiscount = binding!!.etDiscount.text.toString()
        classifiedRequest.contact = binding!!.etContact.text.toString()
        classifiedRequest.latitude = latLng!!.latitude.toString()
        classifiedRequest.longitude = latLng!!.longitude.toString()
        addClassified(
            context,
            Gson().toJson(classifiedRequest),
            onAddClassifiedSuccessListener,
            AddClassifiedResponse::class.java,
            onErrorListener
        )
    }

    private val onAddClassifiedSuccessListener =
        Response.Listener { response: AddClassifiedResponse? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnCode == Constants.SUCCESS_RETURN_CODE) {
                successMessage = response.returnMessage
                isImageAvailable(response.classified.postedAdId)
            } else UiUtil.showToast(context, getString(R.string.err_occurred))
        }

    private fun isImageAvailable(classfiedId: String) {
        if (imageItemMap.size > 0) uploadClassifiedImage(classfiedId) else {
            UiUtil.showToast(context, successMessage!!)
            if (globalFragmentInteractionListener != null) {
                globalFragmentInteractionListener.setFragmentResult(Activity.RESULT_OK, null)
                globalFragmentInteractionListener.finishActivity()
            }
        }
    }

    private fun uploadClassifiedImage(classfiedId: String) {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val params: MutableMap<String?, DataPart?> = HashMap()
        params[NetworkConstants.ProfileImage] = DataPart(
            AppUtil.getUniqueFileName(),
            AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(context, imageItemMap)),
            NetworkConstants.JPEG_MIME_TYPE
        )
        uploadClassifiedImage(
            context,
            params,
            onImageUploadSuccessListener,
            GeneralResponse::class.java,
            onErrorListener,
            classfiedId
        )
    }

    private val onImageUploadSuccessListener = Response.Listener { response: GeneralResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnCode == Constants.SUCCESS_RETURN_CODE) {
            UiUtil.showToast(context, successMessage!!)
            activity!!.finish()
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(categoryId)) errorMessage =
            R.string.select_a_category else if (TextUtils.isEmpty(subcategoryId)) errorMessage =
            R.string.select_a_sub_category else if (TextUtils.isEmpty(cityId)) errorMessage =
            R.string.select_a_city else if (TextUtils.isEmpty(
                binding!!.etLocality.text
            )
        ) errorMessage = R.string.enter_location else if (TextUtils.isEmpty(
                binding!!.etTitle.text
            )
        ) errorMessage = R.string.enter_title else if (TextUtils.isEmpty(
                binding!!.etAbout.text
            )
        ) errorMessage = R.string.enter_details else if (TextUtils.isEmpty(
                binding!!.etRate.text
            )
        ) errorMessage = R.string.enter_rate else if (TextUtils.isEmpty(
                binding!!.etDiscount.text
            ) && !isProperty
        ) errorMessage = R.string.enter_daily_discount else if (TextUtils.isEmpty(
                binding!!.etContact.text
            )
        ) errorMessage = R.string.enter_contact else if (latLng == null) errorMessage =
            R.string.add_a_location
        return errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                binding!!.tvLocation.text = data?.getStringExtra(LOCATION_ADDRESS)
                latLng = LatLng(
                    data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0, data?.getDoubleExtra(
                        LONGITUDE, 0.0
                    ) ?: 0.0
                )
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.sp_category -> {
                categoryId = (parent.getItemAtPosition(position) as ClassifiedCategory).categoryId
                if (!TextUtils.isEmpty(categoryId)) {
                    subcategoryId = null
                    getSubCategories()
                    if (categoryId == "1") {
                        isProperty = true
                        hideViewsForProperty()
                    } else {
                        isProperty = false
                        showViewsForProperty()
                    }
                }
            }
            R.id.sp_sub_category -> subcategoryId =
                (parent.getItemAtPosition(position) as ClassifiedSubCategory).subCategoryId
            R.id.sp_city -> cityId = (parent.getItemAtPosition(position) as City).cityid
        }
    }

    private fun hideViewsForProperty() {
        binding!!.etDiscount.visibility = View.GONE
        binding!!.tvDiscount.visibility = View.GONE
    }

    private fun showViewsForProperty() {
        binding!!.etDiscount.visibility = View.VISIBLE
        binding!!.tvDiscount.visibility = View.VISIBLE
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    companion object {
        @JvmStatic
        fun newInstance(): AddClassifiedFragment {
            return AddClassifiedFragment()
        }
    }
}