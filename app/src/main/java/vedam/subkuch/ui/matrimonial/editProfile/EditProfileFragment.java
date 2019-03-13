package vedam.subkuch.ui.matrimonial.editProfile;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImageFragment;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.DataPart;
import vedam.subkuch.network.models.GetSmokingResponse;
import vedam.subkuch.network.models.GetWeightResponse;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.network.models.GetAnnualIncome;
import vedam.subkuch.network.models.GetBodyTypeBean;
import vedam.subkuch.network.models.GetComplexionBean;
import vedam.subkuch.network.models.GetDoshamBean;
import vedam.subkuch.network.models.GetDrinkingHabits;
import vedam.subkuch.network.models.GetFoodHabitsBean;
import vedam.subkuch.network.models.GetGotrasBean;
import vedam.subkuch.network.models.GetHeightResponse;
import vedam.subkuch.network.models.GetMaritalStatusResponse;
import vedam.subkuch.network.models.GetMothertongueBean;
import vedam.subkuch.network.models.GetNakshatrasBean;
import vedam.subkuch.network.models.GetOccupationBean;
import vedam.subkuch.network.models.GetOwnCarResponse;
import vedam.subkuch.network.models.GetOwnHouseResponse;
import vedam.subkuch.network.models.GetPhysicalStatusBean;
import vedam.subkuch.network.models.GetQualificationBean;
import vedam.subkuch.network.models.UserDetail.GetUserDetailResponse;
import vedam.subkuch.network.models.UserDetail.UpdateProfileRequest;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.matrimonial.editProfile.presenter.EditProfileFragmentPresenter;
import vedam.subkuch.ui.matrimonial.editProfile.view.EditProfileFragmentView;
import vedam.subkuch.ui.matrimonial.preference.ItemAdapter;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.FrequentFunctions;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;
import vedam.subkuch.utils.Validations;

public class EditProfileFragment extends BaseAddImageFragment implements EditProfileFragmentView, ItemAdapter.ItemClickHandler {

    @BindView(R.id.rootLayout)
    ScrollView rootLayout;
    @BindView(R.id.textViewCast)
    TextView textViewCast;

    @BindView(R.id.textViewGotra)
    TextView textViewGotra;

    @BindView(R.id.textViewReligion)
    TextView textViewReligion;

    @BindView(R.id.textViewLivingWith)
    TextView textViewLivingWith;

    @BindView(R.id.textViewMatrialStatus)
    TextView textViewMatrialStatus;

    @BindView(R.id.textViewHeight)
    TextView textViewHeight;
    @BindView(R.id.textViewWeight)
    TextView textViewWeight;
    @BindView(R.id.textViewOwnCar)
    TextView textViewOwnCar;
    @BindView(R.id.textViewOwnHouse)
    TextView textViewOwnHouse;
    @BindView(R.id.textViewSmoking)
    TextView textViewSmoking;
    @BindView(R.id.tv_interested_in)
    TextView tvInterestedIn;

    @BindView(R.id.btnUpdate)
    TextView btnUpdate;

    @BindView(R.id.switchMatrimonial)
    Switch switchDatingOrMatrimonial;

    String clickedItem = "";

    GetMasterCastResponse masterCastResponse;
    GetGotrasBean gotraResponse;
    GetReligionResponse getReligionResponse;
    GetBodyTypeBean getBodyTypeBean;
    GetComplexionBean getComplexionBean;
    GetDoshamBean getDoshamBean;
    GetFoodHabitsBean getFoodHabitsBean;
    GetMothertongueBean getMothertongueBean;
    GetNakshatrasBean getNakshatrasBean;
    GetOccupationBean getOccupationBean;
    GetPhysicalStatusBean getPhysicalStatusBean;
    GetQualificationBean getQualificationBean;
    GetAnnualIncome getAnnualIncome;
    GetHeightResponse getHeightResponse;
    GetWeightResponse getWeightResponse;
    GetOwnCarResponse getOwnCarResponse;
    GetOwnHouseResponse getOwnHouseResponse;
    GetSmokingResponse getSmokingResponse;
    GetDrinkingHabits getDrinkingHabits;
    GetLivingResponse getLivingResponse;
    GetMaritalStatusResponse getMaritalStatusResponse;

