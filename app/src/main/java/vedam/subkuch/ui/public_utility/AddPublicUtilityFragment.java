package vedam.subkuch.ui.public_utility;


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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adevinta.leku.LocationPickerActivity;
import com.adevinta.leku.LocationPickerActivityKt;
import vedam.subkuch.network.Response;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentAddPublicUtilityBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.CountriesResponse;
import vedam.subkuch.network.models.Country;
import vedam.subkuch.network.models.public_utility.AddPublicUtilityRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.ui.jobs.models.CitiesResponse;
import vedam.subkuch.ui.jobs.models.City;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPublicUtilityFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddPublicUtilityBinding binding;
    private String cityId;
    private String countryId;
    private LatLng latLng;
    private final ActivityResultLauncher<Intent> locationPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), this::handleLocationResult);

    public AddPublicUtilityFragment() {
        // Required empty public constructor
    }


    public static AddPublicUtilityFragment newInstance() {

        return new AddPublicUtilityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_public_utility, container, false);
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
        getCountries();
        getCities();
        bindCallbacks();
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
        binding.spCity.setOnItemSelectedListener(this);
        binding.spCity.setSelection(0);
    }

    private void getCountries() {

        UiUtil.showProgressDialog(mContext, getString(R.string.loading));
        DataFetcher.getCountries(mContext, onCountriesSuccessListener, CountriesResponse.class, onErrorListener);
    }

    private Response.Listener<CountriesResponse> onCountriesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                setCountries(response.getCountries());
            } else {
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
            }
    };

    private void setCountries(ArrayList<Country> countries) {

        ArrayAdapter<Country> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, countries);
        binding.spCountry.setAdapter(adapter);
        binding.spCountry.setOnItemSelectedListener(this);
        binding.spCountry.setSelection(getIndexOfIndia(countries));

    }

    private int getIndexOfIndia(ArrayList<Country> countries) {

        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            if (country.getName().equalsIgnoreCase("India"))
                return i;
        }
        return 0;
    }

    private void bindCallbacks() {

        binding.btAddLocation.setOnClickListener(view1 -> {
            if (getActivity() == null) return;
            Intent locationPickerIntent = new LocationPickerActivity.Builder()
                    .withGeolocApiKey(Constants.MAPS_API_KEY)
                    .withGooglePlacesApiKey(Constants.MAPS_API_KEY)
                    .withDefaultLocaleSearchZone()
                    .shouldReturnOkOnBackPressed()
                    .withStreetHidden()
                    .withCityHidden()
                    .withZipCodeHidden()
                    .withGoogleTimeZoneEnabled()
                    .withVoiceSearchHidden()
                    .build(getActivity());
            locationPickerLauncher.launch(locationPickerIntent);
        });
    }

    private void submit() {

        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));

        AddPublicUtilityRequest addPublicUtilityRequest = new AddPublicUtilityRequest();
        addPublicUtilityRequest.setCountryId(countryId);
        addPublicUtilityRequest.setCityId(cityId);
        addPublicUtilityRequest.setName(binding.etBusinessName.getText().toString());
        addPublicUtilityRequest.setWebsite(binding.etWebsite.getText().toString());
//        addPublicUtilityRequest.setDealingIn(AppUtil.deNull(etDealingIn.getText().toString()));
        addPublicUtilityRequest.setAddress(binding.etAddress.getText().toString());
        addPublicUtilityRequest.setZipcode(binding.etZipCode.getText().toString());
        addPublicUtilityRequest.setPhoneNo(AppUtil.deNull(binding.etPhone.getText().toString()));
        addPublicUtilityRequest.setMobile1(AppUtil.deNull(binding.etCellphone1.getText().toString()));
        addPublicUtilityRequest.setMobile2(AppUtil.deNull(binding.etCellphone2.getText().toString()));
        addPublicUtilityRequest.setEmailId(binding.etEmail.getText().toString());
        addPublicUtilityRequest.setContactPerson(binding.etContactPerson.getText().toString());
        addPublicUtilityRequest.setInfoLine1(AppUtil.deNull(binding.etInfo1.getText().toString()));
        addPublicUtilityRequest.setInfoLine2(AppUtil.deNull(binding.etInfo2.getText().toString()));
        addPublicUtilityRequest.setLatitude(String.valueOf(latLng.latitude));
        addPublicUtilityRequest.setLongitude(String.valueOf(latLng.longitude));

        DataFetcher.addBusiness(mContext, new Gson().toJson(addPublicUtilityRequest), onAddBusinessSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddBusinessSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && !TextUtils.isEmpty(response.getMessage())) {
                UiUtil.showToast(mContext, response.getMessage());
                getActivity().setResult(RESULT_OK);
                getActivity().finish();
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;

        if (TextUtils.isEmpty(countryId))
            errorMessage = R.string.select_a_country;
        else if (TextUtils.isEmpty(cityId))
            errorMessage = R.string.select_a_city;
        else if (TextUtils.isEmpty(binding.etBusinessName.getText()))
            errorMessage = R.string.enter_business_name;
        else if (TextUtils.isEmpty(binding.etAddress.getText()))
            errorMessage = R.string.enter_address;
        else if (TextUtils.isEmpty(binding.etZipCode.getText()))
            errorMessage = R.string.enter_zip_code;
        else if (!TextUtils.isEmpty(binding.etEmail.getText()) && !AppUtil.validateEmail(binding.etEmail.getText().toString()))
            errorMessage = R.string.enter_valid_email;
        else if (latLng == null)
            errorMessage = R.string.add_a_location_for_a_branch;

        return errorMessage;
    }

    private void handleLocationResult(ActivityResult result) {
        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK && data != null) {
            latLng = new LatLng(
                    data.getDoubleExtra(LocationPickerActivityKt.LATITUDE, 0.0),
                    data.getDoubleExtra(LocationPickerActivityKt.LONGITUDE, 0.0));
            UiUtil.setTextView(binding.tvLocation, data.getStringExtra(LocationPickerActivityKt.LOCATION_ADDRESS));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.sp_city) {
            cityId = ((City) parent.getItemAtPosition(position)).getCityid();
        } else if (parent.getId() == R.id.sp_country) {
            countryId = ((Country) parent.getItemAtPosition(position)).getCountryid();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
