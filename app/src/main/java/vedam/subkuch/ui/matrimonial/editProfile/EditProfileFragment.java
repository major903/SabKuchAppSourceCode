package vedam.subkuch.ui.matrimonial.editProfile;

import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import vedam.subkuch.network.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseAddImageFragment;
import vedam.subkuch.databinding.FragmentEditProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.DataPart;
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
import vedam.subkuch.network.models.GetSmokingResponse;
import vedam.subkuch.network.models.GetWeightResponse;
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

public class EditProfileFragment extends BaseAddImageFragment implements EditProfileFragmentView, ItemAdapter.ItemClickHandler {

    private FragmentEditProfileBinding binding;
    private String clickedItem = "";

    private GetMasterCastResponse masterCastResponse;
    private GetGotrasBean gotraResponse;
    private GetReligionResponse getReligionResponse;
    private GetBodyTypeBean getBodyTypeBean;
    private GetComplexionBean getComplexionBean;
    private GetDoshamBean getDoshamBean;
    private GetFoodHabitsBean getFoodHabitsBean;
    private GetMothertongueBean getMothertongueBean;
    private GetNakshatrasBean getNakshatrasBean;
    private GetOccupationBean getOccupationBean;
    private GetPhysicalStatusBean getPhysicalStatusBean;
    private GetQualificationBean getQualificationBean;
    private GetAnnualIncome getAnnualIncome;
    private GetHeightResponse getHeightResponse;
    private GetWeightResponse getWeightResponse;
    private GetOwnCarResponse getOwnCarResponse;
    private GetOwnHouseResponse getOwnHouseResponse;
    private GetSmokingResponse getSmokingResponse;
    private GetDrinkingHabits getDrinkingHabits;
    private GetLivingResponse getLivingResponse;
    private GetMaritalStatusResponse getMaritalStatusResponse;

    private EditProfileFragmentPresenter mPresenter;
    private View view;
    private List<String> items = new ArrayList<>();

