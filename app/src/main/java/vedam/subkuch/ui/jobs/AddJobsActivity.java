package vedam.subkuch.ui.jobs;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
import vedam.subkuch.databinding.ActivityAddJobsBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class AddJobsActivity extends BaseActivity {

    private ActivityAddJobsBinding activityAddJobsBinding;
    private String jobCategoryId;
    private ArrayList<View> alJobs = new ArrayList<>();
    private LatLng latLng;
    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddJobsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_jobs);
        setToolbarBackButton();
        setTitle(R.string.add_job);

//        jobCategoryId = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_ID);
        getJobCategory();
        getCities();
        bindCallbacks();
        requestLocation();
    }

    private void getJobCategory() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        DataFetcher.getJobsCategory(this, onJobCategorySuccessListener, JobCategoryResponse.class, onErrorListener);
    }

    private Response.Listener<JobCategoryResponse> onJobCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getStatus().equals(Constants.TRUE)) {
            setJobCategories(response.getJobCategoriesResult().getJobCategories());
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private void setJobCategories(ArrayList<JobCategory> jobCategories) {

        JobCategory jobCategory = new JobCategory();
        jobCategory.setJobCategoryName(getString(R.string.select_a_category));
        jobCategories.add(0, jobCategory);

        ArrayAdapter<JobCategory> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jobCategories);
        activityAddJobsBinding.spCategory.setAdapter(adapter);
        activityAddJobsBinding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobCategoryId = ((JobCategory) parent.getItemAtPosition(position)).getJobCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityAddJobsBinding.spCity.setSelection(0);
    }

    private void getCities() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getCities(this, onCitiesSuccessListener, CitiesResponse.class, onErrorListener);
    }

    private Response.Listener<CitiesResponse> onCitiesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            setCities(response.getReturnData());
        } else {
            UiUtil.showToast(AddJobsActivity.this, getString(R.string.err_occurred));
        }


    };

    private void setCities(ArrayList<City> cities) {

        City city = new City();
        city.setName(getString(R.string.select_a_city));
        cities.add(0, city);

        ArrayAdapter<City> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cities);
        activityAddJobsBinding.spCity.setAdapter(adapter);
        activityAddJobsBinding.spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityId = ((City) parent.getItemAtPosition(position)).getCityid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityAddJobsBinding.spCity.setSelection(0);

    }

    private void bindCallbacks() {

        activityAddJobsBinding.btAddJob.setOnClickListener(view -> {
            if (alJobs.size() != 10) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                View v = getLayoutInflater().inflate(R.layout.layout_job, activityAddJobsBinding.llContainer, false);
                Button b = v.findViewById(R.id.bt_remove);
            /*int index = alJobs.size();
            TextView tvTitle = v.findViewById(R.id.tv_job_title);
            tvTitle.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_title)));
            TextView tvRequirement = v.findViewById(R.id.tv_job_requirement);
            tvRequirement.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_requirement)));*/
                b.setOnClickListener(view1 -> {
                    alJobs.remove(v);
                    activityAddJobsBinding.llContainer.removeView(v);
                });
                alJobs.add(v);
                activityAddJobsBinding.llContainer.addView(v, params);
            } else
                UiUtil.showToast(this, getString(R.string.no_more_jobs));
        });

        activityAddJobsBinding.btAddLocation.setOnClickListener(view -> {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                int errorMessage = validateErrorMessage();
                if (errorMessage == 0) {
                    submit();
                } else
                    UiUtil.showDialog(this, getString(errorMessage), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submit() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));

        JobRequest jobRequest = new JobRequest();
        jobRequest.setDealingIn(activityAddJobsBinding.etDealingIn.getText().toString());
        jobRequest.setJobLocation(activityAddJobsBinding.etJobLocation.getText().toString());
        jobRequest.setHowToContact(activityAddJobsBinding.etContact.getText().toString());
        jobRequest.setOrganisationName(activityAddJobsBinding.etCompanyName.getText().toString());
        jobRequest.setCityID(cityId);
        jobRequest.setLatitude(String.valueOf(latLng.latitude));
        jobRequest.setLongitude(String.valueOf(latLng.longitude));

        ArrayList<Post> alPosts = new ArrayList<>();
        for (View v : alJobs) {
            Post post = new Post();
            post.setJobCategoryId(jobCategoryId);
            EditText etJobTitle = v.findViewById(R.id.et_job_title);
            EditText etJobRequirement = v.findViewById(R.id.et_job_requirement);
            post.setJobTitle(etJobTitle.getText().toString());
            post.setRequirement(etJobRequirement.getText().toString());
            alPosts.add(post);
        }
        jobRequest.setJobs(alPosts);

        DataFetcher.addJobs(this, new Gson().toJson(jobRequest), onAddJobSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddJobSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.isStatus()) {
            UiUtil.showToast(this, AppUtil.deNull(response.getMessage()));
//            finish();
        } else
            UiUtil.showToast(this, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(jobCategoryId))
            errorMessage = R.string.select_a_category;
        if (TextUtils.isEmpty(activityAddJobsBinding.etCompanyName.getText()))
            errorMessage = R.string.enter_company_name;
        else if (TextUtils.isEmpty(activityAddJobsBinding.etDealingIn.getText()))
            errorMessage = R.string.enter_dealing_in;
        else if (TextUtils.isEmpty(activityAddJobsBinding.etJobLocation.getText()))
            errorMessage = R.string.enter_job_location;
        else if (TextUtils.isEmpty(activityAddJobsBinding.etContact.getText()))
            errorMessage = R.string.enter_contact;
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (latLng == null)
            errorMessage = R.string.add_a_location;
        else if (alJobs.isEmpty())
            errorMessage = R.string.add_one_job;
        else {
            for (View v : alJobs) {
                EditText etJobTitle = v.findViewById(R.id.et_job_title);
                EditText etJobRequirement = v.findViewById(R.id.et_job_requirement);
                if (TextUtils.isEmpty(etJobTitle.getText())) {
                    errorMessage = R.string.enter_job_title;
                    break;
                } else if (TextUtils.isEmpty(etJobRequirement.getText())) {
                    errorMessage = R.string.enter_job_requirement;
                    break;
                }
            }
        }

        return errorMessage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                activityAddJobsBinding.tvLocation.setText(place.getName());
                latLng = place.getLatLng();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
