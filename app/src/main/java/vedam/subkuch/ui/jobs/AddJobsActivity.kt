package vedam.subkuch.ui.jobs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.android.volley.Response
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityAddJobsBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.addJobs
import vedam.subkuch.network.DataFetcher.getCities
import vedam.subkuch.network.DataFetcher.getJobsCategory
import vedam.subkuch.ui.jobs.models.*
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

class AddJobsActivity : BaseActivity() {
    private var activityAddJobsBinding: ActivityAddJobsBinding? = null
    private var jobCategoryId: String? = null
    private val alJobs = ArrayList<View>()

    private var latLng: LatLng? = null
    private var cityId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddJobsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_jobs)
        setToolbarBackButton()
        setTitle(R.string.post_a_job)

//        jobCategoryId = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_ID);
        getJobCategory()
        getCities()
        bindCallbacks()
    }

    private fun getJobCategory() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        getJobsCategory(
            this,
            onJobCategorySuccessListener,
            JobCategoryResponse::class.java,
            onErrorListener
        )
    }

    private val onJobCategorySuccessListener = Response.Listener { response: JobCategoryResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.status == Constants.TRUE) {
            setJobCategories(response.jobCategoriesResult.jobCategories)
        } else UiUtil.showToast(this, getString(R.string.err_occurred))
    }

    private fun setJobCategories(jobCategories: ArrayList<JobCategory>) {
        val jobCategory = JobCategory()
        jobCategory.jobCategoryName = getString(R.string.select_a_category)
        jobCategories.add(0, jobCategory)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, jobCategories
        )
        activityAddJobsBinding!!.spCategory.adapter = adapter
        activityAddJobsBinding!!.spCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    jobCategoryId =
                        (parent.getItemAtPosition(position) as JobCategory).jobCategoryId
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        activityAddJobsBinding!!.spCategory.setSelection(0)
    }

    private fun getCities() {
        UiUtil.showProgressDialog(this, getString(R.string.loading))
        getCities(this, onCitiesSuccessListener, CitiesResponse::class.java, onErrorListener)
    }

    private val onCitiesSuccessListener = Response.Listener { response: CitiesResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            setCities(response.returnData)
        } else {
            UiUtil.showToast(this@AddJobsActivity, getString(R.string.err_occurred))
        }
    }

    private fun setCities(cities: ArrayList<City>) {
        val city = City()
        city.name = getString(R.string.select_a_city)
        cities.add(0, city)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, cities
        )
        activityAddJobsBinding!!.spCity.adapter = adapter
        activityAddJobsBinding!!.spCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    cityId = (parent.getItemAtPosition(position) as City).cityid
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        activityAddJobsBinding!!.spCity.setSelection(0)
    }

    private fun bindCallbacks() {
        activityAddJobsBinding!!.btAddJob.setOnClickListener { view: View? ->
            if (alJobs.size != 10) {
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val v = layoutInflater.inflate(
                    R.layout.layout_job,
                    activityAddJobsBinding!!.llContainer,
                    false
                )
                val b = v.findViewById<Button>(R.id.bt_remove)
                /*int index = alJobs.size();
            TextView tvTitle = v.findViewById(R.id.tv_job_title);
            tvTitle.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_title)));
            TextView tvRequirement = v.findViewById(R.id.tv_job_requirement);
            tvRequirement.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_requirement)));*/b.setOnClickListener { view1: View? ->
                    alJobs.remove(v)
                    activityAddJobsBinding!!.llContainer.removeView(v)
                }
                alJobs.add(v)
                activityAddJobsBinding!!.llContainer.addView(v, params)
            } else UiUtil.showToast(this, getString(R.string.no_more_jobs))
        }

        activityAddJobsBinding?.btAddLocation?.setOnClickListener {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) {
            val errorMessage = validateErrorMessage()
            if (errorMessage == 0) {
                submit()
            } else UiUtil.showDialog(this, getString(errorMessage), true)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submit() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val jobRequest = JobRequest()
        jobRequest.dealingIn = activityAddJobsBinding!!.etDealingIn.text.toString()
        jobRequest.jobLocation = activityAddJobsBinding!!.etJobLocation.text.toString()
        jobRequest.howToContact = activityAddJobsBinding!!.etContact.text.toString()
        jobRequest.organisationName = activityAddJobsBinding!!.etCompanyName.text.toString()
        jobRequest.cityID = cityId
        jobRequest.latitude = latLng!!.latitude.toString()
        jobRequest.longitude = latLng!!.latitude.toString()
        val alPosts = ArrayList<Post>()
        for (v in alJobs) {
            val post = Post()
            post.jobCategoryId = jobCategoryId
            val etJobTitle = v.findViewById<EditText>(R.id.et_job_title)
            val etJobRequirement = v.findViewById<EditText>(R.id.et_job_requirement)
            post.jobTitle = etJobTitle.text.toString()
            post.requirement = AppUtil.deNull(etJobRequirement.text)
            alPosts.add(post)
        }
        jobRequest.jobs = alPosts
        addJobs(
            this,
            Gson().toJson(jobRequest),
            onAddJobSuccessListener,
            AddResponse::class.java,
            onErrorListener
        )
    }

    private val onAddJobSuccessListener = Response.Listener { response: AddResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.isStatus) {
            UiUtil.showToast(this, AppUtil.deNull(response.message))
            refreshData()
            //            finish();
        } else UiUtil.showToast(this, getString(R.string.err_occurred))
    }

    private fun refreshData() {
        activityAddJobsBinding!!.llContainer.removeAllViews()
        alJobs.clear()
        activityAddJobsBinding!!.spCategory.setSelection(0)
    }

    private fun validateErrorMessage(): Int {
        var errorMessage = 0
        if (TextUtils.isEmpty(jobCategoryId)) errorMessage =
            R.string.select_a_category else if (TextUtils.isEmpty(
                activityAddJobsBinding!!.etCompanyName.text
            )
        ) errorMessage = R.string.enter_company_name else if (TextUtils.isEmpty(
                activityAddJobsBinding!!.etDealingIn.text
            )
        ) errorMessage = R.string.enter_dealing_in else if (TextUtils.isEmpty(
                activityAddJobsBinding!!.etJobLocation.text
            )
        ) errorMessage = R.string.enter_job_location else if (TextUtils.isEmpty(
                activityAddJobsBinding!!.etContact.text
            )
        ) errorMessage = R.string.enter_contact
        else if (latLng == null) errorMessage =
            R.string.add_a_location
        else if (TextUtils.isEmpty(cityId)) errorMessage =
            R.string.select_a_city else if (alJobs.isEmpty()) errorMessage =
            R.string.add_one_job else {
            for (v in alJobs) {
                val etJobTitle = v.findViewById<EditText>(R.id.et_job_title)
                //                EditText etJobRequirement = v.findViewById(R.id.et_job_requirement);
                if (TextUtils.isEmpty(etJobTitle.text)) {
                    errorMessage = R.string.enter_job_title
                    break
                }
                /*else if (TextUtils.isEmpty(etJobRequirement.getText())) {
                    errorMessage = R.string.enter_job_requirement;
                    break;
                }*/
            }
        }
        return errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                activityAddJobsBinding!!.tvLocation?.text = data?.getStringExtra(LOCATION_ADDRESS)
                latLng = LatLng(
                    data?.getDoubleExtra(LATITUDE, 0.0) ?: 0.0, data?.getDoubleExtra(
                        LONGITUDE, 0.0
                    ) ?: 0.0
                )
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }
}