    private int selectedMaterCastId = 0;
    private int selectedSubCastId = 0;
    private int selectedReligionId = 0;
    private int selectedLivingId = 0;
    private int selectedMatirialStatusId = 0;
    private int selectedNakshakraId = 0;
    private int selectedBodytypeId = 0;
    private int selectedComplexionId = 0;
    private int selectedOccupationId = 0;
    private int selectedQualificationId = 0;
    private int selectedFoodhabitsId = 0;
    private int selectedMothertougeId = 0;
    private int selectedPhysicalstatusId = 0;
    private int selectedDoshamId = 0;
    private int selectedAnnualIncomeId = 0;
    private int selectedDrinkingStatusId = 0;
    private int selectedHeightId = 0;
    private int selectedWeightId = 0;
    private int selectedOwnCarId = 0;
    private int selectedOwnHouseId = 0;
    private int selectedSmokingStatusId = 0;
    private String mEmail = "";
    private String mTokenID = "";
    private String mDeviceID = "";
    private String mUpdatedDate = "";
    private String latitude;
    private String longitude;
    private Stack<Object> object = new Stack<>();
    private boolean isImageLinkPresent;
    private boolean isDating;
    private boolean isInterestedIn;

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        binding.setEditProfileFragment(this);
        setTitle(getString(R.string.edit_profile));
        mPresenter = new EditProfileFragmentPresenter(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isDating)
            hideFields();
        mPresenter.getUserDetail(AppPrefs.getPrefsUserId(mContext), isDating);
        requestLocation();
        setImagesLayout(view);
        bindData();
    }

    private void bindData() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.interested_in_list));
        binding.spInterestedIn.setAdapter(adapter);
        binding.spInterestedIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String interestedIn = parent.getItemAtPosition(position).toString();
                isInterestedIn = Constants.YES.equals(interestedIn);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spInterestedIn.setSelection(1);
    }

    private void hideFields() {
        binding.tvInterestedIn.setText(getString(R.string.interested_in_dating));
        binding.tvReligionHeading.setVisibility(View.GONE);
        binding.relativeLayoutReligion.setVisibility(View.GONE);
        binding.tvCasteHeading.setVisibility(View.GONE);
        binding.relativeLayoutMasterCast.setVisibility(View.GONE);
        binding.tvGotraHeading.setVisibility(View.GONE);
        binding.relativeLayoutGotra.setVisibility(View.GONE);
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
        binding.relativeMatrialStatus.setVisibility(View.GONE);
        binding.tvAnnualIncomeHeading.setVisibility(View.GONE);
        binding.relativeAnnualIncome.setVisibility(View.GONE);
        binding.tvOccupationHeading.setVisibility(View.GONE);
        binding.relativeLayoutOccupation.setVisibility(View.GONE);
        binding.tvFoodHabitHeading.setVisibility(View.GONE);
        binding.relativeLayoutFoodhabits.setVisibility(View.GONE);
        binding.tvWeightHeading.setVisibility(View.GONE);
        binding.relativeWeight.setVisibility(View.GONE);
        binding.tvCarHeading.setVisibility(View.GONE);
        binding.relativeOwnCar.setVisibility(View.GONE);
        binding.tvHouseHeading.setVisibility(View.GONE);
        binding.relativeOwnHouse.setVisibility(View.GONE);
    }

   /* public void setAdapter() {
        adapter = new ImagesAdapter(listOfPhots, getActivity());
        adapter.itemClickHandler(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(adapter);
    }*/


    public void btnUpdateClick(View view) {
        FrequentFunctions.hideKeyBoard(mContext, view);
        if (!isImageLinkPresent && getImageUri() == null) {
            baseshowFeedbackMessage(binding.rootLayout, getString(R.string.add_profile_pricture));
        } else if (selectedBodytypeId == 0) {
            baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_body_type));
        } else if (selectedDrinkingStatusId == 0) {
            baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_drinking_habits));
        } else if (selectedSmokingStatusId == 0) {
            baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_smoking_status));
        } else if (selectedHeightId == 0) {
            baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_height));
        } else if (!isDating) {
            if (selectedReligionId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_religion));
            } else if (selectedOccupationId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_occupation));
            } else if (selectedFoodhabitsId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_food_habits));
            } else if (selectedWeightId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_weight));
            } else if (selectedComplexionId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_complexion));
            } else if (selectedQualificationId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_qualification));
            } else if (selectedPhysicalstatusId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_physical_status));
            } else if (selectedMothertougeId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_mother_tongue));
            } else if (selectedAnnualIncomeId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_annual_income));
            } else if (selectedOwnCarId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_car_status));
            } else if (selectedOwnHouseId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_house_status));
            } else if (selectedLivingId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_living_with));
            } else if (selectedMatirialStatusId == 0) {
                baseshowFeedbackMessage(binding.rootLayout, getString(R.string.empty_matrialstatus));
            } else
                updateProfile();
        } else
            updateProfile();
    }

    private void updateProfile() {

        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setProfileId(Integer.parseInt(AppPrefs.getPrefsUserId(mContext)));
        updateProfileRequest.setAboutMe(AppUtil.deNull(binding.etAboutMe.getText()));
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
            updateProfileRequest.setDating(isInterestedIn);
        else
            updateProfileRequest.setMatrimonial(isInterestedIn);
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
    }

    private String getNotInterestedString() {

        if (isDating)
            return getString(R.string.no_interest_dating);
        else
            return getString(R.string.no_interest_matrimonial);
    }

    private void uploadProfileImage() {
        object.add(new Object());
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
        Map<String, DataPart> params = new HashMap<>();
        params.put(NetworkConstants.ProfileImage, new DataPart(AppUtil.getUniqueFileName(),
                AppUtil.getBytesFromBitmap(AppUtil.getBitmap(mContext, getImageUri()))
                , NetworkConstants.JPEG_MIME_TYPE));

//        ImageIdRequest imageIdRequest = new ImageIdRequest();
       /* ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        imageIdRequest.setImageIds(arrayList);
        System.out.println(new Gson().toJson(imageIdRequest));*/

        if (isDating)
            DataFetcher.uploadDatingProfileImage(mContext, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
        else
            DataFetcher.uploadMatrimonialProfileImage(mContext, params, onImageUploadSuccessListener, GeneralResponse.class, onErrorListener);
    }

    private Response.Listener<GeneralResponse> onImageUploadSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null) {
            object.pop();
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                checkAndFinish();
            } else
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
        }
    };

    private void checkAndFinish() {

        if (object.isEmpty() && getFragmentManager() != null) {
            UiUtil.cancelProgressDialog();
            UiUtil.showToast(getActivity(), getString(R.string.profile_updated));
            getFragmentManager().popBackStack();
        }
    }

    public void relativeLayoutLivingClick(View view) {
        clickedItem = "livingWith";
        this.view = view;
        mPresenter.getLiving();
    }

    public void relativeMatrialStatusClick(View view) {
        clickedItem = "matrialStatus";
        this.view = view;
        mPresenter.getMatrialStatus();
    }

    public void relativeLayoutReligionClick(View view) {
        clickedItem = "religion";
        this.view = view;
        mPresenter.getReligion();
    }

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


    public void relativeLayoutMothertougeClick(View view) {
        clickedItem = "mothertouge";
        this.view = view;
        mPresenter.getMothertouge();
    }

    public void relativeLayoutGotraClick(View view) {
        if (selectedMaterCastId == 0) {
            baseshowFeedbackMessage(view, getString(R.string.select_master_cast_first));
        } else {
            clickedItem = "gotra";
            this.view = view;
            mPresenter.getGotra(selectedMaterCastId + "");
        }
    }

    public void relativeLayoutNakshakraClick(View view) {
        clickedItem = "nakshatra";
        this.view = view;
        mPresenter.getNakshakra();
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

    public void relativeLayoutDrinkingHabitsClick(View view) {
        clickedItem = "DrinkingHabits";
        this.view = view;
        mPresenter.getDrinkingHabits();
    }


    public void relativeLayoutAnnualIncomeClick(View view) {
        clickedItem = "AnnualIncome";
        this.view = view;
        mPresenter.getAnnualIncome();
    }

    public void relativeHeightClick(View view) {
        clickedItem = "Height";
        this.view = view;
        mPresenter.getHeight();
    }

    public void relativeWeightClick(View view) {
        clickedItem = "Weight";
        this.view = view;
        mPresenter.getWeight();
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

    @Override
    public void showProgressBar() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait));
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
        if (getActivity() != null) {
            object.pop();
            checkAndFinish();
        }
    }

    @Override
    public void onSuccessfullyGetUserDetail(GetUserDetailResponse response) {

        GetUserDetailResponse.ReturnDataBean returnDataBean = response.getReturnData().get(0);

        if (returnDataBean.getImagesList() != null && returnDataBean.getImagesList().length > 0) {
            String imageLink = returnDataBean.getImagesList()[0].getImage();
            if (!TextUtils.isEmpty(imageLink)) {
                isImageLinkPresent = true;
                UiUtil.setImageView(new ImageSetter.ImageBuilder(mContext)
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

        binding.etAboutMe.setText(returnDataBean.getAboutMe());
        binding.textViewCast.setText(returnDataBean.getMasterCastName());
        selectedMaterCastId = returnDataBean.getCasteId();

        binding.textViewGotra.setText(returnDataBean.getGotraName());
        selectedSubCastId = returnDataBean.getGotraid();

        binding.textViewNakshakra.setText(returnDataBean.getNakshatraName());
        selectedNakshakraId = returnDataBean.getNakshakraid();

        binding.textViewBodytype.setText(returnDataBean.getBodyTypeName());
        selectedBodytypeId = returnDataBean.getBodyTypeid();

        binding.textViewComplexion.setText(returnDataBean.getComplexionName());
        selectedComplexionId = returnDataBean.getComplexionid();

        binding.textViewOccupation.setText(returnDataBean.getOccupationName());
        selectedOccupationId = returnDataBean.getOccupationid();

        binding.textViewQualification.setText(returnDataBean.getQualificationName());
        selectedQualificationId = returnDataBean.getQualificationid();

        binding.textViewFoodhabits.setText(returnDataBean.getFoodHabitsName());
        selectedFoodhabitsId = returnDataBean.getFoodHabitsid();

        binding.textViewDrinkingHabits.setText(returnDataBean.getDrinkingStatusName());
        selectedDrinkingStatusId = returnDataBean.getDrinkingStatusid();

        binding.textViewPhysicalstatus.setText(returnDataBean.getPhysicalStatusName());
        selectedPhysicalstatusId = returnDataBean.getPhysicalStatusid();

        binding.textViewDosham.setText(returnDataBean.getDoshamName());
        selectedDoshamId = returnDataBean.getDoshamid();

        binding.textViewMothertouge.setText(returnDataBean.getMothertongueName());
        selectedMothertougeId = returnDataBean.getMotherTougeid();


        binding.textViewLivingWith.setText(returnDataBean.getLivingWithName());
        selectedLivingId = returnDataBean.getLivingWithId();

        binding.textViewAnnualIncome.setText(returnDataBean.getIncome());
        selectedAnnualIncomeId = returnDataBean.getAnualIncomeid();

        binding.textViewHeight.setText(returnDataBean.getHeight());
        selectedHeightId = returnDataBean.getHeightId();

        binding.textViewWeight.setText(returnDataBean.getWeight());
        selectedWeightId = returnDataBean.getWeightId();

        binding.textViewOwnCar.setText(returnDataBean.getOwnCarType());
        selectedOwnCarId = returnDataBean.getOwnCarId();

        binding.textViewOwnHouse.setText(returnDataBean.getOwnHouseType());
        selectedOwnHouseId = returnDataBean.getOwnHouseId();

        binding.textViewSmoking.setText(returnDataBean.getSmokingType());
        selectedSmokingStatusId = returnDataBean.getSmokingId();
        //textViewPreperdType.setText(response.getReturnData().getPrefferedtype());
        //selectedPreffedType = response.getReturnData().getPrefferedtype();


        selectedMatirialStatusId = returnDataBean.getMatrialStatusid();
        binding.textViewMatrialStatus.setText(returnDataBean.getMaritalStatusName());
        binding.textViewReligion.setText(returnDataBean.getReligionName());
        selectedReligionId = returnDataBean.getReligionId();

        if (isDating)
            setSelection(returnDataBean.isDating());
        else
            setSelection(returnDataBean.isMatrimonial());
        //setData(response);
    }

    private void setSelection(boolean datingOrMatrimonial) {
        if (datingOrMatrimonial)
            binding.spInterestedIn.setSelection(0);
        else
            binding.spInterestedIn.setSelection(1);
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
            binding.textViewCast.setText(masterCastResponse.getReturnData().get(position).getMasterCastName());
            binding.textViewGotra.setText("");
        } else if (clickedItem.equalsIgnoreCase("gotra")) {
            selectedSubCastId = gotraResponse.getReturnData().get(position).getGotrasid();
            binding.textViewGotra.setText(gotraResponse.getReturnData().get(position).getName());
        } else if (clickedItem.equalsIgnoreCase("religion")) {
            selectedReligionId = getReligionResponse.getReturnData().get(position).getReligionID();
            binding.textViewReligion.setText(getReligionResponse.getReturnData().get(position).getReligionName());
        } else if (clickedItem.equalsIgnoreCase("livingWith")) {
            selectedLivingId = getLivingResponse.getReturnData().get(position).getLivingWithId();
            binding.textViewLivingWith.setText(getLivingResponse.getReturnData().get(position).getLivingWithName());
        } else if (clickedItem.equalsIgnoreCase("mothertouge")) {
            selectedMothertougeId = getMothertongueBean.getReturnData().get(position).getMothertongueid();
            binding.textViewMothertouge.setText(getMothertongueBean.getReturnData().get(position).getMothertongueName());
        } else if (clickedItem.equalsIgnoreCase("nakshatra")) {
            selectedNakshakraId = getNakshatrasBean.getReturnData().get(position).getNakshatraid();
            binding.textViewNakshakra.setText(getNakshatrasBean.getReturnData().get(position).getNakshatraname());
        } else if (clickedItem.equalsIgnoreCase("bodytype")) {
            selectedBodytypeId = getBodyTypeBean.getReturnData().get(position).getBodytypeid();
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
            selectedFoodhabitsId = getFoodHabitsBean.getReturnData().get(position).getFoodHabitsid();
            binding.textViewFoodhabits.setText(getFoodHabitsBean.getReturnData().get(position).getFoodHabitsName());
        } else if (clickedItem.equalsIgnoreCase("DrinkingHabits")) {
            selectedDrinkingStatusId = getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Id();
            binding.textViewDrinkingHabits.setText(getDrinkingHabits.getReturnData().get(position).getDrinkingStatus_Name());
        } else if (clickedItem.equalsIgnoreCase("AnnualIncome")) {
            selectedAnnualIncomeId = getAnnualIncome.getReturnData().get(position).getAnnualIncomeId();
            binding.textViewAnnualIncome.setText(getAnnualIncome.getReturnData().get(position).getIncome());
        } else if (clickedItem.equalsIgnoreCase("Height")) {
            selectedHeightId = getHeightResponse.getReturnData().get(position).getHeightID();
            binding.textViewHeight.setText(getHeightResponse.getReturnData().get(position).getHeight1());
        } else if (clickedItem.equalsIgnoreCase("Weight")) {
            selectedWeightId = getWeightResponse.getReturnData().get(position).getWeightID();
            binding.textViewWeight.setText(getWeightResponse.getReturnData().get(position).getWeight1());
        } else if (clickedItem.equalsIgnoreCase("OwnCar")) {
            selectedOwnCarId = getOwnCarResponse.getReturnData().get(position).getOwnCarId();
            binding.textViewOwnCar.setText(getOwnCarResponse.getReturnData().get(position).getOwnCarType());
        } else if (clickedItem.equalsIgnoreCase("OwnHouse")) {
            selectedOwnHouseId = getOwnHouseResponse.getReturnData().get(position).getOwnHouseId();
            binding.textViewOwnHouse.setText(getOwnHouseResponse.getReturnData().get(position).getOwnHouseType());
        } else if (clickedItem.equalsIgnoreCase("Smoking")) {
            selectedSmokingStatusId = getSmokingResponse.getReturnData().get(position).getSmokingId();
            binding.textViewSmoking.setText(getSmokingResponse.getReturnData().get(position).getSmokingType());
        } else if (clickedItem.equalsIgnoreCase("physicalstatus")) {
            selectedPhysicalstatusId = getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusid();
            binding.textViewPhysicalstatus.setText(getPhysicalStatusBean.getReturnData().get(position).getPhysicalStatusName());
        } else if (clickedItem.equalsIgnoreCase("dosham")) {
            selectedDoshamId = getDoshamBean.getReturnData().get(position).getDoshamid();
            binding.textViewDosham.setText(getDoshamBean.getReturnData().get(position).getDoshamName());
        } else if (clickedItem.equalsIgnoreCase("matrialStatus")) {
            selectedMatirialStatusId = getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Id();
            binding.textViewMatrialStatus.setText(getMaritalStatusResponse.getReturnData().get(position).getMaritalStatus_Name());
        }
    }

    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }
}
