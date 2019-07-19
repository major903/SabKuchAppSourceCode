package vedam.subkuch.ui.matrimonial.preference;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentPreferenceBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.models.DrinkingHabits;
import vedam.subkuch.network.models.GetBodyTypeBean;
import vedam.subkuch.network.models.GetCityResponse;
import vedam.subkuch.network.models.GetComplexionBean;
import vedam.subkuch.network.models.GetDoshamBean;
import vedam.subkuch.network.models.GetDrinkingHabits;
import vedam.subkuch.network.models.GetFoodHabitsBean;
import vedam.subkuch.network.models.GetGotrasBean;
import vedam.subkuch.network.models.GetMaritalStatusResponse;
import vedam.subkuch.network.models.GetMothertongueBean;
import vedam.subkuch.network.models.GetNakshatrasBean;
import vedam.subkuch.network.models.GetOccupationBean;
import vedam.subkuch.network.models.GetOwnCarResponse;
import vedam.subkuch.network.models.GetOwnHouseResponse;
import vedam.subkuch.network.models.GetPhysicalStatusBean;
import vedam.subkuch.network.models.GetQualificationBean;
import vedam.subkuch.network.models.GetSmokingResponse;
import vedam.subkuch.network.models.OwnCar;
import vedam.subkuch.network.models.OwnHouse;
import vedam.subkuch.network.models.Smoking;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getPreferencesResponse.GetPreferenceResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.matrimonial.preference.presenter.PerferenceFragmentPresenter;
import vedam.subkuch.ui.matrimonial.preference.presenter.PerferenceFragmentPresenterHandler;
import vedam.subkuch.ui.matrimonial.preference.view.PerferenceFragmentView;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.FrequentFunctions;
import vedam.subkuch.utils.UiUtil;

public class PreferenceFragment extends BaseFragment implements PerferenceFragmentView, ItemAdapter.ItemClickHandler {

    private FragmentPreferenceBinding binding;
    private PerferenceFragmentPresenterHandler mPresenter;
    private GetPreferenceResponse response;

    private String minAge;
    private String maxAge;
    private List<String> items = new ArrayList<>();
    private int selectedMaterCastId = 0;
    private int selectedSubCastId = 0;
    private int selectedReligionId = 0;
    private int selectedLivingId = 0;
    private int selectedCityId = 0;
    private int selectedNakshatraId = 0;
    private int selectedBodyTypeId = 0;
    private int selectedComplexionId = 0;
    private int selectedOccupationId = 0;
    private int selectedQualificationId = 0;
    private int selectedFoodHabitesId = 0;
    private int selectedMothertoungeId = 0;
    private int selectedPhysicalstatusId = 0;
    private int selectedDoshamId = 0;
    private int selectedMaritalStatusId = 0;
    private int selectedDrinkingStatusId = 0;
    private int selectedOwnCarId = 0;
    private int selectedOwnHouseId = 0;
    private int selectedSmokingStatusId = 0;
    private String clickedItem = "";


    private GetMasterCastResponse masterCastResponse;
    private GetCityResponse getCityResponse;
    private GetGotrasBean gotraResponse;
    private GetReligionResponse getReligionResponse;
    private GetLivingResponse getLivingResponse;
    private GetBodyTypeBean getBodyTypeBean;
    private GetComplexionBean getComplexionBean;
    private GetDoshamBean getDoshamBean;
    private GetFoodHabitsBean getFoodHabitsBean;
    private GetMothertongueBean getMothertongueBean;
    private GetNakshatrasBean getNakshatrasBean;
    private GetOccupationBean getOccupationBean;
    private GetPhysicalStatusBean getPhysicalStatusBean;
    private GetQualificationBean getQualificationBean;
    private GetMaritalStatusResponse getMaritalStatusResponse;
    private GetDrinkingHabits getDrinkingHabits;
    private GetOwnCarResponse getOwnCarResponse;
    private GetOwnHouseResponse getOwnHouseResponse;
    private GetSmokingResponse getSmokingResponse;

