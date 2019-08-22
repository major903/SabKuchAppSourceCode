package vedam.subkuch.ui.classifieds;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

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
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImagesFragment;
import vedam.subkuch.databinding.FragmentAddClassifiedBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.DataPart;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.network.models.classifieds.AddClassifiedResponse;
import vedam.subkuch.network.models.classifieds.Classified;
import vedam.subkuch.network.models.classifieds.ClassifiedCategory;
import vedam.subkuch.network.models.classifieds.ClassifiedResponse;
import vedam.subkuch.network.models.classifieds.ClassifiedSubCategory;
import vedam.subkuch.ui.jobs.models.CitiesResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;

public class AddClassifiedFragment extends BaseAddImagesFragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddClassifiedBinding binding;
    private LatLng latLng;
    private String categoryId;
    private String subcategoryId;
    private String cityId;
    private String successMessage;

    public static AddClassifiedFragment newInstance() {

        return new AddClassifiedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for context fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_classified, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImagesLayout(view, 1);
        getCategories();
        getCities();
        bindCallbacks();
    }

    private void getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedCategory>>() {
        }.getType();
        DataFetcher.getClassifiedsCategories(context, onCategorySuccessListener, type, onErrorListener);

    }

    private Response.Listener<ClassifiedResponse<ClassifiedCategory>> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCategories(response.getReturnData());
            } else
                UiUtil.showToast(context, getString(R.string.no_data));
    };

    private void setCategories(ArrayList<ClassifiedCategory> categories) {

        ClassifiedCategory category = new ClassifiedCategory();
        category.setCategory(getString(R.string.select_a_category));
        categories.add(0, category);

        ArrayAdapter<ClassifiedCategory> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, categories);
        binding.spCategory.setAdapter(adapter);
        binding.spCategory.setOnItemSelectedListener(this);
        binding.spCategory.setSelection(0);
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedSubCategory>>() {
        }.getType();
        DataFetcher.getClassifiedSubCategories(context, onSubCategorySuccessListener, type, onErrorListener, categoryId);

    }

    private Response.Listener<ClassifiedResponse<ClassifiedSubCategory>> onSubCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setSubcategories(response.getReturnData());
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void setSubcategories(ArrayList<ClassifiedSubCategory> subCategories) {

        ClassifiedSubCategory subCategory = new ClassifiedSubCategory();
        subCategory.setSubCategory(getString(R.string.select_a_sub_category));
        subCategories.add(0, subCategory);

        ArrayAdapter<ClassifiedSubCategory> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, subCategories);
        binding.spSubCategory.setAdapter(adapter);
        binding.spSubCategory.setOnItemSelectedListener(this);
        binding.spSubCategory.setSelection(0);
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
        binding.spCity.setAdapter(adapter);
        binding.spCity.setOnItemSelectedListener(this);
        binding.spCity.setSelection(0);
    }


    private void bindCallbacks() {

        binding.btAddLocation.setOnClickListener(view -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                if (getActivity() != null)
                    startActivityForResult(builder.build(getActivity()), Constants.REQUEST_PLACE_PICKER);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        });

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));

        Classified classifiedRequest = new Classified();
        classifiedRequest.setCategoryId(categoryId);
        classifiedRequest.setSubCategoryId(subcategoryId);
        classifiedRequest.setUserId(AppPrefs.getPrefsUserId(context));
        classifiedRequest.setCityId(cityId);
        classifiedRequest.setLocality(binding.etLocality.getText().toString());
        classifiedRequest.setRate(binding.etRate.getText().toString());
        classifiedRequest.setAbout(binding.etAbout.getText().toString());
        classifiedRequest.setTitle(binding.etTitle.getText().toString());
        classifiedRequest.setDailyDiscount(binding.etDiscount.getText().toString());
        classifiedRequest.setContact(binding.etContact.getText().toString());
        classifiedRequest.setLatitude(String.valueOf(latLng.latitude));
        classifiedRequest.setLongitude(String.valueOf(latLng.longitude));

        DataFetcher.addClassified(context, new Gson().toJson(classifiedRequest), onAddClassifiedSuccessListener, AddClassifiedResponse.class, onErrorListener);
    }

    private Response.Listener<AddClassifiedResponse> onAddClassifiedSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                successMessage = response.getReturnMessage();
                isImageAvailable(response.getClassified().getPostedAdId());
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void isImageAvailable(String classfiedId) {

        if (getImageItemMap().size() > 0)
            uploadClassifiedImage(classfiedId);
        else {
            UiUtil.showToast(context, successMessage);
            if (getGlobalFragmentInteractionListener() != null) {
                getGlobalFragmentInteractionListener().setFragmentResult(RESULT_OK, null);
                getGlobalFragmentInteractionListener().finishActivity();
            }
        }
    }

    private void uploadClassifiedImage(String classfiedId) {

        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(context, getImageItemMap()))
                , NetworkConstants.JPEG_MIME_TYPE));

        DataFetcher.uploadClassifiedImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener, classfiedId);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                UiUtil.showToast(context, successMessage);
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
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(binding.etLocality.getText()))
            errorMessage = R.string.enter_location;
        else if (TextUtils.isEmpty(binding.etTitle.getText()))
            errorMessage = R.string.enter_title;
        else if (TextUtils.isEmpty(binding.etAbout.getText()))
            errorMessage = R.string.enter_details;
        else if (TextUtils.isEmpty(binding.etRate.getText()))
            errorMessage = R.string.enter_rate;
        else if (TextUtils.isEmpty(binding.etDiscount.getText()))
            errorMessage = R.string.enter_daily_discount;
        else if (TextUtils.isEmpty(binding.etContact.getText()))
            errorMessage = R.string.enter_contact;
        else if (latLng == null)
            errorMessage = R.string.add_a_location;

        return errorMessage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(context, data);
                binding.tvLocation.setText(place.getName());
                latLng = place.getLatLng();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_category:
                categoryId = ((ClassifiedCategory) parent.getItemAtPosition(position)).getCategoryId();
                if (!TextUtils.isEmpty(categoryId)) {
                    subcategoryId = null;
                    getSubCategories();
                }
                break;
            case R.id.sp_sub_category:
                subcategoryId = ((ClassifiedSubCategory) parent.getItemAtPosition(position)).getSubCategoryId();
                break;
            case R.id.sp_city:
                cityId = ((City) parent.getItemAtPosition(position)).getCityid();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
