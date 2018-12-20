package vedam.subkuch.ui.directory;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImagesFragment;
import vedam.subkuch.databinding.FragmentAddDirectoryBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.directory.models.AddBusinessRequest;
import vedam.subkuch.ui.directory.models.BusinessAddress;
import vedam.subkuch.ui.jobs.AddResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDirectoryFragment extends BaseAddImagesFragment {

    private FragmentAddDirectoryBinding fragmentAddDirectoryBinding;
    private ArrayList<View> alBranches = new ArrayList<>();
    private String categoryId;
    private String subcategoryId;
    private View viewTappedForLocation;

    public AddDirectoryFragment() {
        // Required empty public constructor
    }


    public static AddDirectoryFragment newInstance(Bundle extras) {

        AddDirectoryFragment fragment = new AddDirectoryFragment();
        fragment.setArguments(extras);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
            subcategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        fragmentAddDirectoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_directory, container, false);
        return fragmentAddDirectoryBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImagesLayout(view, 1);
        bindCallbacks();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                int errorMessage = validateErrorMessage();
                if (errorMessage == 0) {
                    submit();
                } else
                    UiUtil.showDialog(context, getString(errorMessage), true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindCallbacks() {

        fragmentAddDirectoryBinding.btAddBranch.setOnClickListener(view -> {
            if (alBranches.size() != 10) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                View v = getLayoutInflater().inflate(R.layout.layout_branch, fragmentAddDirectoryBinding.llContainer, false);
                Button b = v.findViewById(R.id.bt_remove);
            /*int index = alJobs.size();
            TextView tvTitle = v.findViewById(R.id.tv_job_title);
            tvTitle.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_title)));
            TextView tvRequirement = v.findViewById(R.id.tv_job_requirement);
            tvRequirement.setText(String.format(Locale.US, "%d. %s", (index + 1), getString(R.string.job_requirement)));*/
                b.setOnClickListener(view1 -> {
                    alBranches.remove(v);
                    fragmentAddDirectoryBinding.llContainer.removeView(v);
                });

                Button btLocation = v.findViewById(R.id.bt_add_location);

                btLocation.setOnClickListener(view1 -> {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        viewTappedForLocation = v;
                        startActivityForResult(builder.build(getActivity()), Constants.REQUEST_PLACE_PICKER);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                });

                alBranches.add(v);
                fragmentAddDirectoryBinding.llContainer.addView(v, params);
            } else
                UiUtil.showToast(context, getString(R.string.no_more_jobs));
        });

    }

    private void submit() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));

        AddBusinessRequest addBusinessRequest = new AddBusinessRequest();
        addBusinessRequest.setCategoryID(categoryId);
        addBusinessRequest.setSubCategoryID(subcategoryId);
        addBusinessRequest.setBusinessName(fragmentAddDirectoryBinding.etBusinessName.getText().toString());
        addBusinessRequest.setPhone(AppUtil.deNull(fragmentAddDirectoryBinding.etPhone.getText().toString()));
        addBusinessRequest.setMobile(fragmentAddDirectoryBinding.etMobile.getText().toString());
        addBusinessRequest.setEmail(fragmentAddDirectoryBinding.etEmail.getText().toString());
        addBusinessRequest.setWebsite(fragmentAddDirectoryBinding.etWebsite.getText().toString());
        addBusinessRequest.setContactPerson(fragmentAddDirectoryBinding.etContactPerson.getText().toString());
        addBusinessRequest.setBusinessImage(AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap())));

        ArrayList<BusinessAddress> alBusinessAddresses = new ArrayList<>();
        for (View v : alBranches) {
            BusinessAddress businessAddress = new BusinessAddress();
            EditText etAddressLine1 = v.findViewById(R.id.et_address_1);
            EditText etAddressLine2 = v.findViewById(R.id.et_address_2);
            EditText etCity = v.findViewById(R.id.et_city);
            EditText etState = v.findViewById(R.id.et_state);
            EditText etCountry = v.findViewById(R.id.et_country);
            EditText etZipCode = v.findViewById(R.id.et_zip_code);

//            businessAddress.setAddress1(etAddressLine1.getText().toString());
//            businessAddress.setAddress2(AppUtil.deNull(etAddressLine2.getText().toString()));
            businessAddress.setCity(etCity.getText().toString());
            businessAddress.setState(etState.getText().toString());
            businessAddress.setCountry(etCountry.getText().toString());
            businessAddress.setZipcode(etZipCode.getText().toString());

            LatLng latLng = (LatLng) v.getTag();
            businessAddress.setLatitude(String.valueOf(latLng.latitude));
            businessAddress.setLongitude(String.valueOf(latLng.longitude));

            alBusinessAddresses.add(businessAddress);
        }
        addBusinessRequest.setBusinessAddresses(alBusinessAddresses);

        DataFetcher.addBusiness(context, new Gson().toJson(addBusinessRequest), onAddBusinessSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddBusinessSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.isStatus()) {
            UiUtil.showToast(context, AppUtil.deNull(response.getMessage()));
            getActivity().finish();
        } else
            UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(fragmentAddDirectoryBinding.etBusinessName.getText()))
            errorMessage = R.string.enter_business_name;
        else if (TextUtils.isEmpty(fragmentAddDirectoryBinding.etMobile.getText()))
            errorMessage = R.string.enter_mobile1;
        else if (TextUtils.isEmpty(fragmentAddDirectoryBinding.etEmail.getText()))
            errorMessage = R.string.enter_email;
        else if (!AppUtil.validateEmail(fragmentAddDirectoryBinding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (TextUtils.isEmpty(fragmentAddDirectoryBinding.etWebsite.getText()))
            errorMessage = R.string.enter_website;
        else if (getImageItemMap().isEmpty())
            errorMessage = R.string.select_an_image;
        else if (alBranches.isEmpty())
            errorMessage = R.string.add_one_branch;
        else {
            for (View v : alBranches) {
                EditText etAddressLine1 = v.findViewById(R.id.et_address_1);
                EditText etCity = v.findViewById(R.id.et_city);
                EditText etState = v.findViewById(R.id.et_state);
                EditText etCountry = v.findViewById(R.id.et_country);
                EditText etZipCode = v.findViewById(R.id.et_zip_code);

                if (TextUtils.isEmpty(etAddressLine1.getText()))
                    errorMessage = R.string.enter_address;
                else if (TextUtils.isEmpty(etCity.getText()))
                    errorMessage = R.string.enter_city;
                else if (TextUtils.isEmpty(etState.getText()))
                    errorMessage = R.string.enter_state;
                else if (TextUtils.isEmpty(etZipCode.getText()))
                    errorMessage = R.string.enter_zip_code;
                else if (TextUtils.isEmpty(etCountry.getText()))
                    errorMessage = R.string.enter_country;
                else if (v.getTag() == null)
                    errorMessage = R.string.add_a_location_for_a_branch;
            }
        }

        return errorMessage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                TextView tv = viewTappedForLocation.findViewById(R.id.tv_location);
//                EditText etCity = viewTappedForLocation.findViewById(R.id.et_city);
//                EditText etState = viewTappedForLocation.findViewById(R.id.et_state);
//                EditText etCountry = viewTappedForLocation.findViewById(R.id.et_country);
//                EditText etZipCode = viewTappedForLocation.findViewById(R.id.et_zip_code);

                UiUtil.setTextView(tv, place.getName().toString());

                viewTappedForLocation.setTag(place.getLatLng());
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
