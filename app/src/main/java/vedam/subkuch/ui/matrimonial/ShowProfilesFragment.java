package vedam.subkuch.ui.matrimonial;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentShowProfilesBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.matrimonial.editProfile.EditProfileFragment;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse;
import vedam.subkuch.ui.matrimonial.models.LikeDislike;
import vedam.subkuch.ui.matrimonial.models.LikeDislikeResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowProfilesFragment extends BaseFragment implements CardStackListener {

    private FragmentShowProfilesBinding fragmentShowProfilesBinding;
    private int pageNo = 1;
    private int pageSize = 20;
    private int profileNo = 0;
    private boolean hasMoreProfiles = true;
    ArrayList<DatingProfile> datingProfiles = new ArrayList<>();
    private View vEmptyInflated;
    private CardStackLayoutManager manager;
    private boolean isManual;

    public ShowProfilesFragment() {
        // Required empty public constructor
    }

    public static ShowProfilesFragment newInstance() {

        return new ShowProfilesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentShowProfilesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_profiles, container, false);
        return fragmentShowProfilesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfiles();
    }

    private void getProfiles() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        DataFetcher.getDatingProfile(context, onProfileSuccessListener, DatingProfileResponse.class, onErrorListener, pageNo, pageSize);
    }

    private Response.Listener<DatingProfileResponse> onProfileSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
                if (response.getReturnData() != null && !response.getReturnData().isEmpty()) {
                    hasMoreProfiles = response.getReturnData().size() >= pageSize;
                    datingProfiles = response.getReturnData();
                    setDatingProfile();
                } else
                    showViewStub();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private void setDatingProfile() {

        hideViewStub();
        setAdapter();
        bindValues();
        bindCallbacks();
    }

    private void bindValues() {

        DatingProfile datingProfile = datingProfiles.get(profileNo);
        fragmentShowProfilesBinding.tvName.setText(getNameAndAge(datingProfile));
        UiUtil.setTextView(fragmentShowProfilesBinding.tvAbout, datingProfile.getAboutMe());
        UiUtil.setTextViewWithBoldPrefix(context, "Marital Status :", datingProfile.getMaritalStatusName(), fragmentShowProfilesBinding.tvMaritalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Mother Tongue :", datingProfile.getMothertongueName(), fragmentShowProfilesBinding.tvMotherTongue);
        UiUtil.setTextViewWithBoldPrefix(context, "Occupation :", datingProfile.getOccupationName(), fragmentShowProfilesBinding.tvOccupation);
        UiUtil.setTextViewWithBoldPrefix(context, "Qualification :", datingProfile.getQualificationName(), fragmentShowProfilesBinding.tvQualification);
        UiUtil.setTextViewWithBoldPrefix(context, "Annual Income :", datingProfile.getIncome(), fragmentShowProfilesBinding.tvAnnualIncome);
        UiUtil.setTextViewWithBoldPrefix(context, "Body Type :", datingProfile.getBodyTypeName(), fragmentShowProfilesBinding.tvBodyType);
        UiUtil.setTextViewWithBoldPrefix(context, "Complexion :", datingProfile.getComplexionName(), fragmentShowProfilesBinding.tvComplexion);
        UiUtil.setTextViewWithBoldPrefix(context, "Height :", datingProfile.getHeight(), fragmentShowProfilesBinding.tvHeight);
        UiUtil.setTextViewWithBoldPrefix(context, "Weight :", datingProfile.getWeight(), fragmentShowProfilesBinding.tvWeight);
        UiUtil.setTextViewWithBoldPrefix(context, "Religion :", datingProfile.getReligionName(), fragmentShowProfilesBinding.tvReligion);
        UiUtil.setTextViewWithBoldPrefix(context, "Caste :", datingProfile.getMasterCastName(), fragmentShowProfilesBinding.tvCaste);
        UiUtil.setTextViewWithBoldPrefix(context, "Gothra :", datingProfile.getGotraName(), fragmentShowProfilesBinding.tvGothra);
        UiUtil.setTextViewWithBoldPrefix(context, "Nakshatra :", datingProfile.getNakshatraName(), fragmentShowProfilesBinding.tvNakshatra);
        UiUtil.setTextViewWithBoldPrefix(context, "Dosham :", datingProfile.getDoshamName(), fragmentShowProfilesBinding.tvDosham);
        UiUtil.setTextViewWithBoldPrefix(context, "Food Habit :", datingProfile.getFoodHabitsName(), fragmentShowProfilesBinding.tvFoodHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Drinking Habit :", datingProfile.getDrinkingStatusName(), fragmentShowProfilesBinding.tvDrinkingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Smoking Habit :", datingProfile.getSmokingType(), fragmentShowProfilesBinding.tvSmokingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Physical Status :", datingProfile.getPhysicalStatusName(), fragmentShowProfilesBinding.tvPhysicalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Living With :", datingProfile.getLivingWithName(), fragmentShowProfilesBinding.tvLivingWith);
        UiUtil.setTextViewWithBoldPrefix(context, "Car Status :", datingProfile.getOwnCarType(), fragmentShowProfilesBinding.tvOwnCar);
        UiUtil.setTextViewWithBoldPrefix(context, "House Status :", datingProfile.getOwnHouseType(), fragmentShowProfilesBinding.tvOwnHouse);
    }

    private String getNameAndAge(DatingProfile datingProfile) {

//        String fullName = AppUtil.getFullName(datingProfile.getFirstName(), datingProfile.getLastName());
        String fullName = AppUtil.deNull(datingProfile.getFirstName());
        String age = AppUtil.deNull(datingProfile.getAge());
        if (!TextUtils.isEmpty(age))
            return fullName + ", " + age;
        else
            return fullName;
    }

    private void setAdapter() {

        manager = new CardStackLayoutManager(context, this);
        manager.setCanScrollVertical(false);
        ProfileStackAdapter adapter = new ProfileStackAdapter(context, datingProfiles);
        fragmentShowProfilesBinding.csvProfile.setLayoutManager(manager);
        fragmentShowProfilesBinding.csvProfile.setNestedScrollingEnabled(false);
        fragmentShowProfilesBinding.csvProfile.setAdapter(adapter);
    }

    private void hideViewStub() {

        fragmentShowProfilesBinding.nsvContainer.setVisibility(View.VISIBLE);
        if (vEmptyInflated != null)
            vEmptyInflated.setVisibility(View.GONE);
    }

    private void showViewStub() {

        if (vEmptyInflated == null && fragmentShowProfilesBinding.vsNoProfiles.getViewStub() != null)
            vEmptyInflated = fragmentShowProfilesBinding.vsNoProfiles.getViewStub().inflate();

        setViewStubChildViews();
        vEmptyInflated.setVisibility(View.VISIBLE);
        fragmentShowProfilesBinding.nsvContainer.setVisibility(View.GONE);
    }

    private void setViewStubChildViews() {

        Button btEditProfile = vEmptyInflated.findViewById(R.id.bt_edit_profile);
        btEditProfile.setOnClickListener(v -> {
            addFragmentWithAnimation(R.id.content_frame, EditProfileFragment.newInstance(), Constants.TAG_PROFILE_FRAGMENT, true);
        });
    }

    private void bindCallbacks() {

        fragmentShowProfilesBinding.fabLike.setOnClickListener(v ->
        {
            isManual = true;
            setLikeDislike(Constants.REACTION_TYPE_LIKE);
        });

        fragmentShowProfilesBinding.fabDislike.setOnClickListener(v ->
        {
            isManual = true;
            setLikeDislike(Constants.REACTION_TYPE_DISLIKE);
        });
    }

    private void setLikeDislike(int reactionType) {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));
        swipe(reactionType);

        LikeDislike likeDislike = new LikeDislike();
        String userId = AppPrefs.getPrefsUserId(context);
        likeDislike.setProfileId(userId);
        likeDislike.setReactionType(reactionType);
        likeDislike.setTargetProfileId(datingProfiles.get(profileNo).getProfileId());

        DataFetcher.setLikeDislike(context, new Gson().toJson(likeDislike), onLikeDislikeSuccessListener,
                LikeDislikeResponse.class, onErrorListener);
    }

    private void swipe(int reactionType) {

        Direction direction;
        if (reactionType == Constants.REACTION_TYPE_LIKE)
            direction = Direction.Right;
        else
            direction = Direction.Left;

        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .build();
        manager.setSwipeAnimationSetting(setting);
        fragmentShowProfilesBinding.csvProfile.swipe();
    }

    private Response.Listener<LikeDislikeResponse> onLikeDislikeSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getLikeDislike() != null) {
                UiUtil.showToast(context, getReactionString(response.getLikeDislike().getReactionType()));
                changeUI();
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    private String getReactionString(int reactionType) {
        if (reactionType == Constants.REACTION_TYPE_LIKE)
            return getString(R.string.liked);
        return getString(R.string.disliked);
    }

    private void changeUI() {

        profileNo++;
        if (profileNo < datingProfiles.size()) {
            hideViewStub();
            bindValues();
        } else {
            if (hasMoreProfiles) {
                pageNo++;
                profileNo = 0;
                getProfiles();
            } else
                showViewStub();
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (isManual) {
            isManual = false;
            return;
        }
        if (direction == Direction.Right)
            setLikeDislike(Constants.REACTION_TYPE_LIKE);
        else
            setLikeDislike(Constants.REACTION_TYPE_DISLIKE);
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}