    View view;
    EditProfileFragmentPresenter mPresenter;
    List<String> items = new ArrayList<>();
    /*@BindView(R.id.editTextIncome)
    EditText editTextIncome;*/
    @BindView(R.id.textViewNakshakra)
    TextView textViewNakshakra;
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
    @BindView(R.id.textViewPhysicalstatus)
    TextView textViewPhysicalstatus;
    @BindView(R.id.textViewDosham)
    TextView textViewDosham;
    @BindView(R.id.textViewMothertouge)
    TextView textViewMothertouge;
    @BindView(R.id.textViewAnnualIncome)
    TextView textViewAnnualIncome;
    @BindView(R.id.et_about_me)
    EditText etAboutMe;

    int selectedMaterCastId = -1;
    int selectedSubCastId = -1;
    int selectedReligionId = -1;
    int selectedLivingId = -1;
    int selectedMatirialStatusId = -1;
    int selectedNakshakraId = -1;
    int selectedBodytypeId = -1;
    int selectedComplexionId = -1;
    int selectedOccupationId = -1;
    int selectedQualificationId = -1;
    int selectedFoodhabitsId = -1;
    int selectedMothertougeId = -1;
    int selectedPhysicalstatusId = -1;
    int selectedDoshamId = -1;
    int selectedAnnualIncomeId = -1;
    int selectedDrinkingStatusId = -1;
    int selectedHeightId = -1;
    int selectedWeightId = -1;
    int selectedOwnCarId = -1;
    int selectedOwnHouseId = -1;
    int selectedSmokingStatusId = -1;
    @BindView(R.id.tv_religion_heading)
    TextView tvReligionHeading;
    @BindView(R.id.relativeLayoutReligion)
    RelativeLayout relativeLayoutReligion;
    @BindView(R.id.tv_caste_heading)
    TextView tvCasteHeading;
    @BindView(R.id.relativeLayoutMasterCast)
    RelativeLayout relativeLayoutMasterCast;
    @BindView(R.id.tv_gotra_heading)
    TextView tvGotraHeading;
    @BindView(R.id.relativeLayoutGotra)
    RelativeLayout relativeLayoutGotra;
    @BindView(R.id.tv_nakshatra_heading)
    TextView tvNakshatraHeading;
    @BindView(R.id.relativeLayoutNakshakra)
    RelativeLayout relativeLayoutNakshakra;
    @BindView(R.id.tv_complexion_heading)
    TextView tvComplexionHeading;
    @BindView(R.id.relativeLayoutComplexion)
    RelativeLayout relativeLayoutComplexion;
    @BindView(R.id.tv_qualification_heading)
    TextView tvQualificationHeading;
    @BindView(R.id.relativeLayoutQualification)
    RelativeLayout relativeLayoutQualification;
    @BindView(R.id.tv_physical_status_heading)
    TextView tvPhysicalStatusHeading;
    @BindView(R.id.relativeLayoutPhysicalstatus)
    RelativeLayout relativeLayoutPhysicalstatus;
    @BindView(R.id.tv_dosham_heading)
    TextView tvDoshamHeading;
    @BindView(R.id.relativeLayoutDosham)
    RelativeLayout relativeLayoutDosham;
    @BindView(R.id.tv_mother_tongue_heading)
    TextView tvMotherTongueHeading;
    @BindView(R.id.relativeLayoutMothertouge)
    RelativeLayout relativeLayoutMothertouge;
    @BindView(R.id.tv_living_with_heading)
    TextView tvLivingWithHeading;
    @BindView(R.id.relativeLayoutLiving)
    RelativeLayout relativeLayoutLiving;
    @BindView(R.id.tv_marital_status_heading)
    TextView tvMaritalStatusHeading;
    @BindView(R.id.relativeMatrialStatus)
    RelativeLayout relativeMatrialStatus;
    @BindView(R.id.tv_annual_income_heading)
    TextView tvAnnualIncomeHeading;
    @BindView(R.id.relativeAnnualIncome)
    RelativeLayout relativeAnnualIncome;
    @BindView(R.id.tv_car_heading)
    TextView tvCarHeading;
    @BindView(R.id.relativeOwnCar)
    RelativeLayout relativeOwnCar;
    @BindView(R.id.tv_house_heading)
    TextView tvHouseHeading;
    @BindView(R.id.relativeOwnHouse)
    RelativeLayout relativeOwnHouse;


