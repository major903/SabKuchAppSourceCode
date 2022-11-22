package vedam.subkuch.ui.directory

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.android.volley.Response
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.base.BaseAddImagesFragment
import vedam.subkuch.databinding.FragmentAddDirectoryBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addBusiness
import vedam.subkuch.network.DataFetcher.getCategories
import vedam.subkuch.network.DataFetcher.getCities
import vedam.subkuch.network.DataFetcher.getCountries
import vedam.subkuch.network.DataFetcher.getSubCategories
import vedam.subkuch.network.models.CountriesResponse
import vedam.subkuch.network.models.Country
import vedam.subkuch.network.models.SubCategory
import vedam.subkuch.ui.directory.models.*
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.jobs.models.CitiesResponse
import vedam.subkuch.ui.jobs.models.City
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddDirectoryFragment : BaseAddImagesFragment(), AdapterView.OnItemSelectedListener {
    private var fragmentAddDirectoryBinding: FragmentAddDirectoryBinding? = null
    private val alBranches = ArrayList<View>()
    private var categoryId: String? = null
    private var subcategoryId: String? = null
    private var cityId: String? = null
    private var countryId: String? = null
    private var viewTappedForLocation: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        if (getArguments() != null) {
//            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
//            subcategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
//        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for context fragment
        fragmentAddDirectoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_directory, container, false)
        return fragmentAddDirectoryBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImagesLayout(view, 1)
        bindCallbacks()
        categories
        countries
        cities
        addBranch()
    }

    private val categories: Unit
        private get() {
            UiUtil.showProgressDialog(mContext, R.string.please_wait)
            getCategories(
                mContext,
                onCategorySuccessListener,
                CategoryResponse::class.java,
                onErrorListener
            )
        }
    private val onCategorySuccessListener = Response.Listener { response: CategoryResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == Constants.TRUE) {
            setCategories(response.categoryResult.categories)
        } else UiUtil.showToast(mContext, getString(R.string.no_data))
    }

    private fun setCategories(categories: ArrayList<Category>) {
        val category = Category()
        category.name = getString(R.string.select_a_category)
        categories.add(0, category)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, categories
        )
        fragmentAddDirectoryBinding!!.spCategory.adapter = adapter
        fragmentAddDirectoryBinding!!.spCategory.onItemSelectedListener = this
        fragmentAddDirectoryBinding!!.spCategory.setSelection(0)
    }

    private fun getSubCategories() {
        UiUtil.showProgressDialog(mContext, R.string.please_wait)
        getSubCategories(
            mContext,
            onSubCategorySuccessListener,
            SubCategoryResponse::class.java,
            onErrorListener,
            categoryId
        )
    }
    private val onSubCategorySuccessListener = Response.Listener { response: SubCategoryResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == Constants.TRUE) {
            setSubcategories(response.subCategoryResult.subCategories)
        } else UiUtil.showToast(mContext, getString(R.string.err_occurred))
    }

    private fun setSubcategories(subCategories: ArrayList<SubCategory>) {
        val subCategory = SubCategory()
        subCategory.subCategoryName = getString(R.string.select_a_sub_category)
        subCategories.add(0, subCategory)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, subCategories
        )
        fragmentAddDirectoryBinding!!.spSubCategory.adapter = adapter
        fragmentAddDirectoryBinding!!.spSubCategory.onItemSelectedListener = this
        fragmentAddDirectoryBinding!!.spSubCategory.setSelection(0)
    }

    private val cities: Unit
        private get() {
            UiUtil.showProgressDialog(mContext, getString(R.string.loading))
            getCities(mContext, onCitiesSuccessListener, CitiesResponse::class.java, onErrorListener)
        }
    private val onCitiesSuccessListener = Response.Listener { response: CitiesResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
            setCities(response.returnData)
        } else {
            UiUtil.showToast(mContext, getString(R.string.err_occurred))
        }
    }

    private fun setCities(cities: ArrayList<City>) {
        val city = City()
        city.name = getString(R.string.select_a_city)
        cities.add(0, city)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, cities
        )
        fragmentAddDirectoryBinding!!.spCity.adapter = adapter
        fragmentAddDirectoryBinding!!.spCity.onItemSelectedListener = this
        fragmentAddDirectoryBinding!!.spCity.setSelection(0)
    }

    private val countries: Unit
        private get() {
            UiUtil.showProgressDialog(mContext, getString(R.string.loading))
            getCountries(
                mContext,
                onCountriesSuccessListener,
                CountriesResponse::class.java,
                onErrorListener
            )
        }
    private val onCountriesSuccessListener = Response.Listener { response: CountriesResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
            setCountries(response.countries)
        } else {
            UiUtil.showToast(mContext, getString(R.string.err_occurred))
        }
    }

    private fun setCountries(countries: ArrayList<Country>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, countries
        )
        fragmentAddDirectoryBinding!!.spCountry.adapter = adapter
        fragmentAddDirectoryBinding!!.spCountry.onItemSelectedListener = this
        fragmentAddDirectoryBinding!!.spCountry.setSelection(getIndexOfIndia(countries))
    }

    private fun getIndexOfIndia(countries: ArrayList<Country>): Int {
        for (i in countries.indices) {
            val country = countries[i]
            if (country.name.equals("India", ignoreCase = true)) return i
        }
        return 0
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
            } else UiUtil.showDialog(mContext, getString(errorMessage), true)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindCallbacks() {
        fragmentAddDirectoryBinding!!.btAddBranch.setOnClickListener { view: View? ->
            if (alBranches.size != 20) {
                addBranch()
            } else UiUtil.showToast(mContext, getString(R.string.no_more_branches))
        }
    }

    private fun addBranch() {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val v = layoutInflater.inflate(
            R.layout.layout_branch,
            fragmentAddDirectoryBinding!!.llContainer,
            false
        )
        val b = v.findViewById<Button>(R.id.bt_remove)
        if (alBranches.isNotEmpty()) b.setOnClickListener { view1: View? ->
            alBranches.remove(v)
            fragmentAddDirectoryBinding!!.llContainer.removeView(v)
        } else b.visibility = View.GONE

        val btLocation = v.findViewById<Button>(R.id.bt_add_location);

        btLocation.setOnClickListener{
            viewTappedForLocation = v
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
                .build(requireContext())

            startActivityForResult(locationPickerIntent, Constants.REQUEST_PLACE_PICKER)
        }
        alBranches.add(v)
        fragmentAddDirectoryBinding!!.llContainer.addView(v, params)
    }

    private fun submit() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        val addBusinessRequest = AddBusinessRequest()
        addBusinessRequest.categoryID = categoryId
        addBusinessRequest.subCategoryID = subcategoryId
        //        addBusinessRequest.setCountryid(countryId);
