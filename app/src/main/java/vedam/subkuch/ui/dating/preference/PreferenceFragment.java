package vedam.subkuch.ui.dating.preference;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
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
import vedam.subkuch.network.models.GetPhysicalStatusBean;
import vedam.subkuch.network.models.GetQualificationBean;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getPreferencesResponse.GetPreferenceResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.dating.preference.presenter.PerferenceFragmentPresenter;
import vedam.subkuch.ui.dating.preference.presenter.PerferenceFragmentPresenterHandler;
import vedam.subkuch.ui.dating.preference.view.PerferenceFragmentView;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.FrequentFunctions;
import vedam.subkuch.utils.UiUtil;

public class PreferenceFragment extends BaseFragment implements PerferenceFragmentView, ItemAdapter.ItemClickHandler {

    PerferenceFragmentPresenterHandler mPresenter;
    GetPreferenceResponse response;
    @BindView(R.id.textViewReligion)
    TextView textViewReligion;
    @BindView(R.id.textViewCast)
    TextView textViewCast;
    @BindView(R.id.textViewSubCast)
    TextView textViewSubCast;
    @BindView(R.id.textViewLivingWith)
    TextView textViewLivingWith;
    @BindView(R.id.textViewAge)
    TextView textViewAge;
    @BindView(R.id.seekBarAge)
    CrystalRangeSeekbar seekBarAge;
    @BindView(R.id.textViewDistance)
    TextView textViewDistance;
    @BindView(R.id.seekBarDistance)
    CrystalRangeSeekbar seekBarDistance;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    @BindView(R.id.rootLayout)
    ScrollView rootLayout;
    Unbinder unbinder;
    @BindView(R.id.textViewCity)
    TextView textViewCity;
    @BindView(R.id.textViewNakshatra)
    TextView textViewNakshatra;
    @BindView(R.id.textViewBodytype)
    TextView textViewBodytype;
    @BindView(R.id.textViewComplexion)
    TextView textViewComplexion;
    @BindView(R.id.textViewOccupation)
    TextView textViewOccupation;
    @BindView(R.id.textViewQualification)
    TextView textViewQualification;
    @BindView(R.id.textViewFoodhabits)
    TextView textViewFoodhabits;
    @BindView(R.id.textViewDrinkingHabits)
    TextView textViewDrinkingStatus;
    @BindView(R.id.textViewMothertouge)
    TextView textViewMothertouge;
    @BindView(R.id.textViewPhysicalstatus)
    TextView textViewPhysicalstatus;
    @BindView(R.id.textViewDosham)
    TextView textViewDosham;
    @BindView(R.id.checkBoxSmoking)
    AppCompatCheckBox checkBoxSmoking;
    @BindView(R.id.checkBoxOwncar)
    AppCompatCheckBox checkBoxOwncar;
    @BindView(R.id.ceheckBoxHouse)
    AppCompatCheckBox ceheckBoxHouse;
    @BindView(R.id.textViewIncome)
    TextView textViewIncome;
    @BindView(R.id.seekBarIncome)
    CrystalRangeSeekbar seekBarIncome;
    @BindView(R.id.textViewHeight)
    TextView textViewHeight;
    @BindView(R.id.seekBarHeight)
    CrystalRangeSeekbar seekBarHeight;
    @BindView(R.id.textViewWeight)
    TextView textViewWeight;
    @BindView(R.id.seekBarWeight)
    CrystalRangeSeekbar seekBarWeight;
    @BindView(R.id.textViewMatrialstatus)
    TextView textViewMatrialstatus;
    private String minAge;
    private String maxAge;
    List<String> items = new ArrayList<>();
    int selectedMaterCastId = 0;
    int selectedSubCastId = 0;
    int selectedReligionId = 0;
    int selectedLivingId = 0;
    int selectedCityId = 0;
    int selectedNakshatraId = 0;
    int selectedBodyTypeId = 0;
    int selectedComplexionId = 0;
    int selectedOccupationId = 0;
    int selectedQualificationId = 0;
    int selectedFoodHabitesId = 0;
    int selectedMothertoungeId = 0;
    int selectedPhysicalstatusId = 0;
    int selectedDoshamId = 0;
    int selectedMaritalStatusId = 0;
    int selectedDrinkingStatusId = 0;
    String clickedItem = "";
    boolean ownHouse;
    boolean smoking;
    boolean car;


