package vedam.subkuch.ui.dating.editProfile;

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
import vedam.subkuch.network.DataPart;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.handler.AllLocalHandler;
import vedam.subkuch.network.models.GeneralResponse;
import vedam.subkuch.network.models.GetAnnualIncome;
import vedam.subkuch.network.models.GetBodyTypeBean;
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
import vedam.subkuch.network.models.UserDetail.GetUserDetailResponse;
import vedam.subkuch.network.models.UserDetail.UpdateProfileRequest;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.dating.editProfile.presenter.EditProfileFragmentPresenter;
import vedam.subkuch.ui.dating.editProfile.view.EditProfileFragmentView;
import vedam.subkuch.ui.dating.preference.ItemAdapter;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.FrequentFunctions;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;
import vedam.subkuch.utils.Validations;

public class EditProfileFragment extends BaseAddImageFragment implements AllLocalHandler, EditProfileFragmentView, ItemAdapter.ItemClickHandler {

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

    @BindView(R.id.editTextHeight)
    EditText editTextHeight;

    @BindView(R.id.editTextWeight)
    EditText editTextWeight;

    @BindView(R.id.relativeLayoutLiving)
    RelativeLayout relativeLayoutLiving;

    @BindView(R.id.btnUpdate)
    TextView btnUpdate;

    @BindView(R.id.switchCar)
    Switch switchCar;

    @BindView(R.id.switchIsSmoke)
    Switch switchIsSmoke;

    @BindView(R.id.switchHouse)
    Switch switchHouse;

    @BindView(R.id.switchMatrimonial)
    Switch switchMatrimonial;

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
    @BindView(R.id.editTextAge)
    EditText editTextAge;
    @BindView(R.id.editTextFirstName)
    EditText editTextFirstName;
    @BindView(R.id.editTextLastName)
    EditText editTextLastName;
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
    int selectedCityId = -1;

    private Unbinder unbinder;
    private String mEmail = "";
    private String mTokenID = "";
    private String mDeviceID = "";
    private String mUpdatedDate = "";
    private String latitude;
    private String longitude;
    private Stack<Object> object = new Stack<>();