    private Unbinder unbinder;
    private String mEmail = "";
    private String mTokenID = "";
    private String mDeviceID = "";
    private String mUpdatedDate = "";
    private String latitude;
    private String longitude;
    private Stack<Object> object = new Stack<>();
    private boolean isImageLinkPresent;
    private boolean isDating;

    public static EditProfileFragment newInstance(boolean isDating) {

        EditProfileFragment fragment = new EditProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating);
        fragment.setArguments(bundle);
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
        setHasOptionsMenu(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        setTitle(getString(R.string.edit_profile));
        mPresenter = new EditProfileFragmentPresenter(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isDating)
            hideFields();
        mPresenter.getUserDetail(AppPrefs.getPrefsUserId(context), isDating);
        requestLocation();
        setImagesLayout(view);
    }

    private void hideFields() {
        tvInterestedIn.setText(getString(R.string.interested_in_dating));
        tvReligionHeading.setVisibility(View.GONE);
        relativeLayoutReligion.setVisibility(View.GONE);
        tvCasteHeading.setVisibility(View.GONE);
        relativeLayoutMasterCast.setVisibility(View.GONE);
        tvGotraHeading.setVisibility(View.GONE);
        relativeLayoutGotra.setVisibility(View.GONE);
        tvNakshatraHeading.setVisibility(View.GONE);
        relativeLayoutNakshakra.setVisibility(View.GONE);
        tvComplexionHeading.setVisibility(View.GONE);
        relativeLayoutComplexion.setVisibility(View.GONE);
        tvQualificationHeading.setVisibility(View.GONE);
        relativeLayoutQualification.setVisibility(View.GONE);
        tvPhysicalStatusHeading.setVisibility(View.GONE);
        relativeLayoutPhysicalstatus.setVisibility(View.GONE);
        tvDoshamHeading.setVisibility(View.GONE);
        relativeLayoutDosham.setVisibility(View.GONE);
        tvMotherTongueHeading.setVisibility(View.GONE);
        relativeLayoutMothertouge.setVisibility(View.GONE);
        tvLivingWithHeading.setVisibility(View.GONE);
        relativeLayoutLiving.setVisibility(View.GONE);
        tvMaritalStatusHeading.setVisibility(View.GONE);
        relativeMatrialStatus.setVisibility(View.GONE);
        tvAnnualIncomeHeading.setVisibility(View.GONE);
        relativeAnnualIncome.setVisibility(View.GONE);
        tvCarHeading.setVisibility(View.GONE);
        relativeOwnCar.setVisibility(View.GONE);
        tvHouseHeading.setVisibility(View.GONE);
        relativeOwnHouse.setVisibility(View.GONE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }

   /* public void setAdapter() {
        adapter = new ImagesAdapter(listOfPhots, getActivity());
        adapter.itemClickHandler(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
    }*/

    @OnClick({R.id.btnUpdate})
    public void btnUpdateClick(View view) {
        FrequentFunctions.hideKeyBoard(context, view);
        if (!switchDatingOrMatrimonial.isChecked())
            baseshowFeedbackMessage(rootLayout, getNotInterestedString());
        else if (!isImageLinkPresent && getImageUri() == null) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.add_profile_pricture));
        } else if (selectedReligionId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_religion));
        } else if (selectedMaterCastId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_cast));
        } else if (Validations.isFieldEmpty(textViewGotra.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_gothra));
        } else if (selectedLivingId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_living_with));
        } else if (selectedMatirialStatusId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_matrialstatus));
        } else {

            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
            updateProfileRequest.setProfileId(Integer.parseInt(AppPrefs.getPrefsUserId(context)));
            updateProfileRequest.setAboutMe(AppUtil.deNull(etAboutMe.getText()));
            updateProfileRequest.setEMail(mEmail);
            updateProfileRequest.setUserTypeId(1);
            updateProfileRequest.setTokenId(mTokenID);
            updateProfileRequest.setDeviceId(mDeviceID);
            updateProfileRequest.setLatitude(AppUtil.deNull(latitude));
            updateProfileRequest.setLongitude(AppUtil.deNull(longitude));
            updateProfileRequest.setCountryid(Constants.COUNTRY_ID);
            updateProfileRequest.setReligionId(selectedReligionId);
            updateProfileRequest.setCasteId(selectedMaterCastId);
            updateProfileRequest.setOwnCarId(selectedOwnCarId);
            updateProfileRequest.setOwnHouseId(selectedOwnHouseId);
            updateProfileRequest.setLivingWithId(selectedLivingId);
            if (isDating)
                updateProfileRequest.setDating(switchDatingOrMatrimonial.isChecked());
            else
                updateProfileRequest.setMatrimonial(switchDatingOrMatrimonial.isChecked());
            updateProfileRequest.setHeightId(selectedHeightId);
            updateProfileRequest.setWeightId(selectedWeightId);
            updateProfileRequest.setGotraid(selectedSubCastId);
            updateProfileRequest.setNakshakraid(selectedNakshakraId);
            updateProfileRequest.setBodyTypeid(selectedBodytypeId);
            updateProfileRequest.setComplexionid(selectedComplexionId);
            updateProfileRequest.setOccupationid(selectedOccupationId);
            updateProfileRequest.setQualificationid(selectedQualificationId);
            updateProfileRequest.setAnualIncomeid(selectedAnnualIncomeId);
            updateProfileRequest.setSmokingId(selectedSmokingStatusId);
            updateProfileRequest.setDrinkingStatusid(selectedDrinkingStatusId);
            updateProfileRequest.setFoodHabitsid(selectedFoodhabitsId);
            updateProfileRequest.setMotherTougeid(selectedMothertougeId);
            updateProfileRequest.setPhysicalStatusid(selectedPhysicalstatusId);
            updateProfileRequest.setMatrialStatusid(selectedMatirialStatusId);
            updateProfileRequest.setDoshamid(selectedDoshamId);
            updateProfileRequest.setUpdatedDate(mUpdatedDate);
            object.add(new Object());

            mPresenter.updateProfile(isDating, updateProfileRequest);

            if (getImageUri() != null)
                uploadProfileImage();
            //mPresenter.updateMaterimonial(Constants.USER_ID, selectedReligionId + "", selectedMaterCastId + "", selectedSubCastId + "", ownCar, ownHouse, selectedLivingId + "", "123", selectedPreffedType, editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextAge.getText().toString(), "2121.212", "65656.2212", selectedNakshakraId + "", selectedBodytypeId + "", selectedComplexionId + "", selectedOccupationId + "", selectedQualificationId + "", editTextIncome.getText().toString(), smokingstatus, drinkingstatus, selectedFoodhabitsId + "", selectedMothertougeId + "", selectedPhysicalstatusId + "", selectedMatirialStatusId + "", selectedDoshamId + "");
        }
    }

    private String getNotInterestedString() {

        if (isDating)
            return getString(R.string.no_interest_dating);
        else
            return getString(R.string.no_interest_matrimonial);
    }

    private void uploadProfileImage() {
        object.add(new Object());
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getBitmap(context, getImageUri()))
                , NetworkConstants.JPEG_MIME_TYPE));

