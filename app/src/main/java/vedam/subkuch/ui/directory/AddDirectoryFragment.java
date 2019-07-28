package vedam.subkuch.ui.directory;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import vedam.subkuch.network.models.CountriesResponse;
import vedam.subkuch.network.models.Country;
import vedam.subkuch.network.models.SubCategory;
import vedam.subkuch.ui.directory.models.AddBusinessRequest;
import vedam.subkuch.ui.directory.models.BusinessAddress;
import vedam.subkuch.ui.directory.models.Category;
import vedam.subkuch.ui.directory.models.CategoryResponse;
import vedam.subkuch.ui.directory.models.SubCategoryResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.ui.jobs.models.CitiesResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDirectoryFragment extends BaseAddImagesFragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddDirectoryBinding fragmentAddDirectoryBinding;
    private ArrayList<View> alBranches = new ArrayList<>();
    private String categoryId;
    private String subcategoryId;
    private String cityId;
    private String countryId;
    private View viewTappedForLocation;

    public AddDirectoryFragment() {
        // Required empty public constructor
    }


    public static AddDirectoryFragment newInstance() {

        return new AddDirectoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            categoryId = getArguments().getString(Constants.EXTRA_CATEGORY_ID);
//            subcategoryId = getArguments().getString(Constants.EXTRA_SUB_CATEGORY_ID);
//        }
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
        getCategories();
        getCountries();
        getCities();
        addBranch();
    }

    private void getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getCategories(context, onCategorySuccessListener, CategoryResponse.class, onErrorListener);

    }

    private Response.Listener<CategoryResponse> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getStatus().equals(Constants.TRUE)) {
                setCategories(response.getCategoryResult().getCategories());
            } else
                UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void setCategories(ArrayList<Category> categories) {

        Category category = new Category();
        category.setName(getString(R.string.select_a_category));
        categories.add(0, category);

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, categories);
        fragmentAddDirectoryBinding.spCategory.setAdapter(adapter);
        fragmentAddDirectoryBinding.spCategory.setOnItemSelectedListener(this);
        fragmentAddDirectoryBinding.spCategory.setSelection(0);
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        DataFetcher.getSubCategories(context, onSubCategorySuccessListener, SubCategoryResponse.class, onErrorListener, categoryId);

    }

    private Response.Listener<SubCategoryResponse> onSubCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getStatus().equals(Constants.TRUE)) {
                setSubcategories(response.getSubCategoryResult().getSubCategories());
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void setSubcategories(ArrayList<SubCategory> subCategories) {

        SubCategory subCategory = new SubCategory();
        subCategory.setSubCategoryName(getString(R.string.select_a_sub_category));
        subCategories.add(0, subCategory);

        ArrayAdapter<SubCategory> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, subCategories);
        fragmentAddDirectoryBinding.spSubCategory.setAdapter(adapter);
        fragmentAddDirectoryBinding.spSubCategory.setOnItemSelectedListener(this);
        fragmentAddDirectoryBinding.spSubCategory.setSelection(0);
    }

    private void getCities() {

        UiUtil.showProgressDialog(context, getString(R.string.loading));
        DataFetcher.getCities(context, onCitiesSuccessListener, CitiesResponse.class, onErrorListener);
    }

    private Response.Listener<CitiesResponse> onCitiesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCities(response.getReturnData());
            } else {
                UiUtil.showToast(context, getString(R.string.err_occurred));
            }


    };

    private void setCities(ArrayList<City> cities) {

        City city = new City();
        city.setName(getString(R.string.select_a_city));
        cities.add(0, city);

        ArrayAdapter<City> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, cities);
        fragmentAddDirectoryBinding.spCity.setAdapter(adapter);
        fragmentAddDirectoryBinding.spCity.setOnItemSelectedListener(this);
        fragmentAddDirectoryBinding.spCity.setSelection(0);
    }

    private void getCountries() {

        UiUtil.showProgressDialog(context, getString(R.string.loading));
        DataFetcher.getCountries(context, onCountriesSuccessListener, CountriesResponse.class, onErrorListener);
    }

    private Response.Listener<CountriesResponse> onCountriesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCountries(response.getCountries());
            } else {
                UiUtil.showToast(context, getString(R.string.err_occurred));
            }
    };

    private void setCountries(ArrayList<Country> countries) {

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, countries);
        fragmentAddDirectoryBinding.spCountry.setAdapter(adapter);
        fragmentAddDirectoryBinding.spCountry.setOnItemSelectedListener(this);
        fragmentAddDirectoryBinding.spCountry.setSelection(getIndexOfIndia(countries));

    }

    private int getIndexOfIndia(ArrayList<Country> countries) {

        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getName().equalsIgnoreCase("India"))
                return i;
        }
        return 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.done, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
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
            if (alBranches.size() != 20) {
                addBranch();
            } else
                UiUtil.showToast(context, getString(R.string.no_more_branches));
        });

    }

    private void addBranch() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View v = getLayoutInflater().inflate(R.layout.layout_branch, fragmentAddDirectoryBinding.llContainer, false);
        Button b = v.findViewById(R.id.bt_remove);
        if (!alBranches.isEmpty())
            b.setOnClickListener(view1 -> {
                alBranches.remove(v);
                fragmentAddDirectoryBinding.llContainer.removeView(v);
            });
        else
            b.setVisibility(View.GONE);

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
    }

    private void submit() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));

        AddBusinessRequest addBusinessRequest = new AddBusinessRequest();
        addBusinessRequest.setCategoryID(categoryId);
        addBusinessRequest.setSubCategoryID(subcategoryId);
        addBusinessRequest.setCountryid(countryId);
        addBusinessRequest.setCityid(cityId);
        addBusinessRequest.setBusinessName(fragmentAddDirectoryBinding.etBusinessName.getText().toString());
        addBusinessRequest.setWebsite(fragmentAddDirectoryBinding.etWebsite.getText().toString());
        if (!getImageItemMap().isEmpty())
            addBusinessRequest.setBusinessImage(AppUtil.getBase64FromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap())));

        ArrayList<BusinessAddress> alBusinessAddresses = new ArrayList<>();
        for (View v : alBranches) {
            BusinessAddress businessAddress = new BusinessAddress();
            EditText etDealingIn = v.findViewById(R.id.et_dealing_in);
            EditText etAddress = v.findViewById(R.id.et_address);
            EditText etPhone = v.findViewById(R.id.et_phone);
            EditText etCellPhone1 = v.findViewById(R.id.et_cellphone_1);
            EditText etCellPhone2 = v.findViewById(R.id.et_cellphone_2);
            EditText etEmail = v.findViewById(R.id.et_email);
            EditText etContactPerson = v.findViewById(R.id.et_contact_person);
            EditText etInfo1 = v.findViewById(R.id.et_info_1);
            EditText etInfo2 = v.findViewById(R.id.et_info_2);
            EditText etZipCode = v.findViewById(R.id.et_zip_code);

//            businessAddress.setAddress1(etAddressLine1.getText().toString());
//            businessAddress.setAddress2(AppUtil.deNull(etAddressLine2.getText().toString()));
            businessAddress.setDealingIn(AppUtil.deNull(etDealingIn.getText().toString()));
            businessAddress.setAddress(etAddress.getText().toString());
            businessAddress.setZipcode(etZipCode.getText().toString());
            businessAddress.setPhoneNo(AppUtil.deNull(etPhone.getText().toString()));
            businessAddress.setMobile1(AppUtil.deNull(etCellPhone1.getText().toString()));
            businessAddress.setMobile2(AppUtil.deNull(etCellPhone2.getText().toString()));
            businessAddress.setEmail(etEmail.getText().toString());
            businessAddress.setContactPerson(etContactPerson.getText().toString());
            businessAddress.setInfoLine1(AppUtil.deNull(etInfo1.getText().toString()));
            businessAddress.setInfoLine2(AppUtil.deNull(etInfo2.getText().toString()));

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
        if (getActivity() != null)
            if (response != null && response.isStatus()) {
                UiUtil.showToast(context, getString(R.string.business_added));
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(categoryId))
            errorMessage = R.string.select_a_category;
        else if (TextUtils.isEmpty(subcategoryId))
            errorMessage = R.string.select_a_sub_category;
        else if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(fragmentAddDirectoryBinding.etBusinessName.getText()))
            errorMessage = R.string.enter_business_name;