//        addBusinessRequest.setCityid(cityId);
        addBusinessRequest.businessName =
            fragmentAddDirectoryBinding!!.etBusinessName.text.toString()
        addBusinessRequest.website = fragmentAddDirectoryBinding!!.etWebsite.text.toString()
        if (imageItemMap.isNotEmpty()) addBusinessRequest.businessImage =
            AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(mContext, imageItemMap))
        val alBusinessAddresses = ArrayList<BusinessAddress>()
        for (v in alBranches) {
            val businessAddress = BusinessAddress()
            val etDealingIn = v.findViewById<EditText>(R.id.et_dealing_in)
            val etAddress = v.findViewById<EditText>(R.id.et_address)
            val etPhone = v.findViewById<EditText>(R.id.et_phone)
            val etCellPhone1 = v.findViewById<EditText>(R.id.et_cellphone_1)
            val etCellPhone2 = v.findViewById<EditText>(R.id.et_cellphone_2)
            val etEmail = v.findViewById<EditText>(R.id.et_email)
            val etContactPerson = v.findViewById<EditText>(R.id.et_contact_person)
            val etInfo1 = v.findViewById<EditText>(R.id.et_info_1)
            val etInfo2 = v.findViewById<EditText>(R.id.et_info_2)
            val etZipCode = v.findViewById<EditText>(R.id.et_zip_code)

//            businessAddress.setAddress1(etAddressLine1.getText().toString());
//            businessAddress.setAddress2(AppUtil.deNull(etAddressLine2.getText().toString()));
            businessAddress.DealingIn = AppUtil.deNull(etDealingIn.text.toString())
            businessAddress.Address = etAddress.text.toString()
            businessAddress.Zipcode = etZipCode.text.toString()
            businessAddress.PhoneNo = AppUtil.deNull(etPhone.text.toString())
            businessAddress.Mobile1 = AppUtil.deNull(etCellPhone1.text.toString())
            businessAddress.Mobile2 = AppUtil.deNull(etCellPhone2.text.toString())
            businessAddress.Email = etEmail.text.toString()
            businessAddress.ContactPerson = etContactPerson.text.toString()
            businessAddress.InfoLine1 = AppUtil.deNull(etInfo1.text.toString())
            businessAddress.InfoLine2 = AppUtil.deNull(etInfo2.text.toString())
            val latLng = v.tag as LatLng
            businessAddress.latitude = latLng.latitude.toString()
            businessAddress.longitude = latLng.longitude.toString()
            alBusinessAddresses.add(businessAddress)
        }
        addBusinessRequest.businessAddresses = alBusinessAddresses
        addBusiness(
            mContext,
            Gson().toJson(addBusinessRequest),
            onAddBusinessSuccessListener,
            AddResponse::class.java,
            onErrorListener
        )
    }

    private val onAddBusinessSuccessListener = Response.Listener { response: AddResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.isStatus) {
            UiUtil.showToast(mContext, response.message)
            activity!!.setResult(RESULT_OK)
            activity!!.finish()
        } else UiUtil.showToast(mContext, getString(R.string.err_occurred))
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(categoryId)) errorMessage =
            R.string.select_a_category else if (TextUtils.isEmpty(subcategoryId)) errorMessage =
            R.string.select_a_sub_category else if (TextUtils.isEmpty(
                fragmentAddDirectoryBinding!!.etBusinessName.text
            )
        ) errorMessage = R.string.enter_business_name else if (alBranches.isEmpty()) errorMessage =
            R.string.add_one_branch else {
            for (v in alBranches) {
                val etDealingIn = v.findViewById<EditText>(R.id.et_dealing_in)
                val etAddress = v.findViewById<EditText>(R.id.et_address)
                val etPhone = v.findViewById<EditText>(R.id.et_phone)
                val etCellPhone1 = v.findViewById<EditText>(R.id.et_cellphone_1)
                val etEmail = v.findViewById<EditText>(R.id.et_email)
                val etZipCode = v.findViewById<EditText>(R.id.et_zip_code)
                if (TextUtils.isEmpty(etAddress.text)) errorMessage =
                    R.string.enter_address else if (TextUtils.isEmpty(etZipCode.text)) errorMessage =
                    R.string.enter_zip_code else if (!TextUtils.isEmpty(etEmail.text) && !AppUtil.validateEmail(
                        etEmail.text.toString()
                    )
                ) errorMessage = R.string.enter_valid_email
                else if (v.tag == null)
                    errorMessage = R.string.add_a_location_for_a_branch
            }
        }
        return errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {

                val latLng = com.google.android.gms.maps.model.LatLng(
                    data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0, data?.getDoubleExtra(
                        LONGITUDE, 0.0
                    ) ?: 0.0
                )

                val tv = viewTappedForLocation?.findViewById<TextView>(R.id.tv_location)
//                val etCity = viewTappedForLocation.findViewById(R.id.et_city);
//                val etState = viewTappedForLocation.findViewById(R.id.et_state);
//                val etCountry = viewTappedForLocation.findViewById(R.id.et_country);
//                val etZipCode = viewTappedForLocation.findViewById(R.id.et_zip_code);

                UiUtil.setTextView(tv, data?.getStringExtra(LOCATION_ADDRESS))

                viewTappedForLocation?.tag = latLng
            }
        } else
            super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.sp_category -> {
                categoryId = (parent.getItemAtPosition(position) as Category).categoryId
                if (!TextUtils.isEmpty(categoryId)) {
                    subcategoryId = null
                    getSubCategories()
                }
            }
            R.id.sp_sub_category -> subcategoryId =
                (parent.getItemAtPosition(position) as SubCategory).subCategoryId
            R.id.sp_city -> cityId = (parent.getItemAtPosition(position) as City).cityid
            R.id.sp_country -> countryId = (parent.getItemAtPosition(position) as Country).countryid
        }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {}

    companion object {
        @JvmStatic
        fun newInstance(): AddDirectoryFragment {
            return AddDirectoryFragment()
        }
    }
}