    public static EditProfileFragment newInstance() {

        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        setTitle(getString(R.string.edit_profile));
        mPresenter = new EditProfileFragmentPresenter(this);
        mPresenter.getUserDetail(AppPrefs.getPrefsUserId(context));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestLocation();
        setImagesLayout(view);
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
        if (Validations.isFieldEmpty(editTextFirstName.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_first_name));
        } else if (Validations.isFieldEmpty(editTextLastName.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_lst_name));
        } else if (getImageUri() == null) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.add_profile_pricture));
        } else if (selectedReligionId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_religion));
        } else if (selectedMaterCastId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_cast));
        } else if (Validations.isFieldEmpty(textViewGotra.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_sub_cast));
        } else if (selectedLivingId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_living_with));
        } else if (Validations.isFieldEmpty(editTextHeight.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_height));
        } else if (Validations.isFieldEmpty(editTextWeight.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_weight));
        } else if (Validations.isFieldEmpty(editTextAge.getText().toString())) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_age));
        } else if (selectedMatirialStatusId == -1) {
            baseshowFeedbackMessage(rootLayout, getString(R.string.empty_matrialstatus));
        } else {
            boolean ownCar;
            boolean ownHouse;
            boolean smokingstatus;

            ownCar = switchCar.isChecked();

            ownHouse = switchHouse.isChecked();

            smokingstatus = switchIsSmoke.isChecked();

            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
            updateProfileRequest.setProfileId(Integer.parseInt(AppPrefs.getPrefsUserId(context)));
            updateProfileRequest.setFirstName(editTextFirstName.getText().toString());
            updateProfileRequest.setLastName(editTextLastName.getText().toString());
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
            updateProfileRequest.setOwnCar(ownCar);
            updateProfileRequest.setOwnHouse(ownHouse);
            updateProfileRequest.setLivingWithId(selectedLivingId);
            updateProfileRequest.setMatrimonial(switchMatrimonial.isChecked());
            updateProfileRequest.setHeight(editTextHeight.getText().toString());
            updateProfileRequest.setWeight(editTextWeight.getText().toString());
            updateProfileRequest.setAge(editTextAge.getText().toString());
            updateProfileRequest.setGotraid(selectedSubCastId);
            updateProfileRequest.setNakshakraid(selectedNakshakraId);
            updateProfileRequest.setBodyTypeid(selectedBodytypeId);
            updateProfileRequest.setComplexionid(selectedComplexionId);
            updateProfileRequest.setOccupationid(selectedOccupationId);
            updateProfileRequest.setQualificationid(selectedQualificationId);
            updateProfileRequest.setAnualIncomeid(selectedAnnualIncomeId);
            updateProfileRequest.setIsSmoking(smokingstatus);
            updateProfileRequest.setDrinkingStatusid(selectedDrinkingStatusId);
            updateProfileRequest.setFoodHabitsid(selectedFoodhabitsId);
            updateProfileRequest.setMotherTougeid(selectedMothertougeId);
            updateProfileRequest.setPhysicalStatusid(selectedPhysicalstatusId);
            updateProfileRequest.setMatrialStatusid(selectedMatirialStatusId);
            updateProfileRequest.setDoshamid(selectedDoshamId);
            updateProfileRequest.setOccupationOther("");
            updateProfileRequest.setUpdatedDate(mUpdatedDate);
            object.add(new Object());

            mPresenter.updateProfile(updateProfileRequest);

            if (getImageUri() != null)
                uploadProfileImage();
            //mPresenter.updateMaterimonial(Constants.USER_ID, selectedReligionId + "", selectedMaterCastId + "", selectedSubCastId + "", ownCar, ownHouse, selectedLivingId + "", "123", selectedPreffedType, editTextHeight.getText().toString(), editTextWeight.getText().toString(), editTextAge.getText().toString(), "2121.212", "65656.2212", selectedNakshakraId + "", selectedBodytypeId + "", selectedComplexionId + "", selectedOccupationId + "", selectedQualificationId + "", editTextIncome.getText().toString(), smokingstatus, drinkingstatus, selectedFoodhabitsId + "", selectedMothertougeId + "", selectedPhysicalstatusId + "", selectedMatirialStatusId + "", selectedDoshamId + "");
        }
    }

    private void uploadProfileImage() {
        object.add(new Object());
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getBitmap(context, getImageUri()))
                , NetworkConstants.JPEG_MIME_TYPE));
        DataFetcher.uploadProfileImage(context, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null) {
            object.pop();
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                getFragmentManager().popBackStack();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
        }
    };

    private void checkAndFinish() {

        if (object.isEmpty() && getGlobalFragmentInteractionListener() != null) {
            UiUtil.cancelProgressDialog();
            UiUtil.showToast(getActivity(), getString(R.string.profile_updated));
            getGlobalFragmentInteractionListener().finishActivity();
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

        String imageLink = returnDataBean.getImage();
        if (TextUtils.isEmpty(imageLink))
            imageLink = "junk";
        UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                .setImageLink(imageLink)
                .setDefaults()
                .setTarget(ivPicture).build());

        mEmail = returnDataBean.getEMail();
        mTokenID = returnDataBean.getTokenId();
        mDeviceID = returnDataBean.getDeviceId();
        mUpdatedDate = FrequentFunctions.getCurrentDateTime();

        editTextFirstName.setText(returnDataBean.getFirstName());

        editTextLastName.setText(returnDataBean.getLastName());

        etAboutMe.setText(returnDataBean.getAboutMe());
        textViewCast.setText(returnDataBean.getMasterCastName());
        selectedMaterCastId = returnDataBean.getCasteId();

        textViewGotra.setText(returnDataBean.getGotraName());
        selectedSubCastId = Integer.parseInt(returnDataBean.getGotraid() + "");

        textViewNakshakra.setText(returnDataBean.getNakshatraName());
        selectedNakshakraId = Integer.parseInt(returnDataBean.getNakshakraid() + "");

        textViewBodytype.setText(returnDataBean.getBodyTypeName());
        selectedBodytypeId = Integer.parseInt(returnDataBean.getBodyTypeid() + "");

        textViewComplexion.setText(returnDataBean.getComplexionName());
        selectedComplexionId = Integer.parseInt(returnDataBean.getComplexionid() + "");

        textViewOccupation.setText(returnDataBean.getOccupationName());
        selectedOccupationId = Integer.parseInt(returnDataBean.getOccupationid() + "");

        textViewQualification.setText(returnDataBean.getQualificationName());
        selectedQualificationId = Integer.parseInt(returnDataBean.getQualificationid() + "");

        textViewFoodhabits.setText(returnDataBean.getFoodHabitsName());
        selectedFoodhabitsId = Integer.parseInt(returnDataBean.getFoodHabitsid() + "");

        textViewDrinkingStatus.setText(returnDataBean.getDrinkingStatusName());
        selectedDrinkingStatusId = Integer.parseInt(returnDataBean.getDrinkingStatusid() + "");

        textViewPhysicalstatus.setText(returnDataBean.getPhysicalStatusName());
        selectedPhysicalstatusId = Integer.parseInt(returnDataBean.getPhysicalStatusid() + "");

        textViewDosham.setText(returnDataBean.getDoshamName());
        selectedDoshamId = Integer.parseInt(returnDataBean.getDoshamid() + "");

        textViewMothertouge.setText(returnDataBean.getMothertongueName());
        selectedMothertougeId = Integer.parseInt(returnDataBean.getMotherTougeid() + "");


        textViewLivingWith.setText(returnDataBean.getLivingWithName());
        selectedLivingId = returnDataBean.getLivingWithId();

        textViewAnnualIncome.setText(returnDataBean.getIncome());
        selectedAnnualIncomeId = Integer.parseInt(returnDataBean.getAnualIncomeid() + "");
        //textViewPreperdType.setText(response.getReturnData().getPrefferedtype());
        //selectedPreffedType = response.getReturnData().getPrefferedtype();


        selectedMatirialStatusId = returnDataBean.getMatrialStatusid();
        textViewMatrialStatus.setText(returnDataBean.getMaritalStatusName());
        editTextHeight.setText(returnDataBean.getHeight());
        editTextAge.setText(returnDataBean.getAge());
        editTextWeight.setText(returnDataBean.getWeight());
        textViewReligion.setText(returnDataBean.getReligionName());
        selectedReligionId = returnDataBean.getReligionId();
        if (returnDataBean.isOwnCar()) {
            switchCar.setChecked(true);
        } else {
            switchCar.setChecked(false);
        }

        if (returnDataBean.isOwnHouse()) {
            switchHouse.setChecked(true);
        } else {
            switchHouse.setChecked(false);
        }

        switchMatrimonial.setChecked(returnDataBean.isMatrimonial());
        switchIsSmoke.setChecked(returnDataBean.isIsSmoking());
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
        ItemAdapter adapter = new ItemAdapter(getActivity(), items);
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