//        ImageIdRequest imageIdRequest = new ImageIdRequest();
       /* ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        imageIdRequest.setImageIds(arrayList);
        System.out.println(new Gson().toJson(imageIdRequest));*/

        if (isDating)
            DataFetcher.uploadDatingProfileImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
        else
            DataFetcher.uploadMatrimonialProfileImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null) {
            object.pop();
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                checkAndFinish();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
        }
    };

    private void checkAndFinish() {

        if (object.isEmpty() && getFragmentManager() != null) {
            UiUtil.cancelProgressDialog();
            UiUtil.showToast(getActivity(), getString(R.string.profile_updated));
            getFragmentManager().popBackStack();
        }
    }

    @OnClick({R.id.relativeLayoutLiving})
    public void relativeLayoutLivingClick(View view) {
        clickedItem = "livingWith";
        this.view = view;
        mPresenter.getLiving();
    }

    @OnClick({R.id.relativeMatrialStatus})
    public void relativeMatrialStatusClick(View view) {
        clickedItem = "matrialStatus";
        this.view = view;
        mPresenter.getMatrialStatus();
    }

    @OnClick({R.id.relativeLayoutReligion})
    public void relativeLayoutReligionClick(View view) {
        clickedItem = "religion";
        this.view = view;
        mPresenter.getReligion();
    }

    @OnClick(R.id.relativeLayoutMasterCast)
    public void relativeLayoutMasterCastClick(View view) {
        clickedItem = "masterCast";
        this.view = view;
        mPresenter.getMasterCast(selectedReligionId + "");
    }


   /* @OnClick(R.id.relativeLayoutPrefferd)
    public void relativeLayoutPreffedClick(View view) {
        clickedItem = "preferred";
        this.view = view;

        items.clear();
        items.addAll(Arrays.asList(getResources().getStringArray(R.array.preffered_type)));
        initializeAdapter();

    }*/


    @OnClick(R.id.relativeLayoutMothertouge)
    public void relativeLayoutGotraMothertougeClick(View view) {
        clickedItem = "mothertouge";
        this.view = view;
        mPresenter.getMothertouge();
    }

    @OnClick(R.id.relativeLayoutGotra)
    public void relativeLayoutGotraClick(View view) {
        if (selectedMaterCastId == -1) {
            baseshowFeedbackMessage(view, getString(R.string.select_master_cast_first));
        } else {
            clickedItem = "gotra";
            this.view = view;
            mPresenter.getGotra(selectedMaterCastId + "");
        }
    }

    @OnClick(R.id.relativeLayoutNakshakra)
    public void relativeLayoutNakshakraClick(View view) {
        clickedItem = "nakshatra";
        this.view = view;
        mPresenter.getNakshakra();
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

    @OnClick(R.id.relativeLayoutDrinkingHabits)
    public void relativeLayoutDrinkingHabitsClick(View view) {
        clickedItem = "DrinkingHabits";
        this.view = view;
        mPresenter.getDrinkingHabits();
    }

    @OnClick(R.id.relativeAnnualIncome)
    public void relativeLayoutAnnualIncomeClick(View view) {
        clickedItem = "AnnualIncome";
        this.view = view;
        mPresenter.getAnnualIncome();
    }

    @OnClick(R.id.relativeHeight)
    public void relativeHeightClick(View view) {
        clickedItem = "Height";
        this.view = view;
        mPresenter.getHeight();
    }

    @OnClick(R.id.relativeWeight)
    public void relativeWeightClick(View view) {
        clickedItem = "Weight";
        this.view = view;
        mPresenter.getWeight();
    }

    @OnClick(R.id.relativeOwnCar)
    public void relativeOwnCarClick(View view) {
        clickedItem = "OwnCar";
        this.view = view;
        mPresenter.getOwnCar();
    }

    @OnClick(R.id.relativeOwnHouse)
    public void relativeOwnHouseClick(View view) {
        clickedItem = "OwnHouse";
        this.view = view;
        mPresenter.getOwnHouse();
    }

    @OnClick(R.id.relativeSmoking)
    public void relativeSmokingClick(View view) {
        clickedItem = "Smoking";
        this.view = view;
        mPresenter.getSmoking();
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
    public void onSuccessfullyGetMasterCast(GetMasterCastResponse response) {
        masterCastResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMasterCastName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetGotra(GetGotrasBean response) {
        gotraResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetReligion(GetReligionResponse response) {
        getReligionResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getReligionName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetLiving(GetLivingResponse response) {

        getLivingResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getLivingWithName());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyUpdateProfile(UpdateMatrimonialResponse response) {
        object.pop();
        checkAndFinish();
    }

    @Override
    public void onSuccessfullyGetUserDetail(GetUserDetailResponse response) {

        GetUserDetailResponse.ReturnDataBean returnDataBean = response.getReturnData().get(0);

        if (returnDataBean.getImagesList() != null && returnDataBean.getImagesList().length > 0) {
            String imageLink = returnDataBean.getImagesList()[0].getImage();
            if (!TextUtils.isEmpty(imageLink)) {
                isImageLinkPresent = true;
                UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                        .setImageLink(imageLink)
                        .setPlaceholderResource(R.drawable.placeholder_small)
                        .setErrorResource(R.drawable.placeholder_small)
                        .setTarget(ivPicture).build());
            } else {
                isImageLinkPresent = false;
                ivPicture.setBackgroundResource(R.drawable.placeholder_small);
            }
        } else
            isImageLinkPresent = false;

        mEmail = returnDataBean.getEMail();
        mTokenID = returnDataBean.getTokenId();
        mDeviceID = returnDataBean.getDeviceId();
        mUpdatedDate = FrequentFunctions.getCurrentDateTime();

        etAboutMe.setText(returnDataBean.getAboutMe());
        textViewCast.setText(returnDataBean.getMasterCastName());
        selectedMaterCastId = returnDataBean.getCasteId();

        textViewGotra.setText(returnDataBean.getGotraName());
        selectedSubCastId = returnDataBean.getGotraid();

        textViewNakshakra.setText(returnDataBean.getNakshatraName());
        selectedNakshakraId = returnDataBean.getNakshakraid();

        textViewBodytype.setText(returnDataBean.getBodyTypeName());
        selectedBodytypeId = returnDataBean.getBodyTypeid();

        textViewComplexion.setText(returnDataBean.getComplexionName());
        selectedComplexionId = returnDataBean.getComplexionid();

        textViewOccupation.setText(returnDataBean.getOccupationName());
        selectedOccupationId = returnDataBean.getOccupationid();

        textViewQualification.setText(returnDataBean.getQualificationName());
        selectedQualificationId = returnDataBean.getQualificationid();

        textViewFoodhabits.setText(returnDataBean.getFoodHabitsName());
        selectedFoodhabitsId = returnDataBean.getFoodHabitsid();

        textViewDrinkingStatus.setText(returnDataBean.getDrinkingStatusName());
        selectedDrinkingStatusId = returnDataBean.getDrinkingStatusid();

        textViewPhysicalstatus.setText(returnDataBean.getPhysicalStatusName());
        selectedPhysicalstatusId = returnDataBean.getPhysicalStatusid();

        textViewDosham.setText(returnDataBean.getDoshamName());
        selectedDoshamId = returnDataBean.getDoshamid();

        textViewMothertouge.setText(returnDataBean.getMothertongueName());
        selectedMothertougeId = returnDataBean.getMotherTougeid();


        textViewLivingWith.setText(returnDataBean.getLivingWithName());
        selectedLivingId = returnDataBean.getLivingWithId();

        textViewAnnualIncome.setText(returnDataBean.getIncome());
        selectedAnnualIncomeId = returnDataBean.getAnualIncomeid();

        textViewHeight.setText(returnDataBean.getHeight());
        selectedHeightId = returnDataBean.getHeightId();

        textViewWeight.setText(returnDataBean.getWeight());
        selectedWeightId = returnDataBean.getWeightId();

        textViewOwnCar.setText(returnDataBean.getOwnCarType());
        selectedOwnCarId = returnDataBean.getOwnCarId();

        textViewOwnHouse.setText(returnDataBean.getOwnHouseType());
        selectedOwnHouseId = returnDataBean.getOwnHouseId();

        textViewSmoking.setText(returnDataBean.getSmokingType());
        selectedSmokingStatusId = returnDataBean.getSmokingId();
        //textViewPreperdType.setText(response.getReturnData().getPrefferedtype());
        //selectedPreffedType = response.getReturnData().getPrefferedtype();


        selectedMatirialStatusId = returnDataBean.getMatrialStatusid();
        textViewMatrialStatus.setText(returnDataBean.getMaritalStatusName());
        textViewReligion.setText(returnDataBean.getReligionName());
        selectedReligionId = returnDataBean.getReligionId();

        if (isDating)
            switchDatingOrMatrimonial.setChecked(returnDataBean.isDating());
        else
            switchDatingOrMatrimonial.setChecked(returnDataBean.isMatrimonial());
        //setData(response);
    }

   /* public void setData(GetUserDetailResponse response) {
        for (int i = 0; i < 6; i++) {
            Photos photos = new Photos();
            photos.setImage(null);
            photos.setImageUrl("");
            photos.setImageId(0);
            listOfPhots.add(photos);
        }
       *//* for (int i = 0; i < response.getReturnData().getImagedata().size(); i++) {
            Photos photos = new Photos();
            photos.setImageUrl(response.getReturnData().getImagedata().get(i).getImage());
            photos.setImageId(Integer.valueOf(response.getReturnData().getImagedata().get(i).getId()));
            listOfPhots.remove(i);
            listOfPhots.add(i, photos);
        }*//*
        setAdapter();
    }*/

    /*@Override
    public void onSuccessfullyDeleteImage(DeleteImageResponse response) {
        listOfPhots.get(clickedImageCrossClick).setImageUrl("");
        listOfPhots.get(clickedImageCrossClick).setImageBitMap(null);
        listOfPhots.get(clickedImageCrossClick).setImage(null);
        listOfPhots.get(clickedImageCrossClick).setImageId(0);
        adapter.customNotify(listOfPhots);
    }*/

   /* @Override
    public void onSuccessfullyInsertUpdateImage(InsertImageResponse response) {
        addPhotosToList(singleBitmapDetail.getBitMap(), response.getReturnData().get(0).getImagedataid());
    }*/

    @Override
    public void onSuccessfullyGetComplexion(GetComplexionBean response) {
        getComplexionBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getComplexionname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetOccupation(GetOccupationBean response) {
        getOccupationBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOccupationname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetQualification(GetQualificationBean response) {
        getQualificationBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getQualificationname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetFoodHabits(GetFoodHabitsBean response) {
        getFoodHabitsBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getFoodHabitsName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetDrinkingHabits(GetDrinkingHabits response) {
        getDrinkingHabits = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDrinkingStatus_Name());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetAnnualIncome(GetAnnualIncome response) {
        getAnnualIncome = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getIncome());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetHeight(GetHeightResponse response) {
        getHeightResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getHeight1());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetWeight(GetWeightResponse response) {
        getWeightResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getWeight1());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetOwnCar(GetOwnCarResponse response) {
        getOwnCarResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOwnCarType());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetOwnHouse(GetOwnHouseResponse response) {
        getOwnHouseResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getOwnHouseType());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetSmoking(GetSmokingResponse response) {
        getSmokingResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getSmokingType());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetPhysicalstatus(GetPhysicalStatusBean response) {
        getPhysicalStatusBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getPhysicalStatusName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetDosham(GetDoshamBean response) {
        getDoshamBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getDoshamName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetNakshatras(GetNakshatrasBean response) {
        getNakshatrasBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getNakshatraname());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyGetBodyType(GetBodyTypeBean response) {
        getBodyTypeBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getBodytypename());
        }
        initializeAdapter();
    }

    @Override
    public void onSuccessfullyGetMothertongue(GetMothertongueBean response) {
        getMothertongueBean = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMothertongueName());
        }
        initializeAdapter();

    }

    @Override
    public void onSuccessfullyMaritalStatus(GetMaritalStatusResponse response) {
        getMaritalStatusResponse = response;
        items.clear();
        for (int i = 0; i < response.getReturnData().size(); i++) {
            items.add(response.getReturnData().get(i).getMaritalStatus_Name());
        }
        initializeAdapter();
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
            textViewCast.setText(masterCastResponse.getReturnData().get(position).getMasterCastName());
            textViewGotra.setText("");
        } else if (clickedItem.equalsIgnoreCase("gotra")) {
            selectedSubCastId = gotraResponse.getReturnData().get(position).getGotrasid();
            textViewGotra.setText(gotraResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("religion")) {
            selectedReligionId = getReligionResponse.getReturnData().get(position).getReligionID();
            textViewReligion.setText(getReligionResponse.getReturnData().get(position).getReligionName());
        } else if (clickedItem.equalsIgnoreCase("livingWith")) {
            selectedLivingId = getLivingResponse.getReturnData().get(position).getLivingWithId();
            textViewLivingWith.setText(getLivingResponse.getReturnData().get(position).getLivingWithName());
        } else if (clickedItem.equalsIgnoreCase("mothertouge")) {
            selectedMothertougeId = getMothertongueBean.getReturnData().get(position).getMothertongueid();
            textViewMothertouge.setText(getMothertongueBean.getReturnData().get(position).getMothertongueName());
        } else if (clickedItem.equalsIgnoreCase("nakshatra")) {
            selectedNakshakraId = getNakshatrasBean.getReturnData().get(position).getNakshatraid();
            textViewNakshakra.setText(getNakshatrasBean.getReturnData().get(position).getNakshatraname());
        } else if (clickedItem.equalsIgnoreCase("bodytype")) {
            selectedBodytypeId = getBodyTypeBean.getReturnData().get(position).getBodytypeid();
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
            selectedFoodhabitsId = getFoodHabitsBean.getReturnData().get(position).getFoodHabitsid();
            textViewFoodhabits.setText(getFoodHabitsBean.getReturnData().get(position).getFoodHabitsName());
        } else if (clickedItem.equalsIgnoreCase("DrinkingHabits")) {
            selectedDrinkingStatusId = getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Id();
            textViewDrinkingStatus.setText(getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("AnnualIncome")) {
            selectedAnnualIncomeId = getAnnualIncome.getReturnData().get(position).getAnnualIncomeId();
            textViewAnnualIncome.setText(getAnnualIncome.getReturnData().get(position).getIncome());
        } else if (clickedItem.equalsIgnoreCase("Height")) {
            selectedHeightId = getHeightResponse.getReturnData().get(position).getHeightID();
            textViewHeight.setText(getHeightResponse.getReturnData().get(position).getHeight1());
        } else if (clickedItem.equalsIgnoreCase("Weight")) {
            selectedWeightId = getWeightResponse.getReturnData().get(position).getWeightID();
            textViewWeight.setText(getWeightResponse.getReturnData().get(position).getWeight1());
        } else if (clickedItem.equalsIgnoreCase("OwnCar")) {
            selectedOwnCarId = getOwnCarResponse.getReturnData().get(position).getOwnCarId();
            textViewOwnCar.setText(getOwnCarResponse.getReturnData().get(position).getOwnCarType());
        } else if (clickedItem.equalsIgnoreCase("OwnHouse")) {
            selectedOwnHouseId = getOwnHouseResponse.getReturnData().get(position).getOwnHouseId();
            textViewOwnHouse.setText(getOwnHouseResponse.getReturnData().get(position).getOwnHouseType());
        } else if (clickedItem.equalsIgnoreCase("Smoking")) {
            selectedSmokingStatusId = getSmokingResponse.getReturnData().get(position).getSmokingId();
            textViewSmoking.setText(getSmokingResponse.getReturnData().get(position).getSmokingType());
        } else if (clickedItem.equalsIgnoreCase("physicalstatus")) {
            selectedPhysicalstatusId = getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusid();
            textViewPhysicalstatus.setText(getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusName());
        } else if (clickedItem.equalsIgnoreCase("dosham")) {
            selectedDoshamId = getDoshamBean.getReturnData().get(position).getDoshamid();
            textViewDosham.setText(getDoshamBean.getReturnData().get(position).getDoshamName());
        } else if (clickedItem.equalsIgnoreCase("matrialStatus")) {
            selectedMatirialStatusId = getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Id();
            textViewMatrialStatus.setText(getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Name());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }
}