    GetMasterCastResponse masterCastResponse;
    GetCityResponse getCityResponse;
    GetGotrasBean gotraResponse;
    GetReligionResponse getReligionResponse;
    GetLivingResponse getLivingResponse;
    GetBodyTypeBean getBodyTypeBean;
    GetComplexionBean getComplexionBean;
    GetDoshamBean getDoshamBean;
    GetFoodHabitsBean getFoodHabitsBean;
    GetMothertongueBean getMothertongueBean;
    GetNakshatrasBean getNakshatrasBean;
    GetOccupationBean getOccupationBean;
    GetPhysicalStatusBean getPhysicalStatusBean;
    GetQualificationBean getQualificationBean;
    GetMaritalStatusResponse getMaritalStatusResponse;
    GetDrinkingHabits getDrinkingHabits;

    View view;
    private String minDistance;
    private String maxDistance;

    private String minHeight;
    private String maxHeight;

    private String minWeight;
    private String maxWeight;

    private String minIncome;
    private String maxMaxIncome;

    public static PreferenceFragment newInstance() {

        return new PreferenceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_preference, container, false);
        setTitle(getString(R.string.preferences));
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

    private void init() {

        seekBarAge.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minAge = minValue.toString();
            maxAge = maxValue.toString();
            textViewAge.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });
        seekBarDistance.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minDistance = minValue.toString();
            maxDistance = maxValue.toString();
            textViewDistance.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        seekBarIncome.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minIncome = minValue.toString();
            maxMaxIncome = maxValue.toString();
            textViewIncome.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        seekBarWeight.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minWeight = minValue.toString();
            maxWeight = maxValue.toString();
            textViewWeight.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        seekBarHeight.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
            minHeight = minValue.toString();
            maxHeight = maxValue.toString();
            textViewHeight.setText(String.format("%s-%s", minValue.toString(), maxValue));
        });

        mPresenter = new PerferenceFragmentPresenter(this);
        mPresenter.getPerference(AppPrefs.getPrefsUserId(context));
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
        baseshowFeedbackMessage(rootLayout, message);
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

        ownHouse = response.getReturnData().isOwnHouse();
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
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyGetReligion(GetReligionResponse response) {
        getReligionResponse = response;
        response.getReturnData().add(0, new GetReligionResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getReligionName());
        }
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyGetLiving(GetLivingResponse response) {
        getLivingResponse = response;
        response.getReturnData().add(0, new GetLivingResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getLivingWithName());
        }
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyUpdatePreferences(UpdateMatrimonialResponse response) {
        baseshowFeedbackMessage(rootLayout, "Updated successfully");
    }

    @Override
    public void onSuccessfullyGetGotra(GetGotrasBean response) {
        gotraResponse = response;
        response.getReturnData().add(0, new GetGotrasBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getName());
        }
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyGetComplexion(GetComplexionBean response) {
        getComplexionBean = response;
        response.getReturnData().add(0, new GetComplexionBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getComplexionname());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetOccupation(GetOccupationBean response) {
        getOccupationBean = response;
        response.getReturnData().add(0, new GetOccupationBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOccupationname());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetQualification(GetQualificationBean response) {
        getQualificationBean = response;
        response.getReturnData().add(0, new GetQualificationBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getQualificationname());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetFoodHabits(GetFoodHabitsBean response) {
        getFoodHabitsBean = response;
        response.getReturnData().add(0, new GetFoodHabitsBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getFoodHabitsName());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetDrinkingHabits(GetDrinkingHabits response) {
        getDrinkingHabits = response;
        response.getReturnData().add(0, new DrinkingHabits(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDrinkingStatus_Name());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetPhysicalstatus(GetPhysicalStatusBean response) {
        getPhysicalStatusBean = response;
        response.getReturnData().add(0, new GetPhysicalStatusBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getPhysicalStatusName());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetDosham(GetDoshamBean response) {
        getDoshamBean = response;
        response.getReturnData().add(0, new GetDoshamBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDoshamName());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetNakshatras(GetNakshatrasBean response) {
        getNakshatrasBean = response;
        response.getReturnData().add(0, new GetNakshatrasBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getNakshatraname());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetBodyType(GetBodyTypeBean response) {
        getBodyTypeBean = response;
        response.getReturnData().add(0, new GetBodyTypeBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getBodytypename());
        }
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyGetMothertongue(GetMothertongueBean response) {
        getMothertongueBean = response;
        response.getReturnData().add(0, new GetMothertongueBean.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMothertongueName());
        }
        intilizeAdapter();

    }

    @Override
    public void onSuccessfullyGetCity(GetCityResponse response) {
        getCityResponse = response;
        response.getReturnData().add(0, new GetCityResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getName());
        }
        intilizeAdapter();
    }

    @Override
    public void onSuccessfullyGetMaritalStatus(GetMaritalStatusResponse response) {
        getMaritalStatusResponse = response;
        response.getReturnData().add(0, new GetMaritalStatusResponse.ReturnDataBean(0, getString(R.string.any)));
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMaritalStatus_Name());
        }
        intilizeAdapter();
    }

    private void setData() {

        setText(textViewReligion, response.getReturnData().getReligionName());
        setText(textViewCast, response.getReturnData().getMasterCastName());
        setText(textViewLivingWith, response.getReturnData().getLivingWithName());
        setText(textViewSubCast, response.getReturnData().getGotraName());
        setText(textViewBodytype, response.getReturnData().getBodyTypeName());
        setText(textViewCity, response.getReturnData().getCityName());
        setText(textViewComplexion, response.getReturnData().getComplexionName());
        setText(textViewDosham, response.getReturnData().getDoshamName());
        setText(textViewFoodhabits, response.getReturnData().getFoodHabitsName());
        setText(textViewMothertouge, response.getReturnData().getMothertongueName());
        setText(textViewNakshatra, response.getReturnData().getNakshatraName());
        setText(textViewOccupation, response.getReturnData().getOccupationName());
        setText(textViewPhysicalstatus, response.getReturnData().getPhysicalStatusName());
        setText(textViewQualification, response.getReturnData().getQualificationName());
        setText(textViewMatrialstatus, response.getReturnData().getMaritalStatusName());
        setText(textViewDrinkingStatus, response.getReturnData().getDrinkingStatusName());


        if (response.getReturnData().isOwnHouse()) {
            ceheckBoxHouse.setChecked(true);
        }

        if (response.getReturnData().isIsSmoking()) {
            checkBoxSmoking.setChecked(true);
        }

        if (response.getReturnData().isOwnCar()) {
            checkBoxOwncar.setChecked(true);
        }

       /* if (!response.getReturnData().getFromLocation().isEmpty()&&!response.getReturnData().getToLocation().isEmpty()){
            seekBarDistance.setMinStartValue(Float.parseFloat(response.getReturnData().getFromLocation())).setMaxStartValue(Float.parseFloat(response.getReturnData().getToLocation())).apply();
        }*/
        seekBarAge.setMinStartValue(Float.parseFloat(response.getReturnData().getMinAge())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxAge())).apply();
        seekBarDistance.setMinStartValue(Float.parseFloat(response.getReturnData().getMinDistance())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxDistance())).apply();
        seekBarWeight.setMinStartValue(Float.parseFloat(response.getReturnData().getMinWeight())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxWeight())).apply();
        seekBarHeight.setMinStartValue(Float.parseFloat(response.getReturnData().getMinHeight())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxHeight())).apply();
        seekBarIncome.setMinStartValue(Float.parseFloat(response.getReturnData().getMinIncome())).setMaxStartValue(Float.parseFloat(response.getReturnData().getMaxIncome())).apply();
    }

    private void setText(TextView tv, String value) {
        if (AppUtil.deNull(value).isEmpty())
            tv.setText(getString(R.string.any));
        else
            tv.setText(value);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.relativeLayoutLiving})
    public void relativeLayoutLivingClick(View view) {
        clickedItem = "livingWith";
        this.view = view;
        mPresenter.getLiving();
    }

    @OnClick({R.id.relativeLayoutReligion})
    public void relativeLayoutReligionClick(View view) {
        clickedItem = "religion";
        this.view = view;
        mPresenter.getReligion();
    }

    @OnClick({R.id.relativeLayoutCity})
    public void relativeLayoutCityClick(View view) {
        clickedItem = "city";
        this.view = view;
        mPresenter.getAllCity();
    }

    @OnClick({R.id.relativeLayoutMatrialstatus})
    public void relativeLayoutMatrialstatusClick(View view) {
        clickedItem = "matrialstatus";
        this.view = view;
        mPresenter.getMatrialStatus();
    }


    @OnClick(R.id.relativeLayoutMasterCast)
    public void relativeLayoutMasterCastClick(View view) {
        clickedItem = "masterCast";
        this.view = view;
        mPresenter.getMasterCast(selectedReligionId + "");
    }


    @OnClick(R.id.relativeLayoutSubCast)
    public void relativeLayoutSubCastClick(View view) {
        if (selectedMaterCastId == 0) {
            baseshowFeedbackMessage(view, getString(R.string.select_master_cast_first));
        } else {
            clickedItem = "subCast";
            this.view = view;
            mPresenter.getGotra(selectedMaterCastId + "");
        }
    }

    @OnClick(R.id.relativeLayoutNakshakra)
    public void relativeLayoutNakshakraClick(View view) {
        clickedItem = "nakshakra";
        this.view = view;
        mPresenter.getNakshakra();
    }

    @OnClick(R.id.relativeLayoutMothertouge)
    public void relativeLayoutMothertougeClick(View view) {
        clickedItem = "mothertouge";
        this.view = view;
        mPresenter.getMothertouge();
    }

    @OnClick(R.id.relativeLayoutBodytype)
    public void relativeLayoutBodytypeClick(View view) {
        clickedItem = "bodytype";
        this.view = view;
        mPresenter.getBodytype();
    }

    @OnClick(R.id.relativeLayoutComplexion)
    public void relativeLayoutComplexionClick(View view) {
        clickedItem = "complexion";
        this.view = view;
        mPresenter.getComplexion();
    }

    @OnClick(R.id.relativeLayoutOccupation)
    public void relativeLayoutOccupationClick(View view) {
        clickedItem = "occupation";
        this.view = view;
        mPresenter.getOccupation();
    }

    @OnClick(R.id.relativeLayoutQualification)
    public void relativeLayoutQualificationClick(View view) {
        clickedItem = "qualification";
        this.view = view;
        mPresenter.getQualification();
    }

    @OnClick(R.id.relativeLayoutFoodhabits)
    public void relativeLayoutFoodhabitsClick(View view) {
        clickedItem = "foodhabits";
        this.view = view;
        mPresenter.getFoodhabits();
    }

    @OnClick(R.id.relativeLayoutDrinkingHabits)
    public void relativeLayoutDrinkingHabitsClick(View view) {
        clickedItem = "DrinkingHabits";
        this.view = view;
        mPresenter.getDrinkingHabits();
    }

    @OnClick(R.id.relativeLayoutPhysicalstatus)
    public void relativeLayoutPhysicalstatusClick(View view) {
        clickedItem = "physicalstatus";
        this.view = view;
        mPresenter.getPhysicalstatus();
    }

    @OnClick(R.id.relativeLayoutDosham)
    public void relativeLayoutDoshamClick(View view) {
        clickedItem = "dosham";
        this.view = view;
        mPresenter.getDosham();
    }

    public void intilizeAdapter() {
        ItemAdapter adapter = new ItemAdapter(items);
        adapter.OnItemClickListener(this);
        showPopWindow(view, adapter);
    }

    @Override
    public void OnItemClick(int position) {
        mPopupWindow.dismiss();
        if (clickedItem.equalsIgnoreCase("masterCast")) {
            selectedMaterCastId = masterCastResponse.getReturnData().get(position).getMasterCasteID();
            textViewCast.setText(masterCastResponse.getReturnData().get(position).getMasterCastName());
            textViewSubCast.setText("");
        } else if (clickedItem.equalsIgnoreCase("subCast")) {
            selectedSubCastId = gotraResponse.getReturnData().get(position).getGotrasid();
            textViewSubCast.setText(gotraResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("religion")) {
            selectedReligionId = getReligionResponse.getReturnData().get(position).getReligionID();
            textViewReligion.setText(getReligionResponse.getReturnData().get(position).getReligionName());
        } else if (clickedItem.equalsIgnoreCase("livingWith")) {
            selectedLivingId = getLivingResponse.getReturnData().get(position).getLivingWithId();
            textViewLivingWith.setText(getLivingResponse.getReturnData().get(position).getLivingWithName());
        } else if (clickedItem.equalsIgnoreCase("mothertouge")) {
            selectedMothertoungeId = getMothertongueBean.getReturnData().get(position).getMothertongueid();
            textViewMothertouge.setText(getMothertongueBean.getReturnData().get(position).getMothertongueName());
        } else if (clickedItem.equalsIgnoreCase("bodytype")) {
            selectedBodyTypeId = getBodyTypeBean.getReturnData().get(position).getBodytypeid();
            textViewBodytype.setText(getBodyTypeBean.getReturnData().get(position).getBodytypename());
        } else if (clickedItem.equalsIgnoreCase("complexion")) {
            selectedComplexionId = getComplexionBean.getReturnData().get(position).getComplexionid();
            textViewComplexion.setText(getComplexionBean.getReturnData().get(position).getComplexionname());
        } else if (clickedItem.equalsIgnoreCase("occupation")) {
            selectedOccupationId = getOccupationBean.getReturnData().get(position).getOccupationid();
            textViewOccupation.setText(getOccupationBean.getReturnData().get(position).getOccupationname());
        } else if (clickedItem.equalsIgnoreCase("qualification")) {
            selectedQualificationId = getQualificationBean.getReturnData().get(position).getQualificationid();
            textViewQualification.setText(getQualificationBean.getReturnData().get(position).getQualificationname());
        } else if (clickedItem.equalsIgnoreCase("foodhabits")) {
            selectedFoodHabitesId = getFoodHabitsBean.getReturnData().get(position).getFoodHabitsid();
            textViewFoodhabits.setText(getFoodHabitsBean.getReturnData().get(position).getFoodHabitsName());
        } else if (clickedItem.equalsIgnoreCase("DrinkingHabits")) {
            selectedDrinkingStatusId = getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Id();
            textViewDrinkingStatus.setText(getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("physicalstatus")) {
            selectedPhysicalstatusId = getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusid();
            textViewPhysicalstatus.setText(getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusName());
        } else if (clickedItem.equalsIgnoreCase("dosham")) {
            selectedDoshamId = getDoshamBean.getReturnData().get(position).getDoshamid();
            textViewDosham.setText(getDoshamBean.getReturnData().get(position).getDoshamName());
        } else if (clickedItem.equalsIgnoreCase("city")) {
            selectedCityId = getCityResponse.getReturnData().get(position).getCityId();
            textViewCity.setText(getCityResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("matrialstatus")) {
            selectedMaritalStatusId = getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Id();
            textViewMatrialstatus.setText(getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("nakshakra")) {
            selectedNakshatraId = getNakshatrasBean.getReturnData().get(position).getNakshatraid();
            textViewNakshatra.setText(getNakshatrasBean.getReturnData().get(position).getNakshatraname());
        }
    }


    @OnClick({R.id.btnUpdate})
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

            ownHouse = ceheckBoxHouse.isChecked();
            smoking = checkBoxSmoking.isChecked();

            car = checkBoxOwncar.isChecked();

            FrequentFunctions.hideKeyBoard(context, view);


            MatrimonialRequest matrimonialRequest = new MatrimonialRequest();
            matrimonialRequest.setProfileId(Integer.parseInt(AppPrefs.getPrefsUserId(context)));
            matrimonialRequest.setCityId(selectedCityId);
//            matrimonialRequest.setCountryId(Constants.COUNTRY_ID);
            matrimonialRequest.setReligionId(selectedReligionId);
            matrimonialRequest.setCasteId(selectedMaterCastId);
            matrimonialRequest.setOwnCar(car);
            matrimonialRequest.setOwnHouse(ownHouse);
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
            matrimonialRequest.setIsSmoking(smoking);
            matrimonialRequest.setFoodHabitsid(selectedFoodHabitesId);
            matrimonialRequest.setDrinkingStatusid(selectedDrinkingStatusId);
            matrimonialRequest.setMotherTougeid(selectedMothertoungeId);
            matrimonialRequest.setPhysicalStatusid(selectedPhysicalstatusId);
            matrimonialRequest.setMatrialStatusid(selectedMaritalStatusId);
            matrimonialRequest.setDoshamid(selectedDoshamId);


            mPresenter.editPreferences(matrimonialRequest);

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
