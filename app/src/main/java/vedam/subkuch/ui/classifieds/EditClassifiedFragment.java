package vedam.subkuch.ui.classifieds;

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
import androidx.core.os.BundleCompat;
import androidx.databinding.DataBindingUtil;

import vedam.subkuch.network.Response;
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

public class EditClassifiedFragment extends BaseAddImagesFragment implements AdapterView.OnItemSelectedListener {
    private FragmentAddClassifiedBinding binding;
    private String categoryId;
    private String subcategoryId;
    private String successMessage;
    private Classified classified;

    public static EditClassifiedFragment newInstance(Bundle args) {

        EditClassifiedFragment fragment = new EditClassifiedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classified = BundleCompat.getParcelable(getArguments(), Constants.EXTRA_DATA, Classified.class);
            if (classified != null) {
                categoryId = classified.getCategoryId();
                subcategoryId = classified.getSubCategoryId();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_classified, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        installMenu(R.menu.done, item -> {
            if (item.getItemId() != R.id.action_done) return false;
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) submit();
            else UiUtil.showDialog(mContext, getString(errorMessage), true);
            return true;
        });
        setImagesLayout(view, 1);
        bindData();
        disableUI();
        getCategories();
        getCities();
    }

    private void bindData() {
        binding.etTitle.setText(AppUtil.deNull(classified.getTitle()));
        binding.etAbout.setText(AppUtil.deNull(classified.getAbout()));
        binding.etLocality.setText(AppUtil.deNull(classified.getLocality()));
        binding.etContact.setText(AppUtil.deNull(classified.getContact()));
        binding.etDiscount.setText(AppUtil.deNull(classified.getDailyDiscount()));
        binding.etRate.setText(AppUtil.deNull(classified.getRate()));
    }

    private void disableUI() {
        binding.spCity.setFocusable(false);
        binding.spCity.setEnabled(false);
        binding.etLocality.setFocusable(false);
        binding.etLocality.setEnabled(false);
        binding.llShowLocation.setVisibility(View.GONE);
    }