    private View view;
    private String minDistance;
    private String maxDistance;

    private String minHeight;
    private String maxHeight;

    private String minWeight;
    private String maxWeight;

    private String minIncome;
    private String maxMaxIncome;
    private boolean isDating;

    public static PreferenceFragment newInstance(boolean isDating) {

        Bundle args = new Bundle();
        args.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        PreferenceFragment fragment = new PreferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            isDating = getArguments().getBoolean(Constants.EXTRA_IS_DATING);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_preference, container, false);
        binding.setPreferenceFragment(this);
        setTitle(getString(R.string.preferences));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isDating)
            hideFields();
        init();

    }

    private void hideFields() {
        binding.tvReligionHeading.setVisibility(View.GONE);
        binding.relativeLayoutReligion.setVisibility(View.GONE);
        binding.tvCasteHeading.setVisibility(View.GONE);
        binding.relativeLayoutMasterCast.setVisibility(View.GONE);
        binding.tvGotraHeading.setVisibility(View.GONE);
        binding.relativeLayoutSubCast.setVisibility(View.GONE);
        binding.tvNakshatraHeading.setVisibility(View.GONE);
        binding.relativeLayoutNakshakra.setVisibility(View.GONE);
        binding.tvComplexionHeading.setVisibility(View.GONE);
        binding.relativeLayoutComplexion.setVisibility(View.GONE);
        binding.tvQualificationHeading.setVisibility(View.GONE);
        binding.relativeLayoutQualification.setVisibility(View.GONE);
        binding.tvPhysicalStatusHeading.setVisibility(View.GONE);
        binding.relativeLayoutPhysicalstatus.setVisibility(View.GONE);
        binding.tvDoshamHeading.setVisibility(View.GONE);
        binding.relativeLayoutDosham.setVisibility(View.GONE);
        binding.tvMotherTongueHeading.setVisibility(View.GONE);
        binding.relativeLayoutMothertouge.setVisibility(View.GONE);
        binding.tvLivingWithHeading.setVisibility(View.GONE);
        binding.relativeLayoutLiving.setVisibility(View.GONE);
        binding.tvMaritalStatusHeading.setVisibility(View.GONE);
        binding.relativeLayoutMatrialstatus.setVisibility(View.GONE);
        binding.tvOccupationHeading.setVisibility(View.GONE);
        binding.relativeLayoutOccupation.setVisibility(View.GONE);
        binding.tvFoodHabitHeading.setVisibility(View.GONE);
        binding.relativeLayoutFoodhabits.setVisibility(View.GONE);
        binding.rlWeight.setVisibility(View.GONE);
        binding.rlIncome.setVisibility(View.GONE);
        binding.tvCarHeading.setVisibility(View.GONE);
        binding.relativeOwnCar.setVisibility(View.GONE);
        binding.tvHouseHeading.setVisibility(View.GONE);
        binding.relativeOwnHouse.setVisibility(View.GONE);
    }

    private void init() {

        binding.seekBarAge.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minAge = minValue.toString();
            maxAge = maxValue.toString();
            binding.textViewAge.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });
        binding.seekBarDistance.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minDistance = minValue.toString();
            maxDistance = maxValue.toString();
            binding.textViewDistance.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        binding.seekBarIncome.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minIncome = minValue.toString();
            maxMaxIncome = maxValue.toString();
            binding.textViewIncome.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        binding.seekBarWeight.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minWeight = minValue.toString();
            maxWeight = maxValue.toString();
            binding.textViewWeight.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        binding.seekBarHeight.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minHeight = minValue.toString();
            maxHeight = maxValue.toString();
            binding.textViewHeight.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        mPresenter = new PerferenceFragmentPresenter(this);
        mPresenter.getPerference(AppPrefs.getPrefsUserId(context), isDating);
    }


    @Override
    public void showProgressBar() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
    }

    @Override
    public void hideProgressBar() {
        UiUtil.cancelProgressDialog();
    }

    @Override
    public void showFeedBackMessage(String message) {
        baseshowFeedbackMessage(binding.rootLayout, message);
    }

    @Override
    public void onSuccessfullyGetPreference(GetPreferenceResponse response) {
        this.response = response;
        selectedReligionId = response.getReturnData().getReligionId();
        selectedMaterCastId = response.getReturnData().getCasteId();
        selectedLivingId = response.getReturnData().getLivingWithId();
        selectedSubCastId = response.getReturnData().getGotraid();
        selectedCityId = response.getReturnData().getCityId();
        selectedNakshatraId = response.getReturnData().getNakshakraid();
        selectedBodyTypeId = response.getReturnData().getBodyTypeid();
        selectedComplexionId = response.getReturnData().getComplexionid();
        selectedOccupationId = response.getReturnData().getOccupationid();
        selectedQualificationId = response.getReturnData().getQualificationid();
        selectedFoodHabitesId = response.getReturnData().getFoodHabitsid();
        selectedMothertoungeId = response.getReturnData().getMotherTougeid();
        selectedPhysicalstatusId = response.getReturnData().getPhysicalStatusid();
        selectedDoshamId = response.getReturnData().getDoshamid();
        selectedMaritalStatusId = response.getReturnData().getMatrialStatusid();
        selectedDrinkingStatusId = response.getReturnData().getDrinkingStatusid();
        selectedOwnCarId = response.getReturnData().getOwnCarId();
        selectedOwnHouseId = response.getReturnData().getOwnHouseId();
        selectedSmokingStatusId = response.getReturnData().getSmokingId();

        setData();
    }

    @Override
    public void onSuccessfullyGetMasterCast(GetMasterCastResponse response) {
        masterCastResponse = response;
        response.getReturnData().add(0, new GetMasterCastResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMasterCastName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetReligion(GetReligionResponse response) {
        getReligionResponse = response;
        response.getReturnData().add(0, new GetReligionResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getReligionName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetLiving(GetLivingResponse response) {
        getLivingResponse = response;
        response.getReturnData().add(0, new GetLivingResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getLivingWithName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyUpdatePreferences(UpdateMatrimonialResponse response) {

        UiUtil.showToast(getActivity(), getString(R.string.preference_updated));
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onSuccessfullyGetGotra(GetGotrasBean response) {
        gotraResponse = response;
        response.getReturnData().add(0, new GetGotrasBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetComplexion(GetComplexionBean response) {
        getComplexionBean = response;
        response.getReturnData().add(0, new GetComplexionBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getComplexionname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetOccupation(GetOccupationBean response) {
        getOccupationBean = response;
        response.getReturnData().add(0, new GetOccupationBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOccupationname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetQualification(GetQualificationBean response) {
        getQualificationBean = response;
        response.getReturnData().add(0, new GetQualificationBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getQualificationname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetFoodHabits(GetFoodHabitsBean response) {
        getFoodHabitsBean = response;
        response.getReturnData().add(0, new GetFoodHabitsBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getFoodHabitsName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetDrinkingHabits(GetDrinkingHabits response) {
        getDrinkingHabits = response;
        response.getReturnData().add(0, new DrinkingHabits(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDrinkingStatus_Name());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetPhysicalstatus(GetPhysicalStatusBean response) {
        getPhysicalStatusBean = response;
        response.getReturnData().add(0, new GetPhysicalStatusBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getPhysicalStatusName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetDosham(GetDoshamBean response) {
        getDoshamBean = response;
        response.getReturnData().add(0, new GetDoshamBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDoshamName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetNakshatras(GetNakshatrasBean response) {
        getNakshatrasBean = response;
        response.getReturnData().add(0, new GetNakshatrasBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getNakshatraname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetBodyType(GetBodyTypeBean response) {
        getBodyTypeBean = response;
        response.getReturnData().add(0, new GetBodyTypeBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getBodytypename());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetMothertongue(GetMothertongueBean response) {
        getMothertongueBean = response;
        response.getReturnData().add(0, new GetMothertongueBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMothertongueName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetCity(GetCityResponse response) {
        getCityResponse = response;
        response.getReturnData().add(0, new GetCityResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetMaritalStatus(GetMaritalStatusResponse response) {
        getMaritalStatusResponse = response;
        response.getReturnData().add(0, new GetMaritalStatusResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMaritalStatus_Name());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetOwnCar(GetOwnCarResponse response) {
        getOwnCarResponse = response;
        response.getReturnData().add(0, new OwnCar(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOwnCarType());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetOwnHouse(GetOwnHouseResponse response) {
        getOwnHouseResponse = response;
        response.getReturnData().add(0, new OwnHouse(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOwnHouseType());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetSmoking(GetSmokingResponse response) {
        getSmokingResponse = response;
        response.getReturnData().add(0, new Smoking(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getSmokingType());
        }
        initializeAdapter();
    }

    private void setData() {

        setText(binding.textViewReligion, response.getReturnData().getReligionName());
        setText(binding.textViewCast, response.getReturnData().getMasterCastName());
        setText(binding.textViewLivingWith, response.getReturnData().getLivingWithName());
        setText(binding.textViewSubCast, response.getReturnData().getGotraName());
        setText(binding.textViewBodytype, response.getReturnData().getBodyTypeName());
        setText(binding.textViewCity, response.getReturnData().getCityName());
        setText(binding.textViewComplexion, response.getReturnData().getComplexionName());
        setText(binding.textViewDosham, response.getReturnData().getDoshamName());
        setText(binding.textViewFoodhabits, response.getReturnData().getFoodHabitsName());
        setText(binding.textViewMothertouge, response.getReturnData().getMothertongueName());
        setText(binding.textViewNakshatra, response.getReturnData().getNakshatraName());
        setText(binding.textViewOccupation, response.getReturnData().getOccupationName());
        setText(binding.textViewPhysicalstatus, response.getReturnData().getPhysicalStatusName());
        setText(binding.textViewQualification, response.getReturnData().getQualificationName());
        setText(binding.textViewMatrialstatus, response.getReturnData().getMaritalStatusName());
        setText(binding.textViewDrinkingHabits, response.getReturnData().getDrinkingStatusName());
        setText(binding.textViewOwnCar, response.getReturnData().getOwnCarType());
        setText(binding.textViewOwnHouse, response.getReturnData().getOwnHouseType());
        setText(binding.textViewSmoking, response.getReturnData().getSmokingType());


       /* if (!response.getReturnData().getFromLocation().isEmpty()&&!response.getReturnData().getToLocation().isEmpty()){
            seekBarDistance.setMinStartValue(Float.parseFloat(response.getReturnData().getFromLocation())).setMaxStartValue(Float.parseFloat(response.getReturnData().getToLocation())).apply();
        }*/
        binding.seekBarAge.setMinStartValue(Float.parseFloat(response.getReturnData().getMinAge())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxAge())).apply();
        binding.seekBarDistance.setMinStartValue(Float.parseFloat(response.getReturnData().getMinDistance())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxDistance())).apply();
        binding.seekBarWeight.setMinStartValue(Float.parseFloat(response.getReturnData().getMinWeight())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxWeight())).apply();
        binding.seekBarHeight.setMinStartValue(Float.parseFloat(response.getReturnData().getMinHeight())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxHeight())).apply();
        binding.seekBarIncome.setMinStartValue(Float.parseFloat(response.getReturnData().getMinIncome())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxIncome())).apply();
    }

    private void setText(TextView tv, String value) {
        if (AppUtil.deNull(value).isEmpty())
            tv.setText(getString(R.string.any));
        else
            tv.setText(value);
    }

    public void relativeLayoutLivingClick(View view) {
        clickedItem = "livingWith";
        this.view = view;
        mPresenter.getLiving();
    }

    public void relativeLayoutReligionClick(View view) {
        clickedItem = "religion";
        this.view = view;
        mPresenter.getReligion();
    }

    public void relativeLayoutCityClick(View view) {
        clickedItem = "city";
        this.view = view;
        mPresenter.getAllCity();
    }

    public void relativeLayoutMatrialstatusClick(View view) {
        clickedItem = "matrialstatus";
        this.view = view;
        mPresenter.getMatrialStatus();
    }

    public void relativeLayoutMasterCastClick(View view) {
        clickedItem = "masterCast";
        this.view = view;
        mPresenter.getMasterCast(selectedReligionId + "");
    }


    public void relativeLayoutSubCastClick(View view) {
        if (selectedMaterCastId == 0) {
            baseshowFeedbackMessage(view, getString(R.string.select_master_cast_first));
        } else {
            clickedItem = "subCast";
            this.view = view;
            mPresenter.getGotra(selectedMaterCastId + "");
        }
    }

    public void relativeLayoutNakshakraClick(View view) {
        clickedItem = "nakshakra";
        this.view = view;
        mPresenter.getNakshakra();
    }

    public void relativeLayoutMothertougeClick(View view) {
        clickedItem = "mothertouge";
        this.view = view;
        mPresenter.getMothertouge();
    }


    public void relativeLayoutBodytypeClick(View view) {
        clickedItem = "bodytype";
        this.view = view;
        mPresenter.getBodytype();
    }

    public void relativeLayoutComplexionClick(View view) {
        clickedItem = "complexion";
        this.view = view;
        mPresenter.getComplexion();
    }

    public void relativeLayoutOccupationClick(View view) {
        clickedItem = "occupation";
        this.view = view;
        mPresenter.getOccupation();
    }

    public void relativeLayoutQualificationClick(View view) {
        clickedItem = "qualification";
        this.view = view;
        mPresenter.getQualification();
    }

    public void relativeLayoutFoodhabitsClick(View view) {
        clickedItem = "foodhabits";
        this.view = view;
        mPresenter.getFoodhabits();
    }

    public void relativeLayoutDrinkingHabitsClick(View view) {
        clickedItem = "DrinkingHabits";
        this.view = view;
        mPresenter.getDrinkingHabits();
    }

    public void relativeLayoutPhysicalstatusClick(View view) {
        clickedItem = "physicalstatus";
        this.view = view;
        mPresenter.getPhysicalstatus();
    }

    public void relativeLayoutDoshamClick(View view) {
        clickedItem = "dosham";
        this.view = view;
        mPresenter.getDosham();
    }

    public void relativeOwnCarClick(View view) {
        clickedItem = "OwnCar";
        this.view = view;
        mPresenter.getOwnCar();
    }

    public void relativeOwnHouseClick(View view) {
        clickedItem = "OwnHouse";
        this.view = view;
        mPresenter.getOwnHouse();
    }

    public void relativeSmokingClick(View view) {
        clickedItem = "Smoking";
        this.view = view;
        mPresenter.getSmoking();
    }

    public void initializeAdapter() {
        ItemAdapter adapter = new ItemAdapter(items);
        adapter.OnItemClickListener(this);
        showPopWindow(view, adapter);
    }

    @Override
    public void OnItemClick(int position) {
        mPopupWindow.dismiss();
        if (clickedItem.equalsIgnoreCase("masterCast")) {
            selectedMaterCastId = masterCastResponse.getReturnData().get(position).getMasterCasteID();
            binding.textViewCast.setText(masterCastResponse.getReturnData().get(position).getMasterCastName());
            binding.textViewSubCast.setText("");
        } else if (clickedItem.equalsIgnoreCase("subCast")) {
            selectedSubCastId = gotraResponse.getReturnData().get(position).getGotrasid();
            binding.textViewSubCast.setText(gotraResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("religion")) {
            selectedReligionId = getReligionResponse.getReturnData().get(position).getReligionID();
            binding.textViewReligion.setText(getReligionResponse.getReturnData().get(position).getReligionName());
        } else if (clickedItem.equalsIgnoreCase("livingWith")) {
            selectedLivingId = getLivingResponse.getReturnData().get(position).getLivingWithId();
            binding.textViewLivingWith.setText(getLivingResponse.getReturnData().get(position).getLivingWithName());
        } else if (clickedItem.equalsIgnoreCase("mothertouge")) {
            selectedMothertoungeId = getMothertongueBean.getReturnData().get(position).getMothertongueid();
            binding.textViewMothertouge.setText(getMothertongueBean.getReturnData().get(position).getMothertongueName());
        } else if (clickedItem.equalsIgnoreCase("bodytype")) {
            selectedBodyTypeId = getBodyTypeBean.getReturnData().get(position).getBodytypeid();
            binding.textViewBodytype.setText(getBodyTypeBean.getReturnData().get(position).getBodytypename());
        } else if (clickedItem.equalsIgnoreCase("complexion")) {
            selectedComplexionId = getComplexionBean.getReturnData().get(position).getComplexionid();
            binding.textViewComplexion.setText(getComplexionBean.getReturnData().get(position).getComplexionname());
        } else if (clickedItem.equalsIgnoreCase("occupation")) {
            selectedOccupationId = getOccupationBean.getReturnData().get(position).getOccupationid();
            binding.textViewOccupation.setText(getOccupationBean.getReturnData().get(position).getOccupationname());
        } else if (clickedItem.equalsIgnoreCase("qualification")) {
            selectedQualificationId = getQualificationBean.getReturnData().get(position).getQualificationid();
            binding.textViewQualification.setText(getQualificationBean.getReturnData().get(position).getQualificationname());
        } else if (clickedItem.equalsIgnoreCase("foodhabits")) {
            selectedFoodHabitesId = getFoodHabitsBean.getReturnData().get(position).getFoodHabitsid();
            binding.textViewFoodhabits.setText(getFoodHabitsBean.getReturnData().get(position).getFoodHabitsName());
        } else if (clickedItem.equalsIgnoreCase("DrinkingHabits")) {
            selectedDrinkingStatusId = getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Id();
            binding.textViewDrinkingHabits.setText(getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("physicalstatus")) {
            selectedPhysicalstatusId = getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusid();
            binding.textViewPhysicalstatus.setText(getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusName());
        } else if (clickedItem.equalsIgnoreCase("dosham")) {
            selectedDoshamId = getDoshamBean.getReturnData().get(position).getDoshamid();
            binding.textViewDosham.setText(getDoshamBean.getReturnData().get(position).getDoshamName());
        } else if (clickedItem.equalsIgnoreCase("city")) {
            selectedCityId = getCityResponse.getReturnData().get(position).getCityId();
            binding.textViewCity.setText(getCityResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("matrialstatus")) {
            selectedMaritalStatusId = getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Id();
            binding.textViewMatrialstatus.setText(getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("nakshakra")) {
            selectedNakshatraId = getNakshatrasBean.getReturnData().get(position).getNakshatraid();
            binding.textViewNakshatra.setText(getNakshatrasBean.getReturnData().get(position).getNakshatraname());
        } else if (clickedItem.equalsIgnoreCase("OwnCar")) {
            selectedOwnCarId = getOwnCarResponse.getReturnData().get(position).getOwnCarId();
            binding.textViewOwnCar.setText(getOwnCarResponse.getReturnData().get(position).getOwnCarType());
        } else if (clickedItem.equalsIgnoreCase("OwnHouse")) {
            selectedOwnHouseId = getOwnHouseResponse.getReturnData().get(position).getOwnHouseId();
            binding.textViewOwnHouse.setText(getOwnHouseResponse.getReturnData().get(position).getOwnHouseType());
        } else if (clickedItem.equalsIgnoreCase("Smoking")) {
            selectedSmokingStatusId = getSmokingResponse.getReturnData().get(position).getSmokingId();
            binding.textViewSmoking.setText(getSmokingResponse.getReturnData().get(position).getSmokingType());
        }
    }


    public void btnUpdateClick(View view) {
//        if (selectedReligionId == 0) {
//            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_religion));
//        } else if (selectedMaterCastId == 0) {
//            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_cast));
//        } else if (Validations.isFieldEmpty(textViewSubCast.getText().toString())) {
//            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_gothra));
//        } else if (selectedLivingId == 0) {
//            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_living_with));
//        } else {

        FrequentFunctions.hideKeyBoard(context, view);


        MatrimonialRequest matrimonialRequest = new MatrimonialRequest();
        matrimonialRequest.setProfileId(Integer.parseInt(AppPrefs.getPrefsUserId(context)));
        matrimonialRequest.setCityId(selectedCityId);
//            matrimonialRequest.setCountryId(Constants.COUNTRY_ID);
        matrimonialRequest.setReligionId(selectedReligionId);
        matrimonialRequest.setCasteId(selectedMaterCastId);
        matrimonialRequest.setOwnCarId(selectedOwnCarId);
        matrimonialRequest.setOwnHouseId(selectedOwnHouseId);
        matrimonialRequest.setLivingWithId(selectedLivingId);
        matrimonialRequest.setMinHeight(minHeight);
        matrimonialRequest.setMaxHeight(maxHeight);
        matrimonialRequest.setMinWeight(minWeight);
        matrimonialRequest.setMaxWeight(maxWeight);
        matrimonialRequest.setMinAge(minAge);
        matrimonialRequest.setMaxAge(maxAge);
        matrimonialRequest.setMinDistance(minDistance);
        matrimonialRequest.setMaxDistance(maxDistance);
        matrimonialRequest.setMinIncome(Integer.parseInt(minIncome));
        matrimonialRequest.setMaxIncome(Integer.parseInt(maxMaxIncome));
        matrimonialRequest.setGotraId(selectedSubCastId);
        matrimonialRequest.setNakshakraid(selectedNakshatraId);
        matrimonialRequest.setBodyTypeid(selectedBodyTypeId);
        matrimonialRequest.setComplexionid(selectedComplexionId);
        matrimonialRequest.setOccupationid(selectedOccupationId);
        matrimonialRequest.setQualificationid(selectedQualificationId);
        matrimonialRequest.setSmokingId(selectedSmokingStatusId);
        matrimonialRequest.setFoodHabitsid(selectedFoodHabitesId);
        matrimonialRequest.setDrinkingStatusid(selectedDrinkingStatusId);
        matrimonialRequest.setMotherTougeid(selectedMothertoungeId);
        matrimonialRequest.setPhysicalStatusid(selectedPhysicalstatusId);
        matrimonialRequest.setMatrialStatusid(selectedMaritalStatusId);
        matrimonialRequest.setDoshamid(selectedDoshamId);


        mPresenter.editPreferences(matrimonialRequest, isDating);

/*
            mPresenter.updatePreferences(Constants.USER_ID, maxAge.toString(), minAge.toString(), selectedReligionId + "",
                    selectedMaterCastId + "", selectedLivingId + "",
                    ownHouse, maxWeight, minWeight, maxHeight, minHeight, maxDistance, minDistance, selectedCityId + "",
                    selectedSubCastId + "", selectedNakshatraId + "",
                    selectedBodyTypeId + "", selectedComplexionId + "",
                    selectedOccupationId + "", selectedQualificationId + "", maxMaxIncome, minIncome,
                    smoking, drinking, selectedFoodHabitesId + "", selectedMothertoungeId + "",
                    selectedPhysicalstatusId + "", selectedMaritalStatusId + "", selectedDoshamId + "");
        */
//        }
    }
}