//        else if (getImageItemMap().isEmpty())
//            errorMessage = R.string.select_an_image;
        else if (alBranches.isEmpty())
            errorMessage = R.string.add_one_branch;
        else {
            for (View v : alBranches) {
                EditText etDealingIn = v.findViewById(R.id.et_dealing_in);
                EditText etAddress = v.findViewById(R.id.et_address);
                EditText etPhone = v.findViewById(R.id.et_phone);
                EditText etCellPhone1 = v.findViewById(R.id.et_cellphone_1);
                EditText etEmail = v.findViewById(R.id.et_email);
                EditText etZipCode = v.findViewById(R.id.et_zip_code);

                if (TextUtils.isEmpty(etAddress.getText()))
                    errorMessage = R.string.enter_address;
                else if (TextUtils.isEmpty(etZipCode.getText()))
                    errorMessage = R.string.enter_zip_code;
//                else if (TextUtils.isEmpty(etPhone.getText()) && TextUtils.isEmpty(etCellPhone1.getText()))
//                    errorMessage = R.string.enter_phone_or_cellphone;
                else if (!TextUtils.isEmpty(etEmail.getText()) && !AppUtil.validateEmail(etEmail.getText().toString()))
                    errorMessage = R.string.enter_valid_email;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_category:
                categoryId = ((Category) parent.getItemAtPosition(position)).getCategoryId();
                if (!TextUtils.isEmpty(categoryId))
                    getSubCategories();
                break;
            case R.id.sp_sub_category:
                subcategoryId = ((SubCategory) parent.getItemAtPosition(position)).getSubCategoryId();
                break;
            case R.id.sp_city:
                cityId = ((City) parent.getItemAtPosition(position)).getCityid();
                break;
            case R.id.sp_country:
                countryId = ((Country) parent.getItemAtPosition(position)).getCountryid();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