    private void getCategories() {
        UiUtil.showProgressDialog(mContext, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedCategory>>() {
        }.getType();
        DataFetcher.getClassifiedsCategories(mContext, onCategorySuccessListener, type, onErrorListener);

    }

    private Response.Listener<ClassifiedResponse<ClassifiedCategory>> onCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCategories(response.getReturnData());
            } else
                UiUtil.showToast(mContext, getString(R.string.no_data));
    };

    private void setCategories(ArrayList<ClassifiedCategory> categories) {

        ClassifiedCategory category = new ClassifiedCategory();
        category.setCategory(getString(R.string.select_a_category));
        categories.add(0, category);

        ArrayAdapter<ClassifiedCategory> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, categories);
        binding.spCategory.setAdapter(adapter);
        binding.spCategory.setOnItemSelectedListener(this);
        setSelectedCategory(categories);
    }

    private void setSelectedCategory(ArrayList<ClassifiedCategory> categories) {

        for (int i = 0; i < categories.size(); i++) {
            ClassifiedCategory category = categories.get(i);
            if (AppUtil.deNull(classified.getCategoryId()).equals(category.getCategoryId()))
                binding.spCategory.setSelection(i);
        }
    }

    private void getSubCategories() {
        UiUtil.showProgressDialog(mContext, R.string.please_wait);
        Type type = new TypeToken<ClassifiedResponse<ClassifiedSubCategory>>() {
        }.getType();
        DataFetcher.getClassifiedSubCategories(mContext, onSubCategorySuccessListener, type, onErrorListener, categoryId);

    }

    private Response.Listener<ClassifiedResponse<ClassifiedSubCategory>> onSubCategorySuccessListener = response -> {
        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setSubcategories(response.getReturnData());
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void setSubcategories(ArrayList<ClassifiedSubCategory> subCategories) {

        ClassifiedSubCategory subCategory = new ClassifiedSubCategory();
        subCategory.setSubCategory(getString(R.string.select_a_sub_category));
        subCategories.add(0, subCategory);

        ArrayAdapter<ClassifiedSubCategory> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, subCategories);
        binding.spSubCategory.setAdapter(adapter);
        binding.spSubCategory.setOnItemSelectedListener(this);
        setSelectedSubCategory(subCategories);
    }

    private void setSelectedSubCategory(ArrayList<ClassifiedSubCategory> subCategories) {

        for (int i = 0; i < subCategories.size(); i++) {
            ClassifiedSubCategory subCategory = subCategories.get(i);
            if (AppUtil.deNull(classified.getSubCategoryId()).equals(subCategory.getSubCategoryId()))
                binding.spSubCategory.setSelection(i);
        }
    }

    private void getCities() {

        UiUtil.showProgressDialog(mContext, getString(R.string.loading));
        DataFetcher.getCities(mContext, onCitiesSuccessListener, CitiesResponse.class, onErrorListener);
    }

    private Response.Listener<CitiesResponse> onCitiesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCities(response.getReturnData());
            } else {
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
            }


    };

    private void setCities(ArrayList<City> cities) {

        City city = new City();
        city.setName(getString(R.string.select_a_city));
        cities.add(0, city);

        ArrayAdapter<City> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, cities);
        binding.spCity.setAdapter(adapter);
        setSelectedCity(cities);
    }

    private void setSelectedCity(ArrayList<City> cities) {

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            if (AppUtil.deNull(classified.getCityId()).equals(city.getCityid()))
                binding.spCity.setSelection(i);
        }
    }


    private void submit() {

        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));

        Classified classifiedRequest = new Classified();
        classifiedRequest.setClassifiedAdId(classified.getClassifiedAdId());
        classifiedRequest.setCategoryId(categoryId);
        classifiedRequest.setSubCategoryId(subcategoryId);
        classifiedRequest.setUserId(AppPrefs.getPrefsUserId(mContext));
        classifiedRequest.setCityId(classified.getCityId());
        classifiedRequest.setLocality(binding.etLocality.getText().toString());
        classifiedRequest.setRate(binding.etRate.getText().toString());
        classifiedRequest.setAbout(binding.etAbout.getText().toString());
        classifiedRequest.setTitle(binding.etTitle.getText().toString());
        classifiedRequest.setDailyDiscount(binding.etDiscount.getText().toString());
        classifiedRequest.setContact(binding.etContact.getText().toString());

        DataFetcher.updateClassified(mContext, new Gson().toJson(classifiedRequest), onAddClassifiedSuccessListener, AddClassifiedResponse.class, onErrorListener);
    }

    private Response.Listener<AddClassifiedResponse> onAddClassifiedSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                successMessage = response.getReturnMessage();
                isImageAvailable(response.getClassified().getPostedAdId());
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private void isImageAvailable(String classfiedId) {

        if (getImageItemMap().size() > 0)
            uploadClassifiedImage(classfiedId);
        else {
            UiUtil.showToast(mContext, successMessage);
            if (getGlobalFragmentInteractionListener() != null) {
                getGlobalFragmentInteractionListener().finishActivity();
            }
        }
    }

    private void uploadClassifiedImage(String classfiedId) {

        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getSingleBitmap(mContext, getImageItemMap()))
                , NetworkConstants.JPEG_MIME_TYPE));

        DataFetcher.uploadClassifiedImage(mContext, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener, classfiedId);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnCode() == Constants.SUCCESS_RETURN_CODE) {
                UiUtil.showToast(mContext, successMessage);
                getActivity().finish();
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };


    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(categoryId))
            errorMessage = R.string.select_a_category;
        else if (TextUtils.isEmpty(subcategoryId))
            errorMessage = R.string.select_a_sub_category;
        else if (TextUtils.isEmpty(binding.etLocality.getText()))
            errorMessage = R.string.enter_location;
        else if (TextUtils.isEmpty(binding.etTitle.getText()))
            errorMessage = R.string.enter_title;
        else if (TextUtils.isEmpty(binding.etRate.getText()))
            errorMessage = R.string.enter_rate;

        return errorMessage;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.sp_category) {
            categoryId = ((ClassifiedCategory) parent.getItemAtPosition(position)).getCategoryId();
            if (!TextUtils.isEmpty(categoryId)) {
                subcategoryId = null;
                getSubCategories();
            }
        } else if (parent.getId() == R.id.sp_sub_category) {
            subcategoryId = ((ClassifiedSubCategory) parent.getItemAtPosition(position)).getSubCategoryId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
