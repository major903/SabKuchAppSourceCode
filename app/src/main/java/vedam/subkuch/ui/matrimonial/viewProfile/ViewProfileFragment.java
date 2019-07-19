package vedam.subkuch.ui.matrimonial.viewProfile;


import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentViewProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.chat.ChatActivity;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.ui.matrimonial.models.LikeDislike;
import vedam.subkuch.ui.matrimonial.models.LikeDislikeResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends BaseFragment {

    private DatingProfile datingProfile;
    private FragmentViewProfileBinding fragmentViewProfileBinding;
    private boolean isDating;

    public ViewProfileFragment() {
        // Required empty public constructor
    }


    public static ViewProfileFragment newInstance(Bundle extras) {

        ViewProfileFragment fragment = new ViewProfileFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            datingProfile = getArguments().getParcelable(Constants.EXTRA_DATA);
            isDating = getArguments().getBoolean(Constants.EXTRA_IS_DATING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        fragmentViewProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_profile, container, false);
        return fragmentViewProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        bindCallback();
    }

    private void bindCallback() {

        fragmentViewProfileBinding.fabDislike.setOnClickListener(v -> showWarning());
    }

    private void showWarning() {

        UiUtil.showConfirmationDialog(context, getString(R.string.are_you_sure_unmatch), (dialog, which) -> setUnmatch(), (dialog, which) -> dialog.dismiss(), true);
    }

    private void initUI() {

        if (datingProfile.getImagesList() != null && datingProfile.getImagesList().length > 0) {
            String imageLink = datingProfile.getImagesList()[0].getImage();
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(imageLink)
                    .setPlaceholderResource(R.drawable.placeholder)
                    .setErrorResource(R.drawable.placeholder)
                    .setTarget(fragmentViewProfileBinding.ivProfile)
                    .build());
        } else
            fragmentViewProfileBinding.ivProfile.setBackgroundResource(R.drawable.placeholder);

        fragmentViewProfileBinding.cvImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fragmentViewProfileBinding.cvImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setLayoutParams();
            }
        });

        fragmentViewProfileBinding.tvName.setText(AppUtil.getNameAndAge(datingProfile.getFirstName(), datingProfile.getAge()));

        UiUtil.setTextView(fragmentViewProfileBinding.tvAbout, datingProfile.getAboutMe());
        UiUtil.setTextView(fragmentViewProfileBinding.tvDistance, datingProfile.getDistance());
        UiUtil.setTextViewWithBoldPrefix(context, "Marital Status :", datingProfile.getMaritalStatusName(), fragmentViewProfileBinding.tvMaritalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Mother Tongue :", datingProfile.getMothertongueName(), fragmentViewProfileBinding.tvMotherTongue);
        UiUtil.setTextViewWithBoldPrefix(context, "Occupation :", datingProfile.getOccupationName(), fragmentViewProfileBinding.tvOccupation);
        UiUtil.setTextViewWithBoldPrefix(context, "Qualification :", datingProfile.getQualificationName(), fragmentViewProfileBinding.tvQualification);
        UiUtil.setTextViewWithBoldPrefix(context, "Annual Income :", datingProfile.getIncome(), fragmentViewProfileBinding.tvAnnualIncome);
        UiUtil.setTextViewWithBoldPrefix(context, "Body Type :", datingProfile.getBodyTypeName(), fragmentViewProfileBinding.tvBodyType);
        UiUtil.setTextViewWithBoldPrefix(context, "Complexion :", datingProfile.getComplexionName(), fragmentViewProfileBinding.tvComplexion);
        UiUtil.setTextViewWithBoldPrefix(context, "Height :", datingProfile.getHeight(), fragmentViewProfileBinding.tvHeight);
        UiUtil.setTextViewWithBoldPrefix(context, "Weight :", datingProfile.getWeight(), fragmentViewProfileBinding.tvWeight);
        UiUtil.setTextViewWithBoldPrefix(context, "Religion :", datingProfile.getReligionName(), fragmentViewProfileBinding.tvReligion);
        UiUtil.setTextViewWithBoldPrefix(context, "Caste :", datingProfile.getMasterCastName(), fragmentViewProfileBinding.tvCaste);
        UiUtil.setTextViewWithBoldPrefix(context, "Gothra :", datingProfile.getGotraName(), fragmentViewProfileBinding.tvGothra);
        UiUtil.setTextViewWithBoldPrefix(context, "Nakshatra :", datingProfile.getNakshatraName(), fragmentViewProfileBinding.tvNakshatra);
        UiUtil.setTextViewWithBoldPrefix(context, "Dosham :", datingProfile.getDoshamName(), fragmentViewProfileBinding.tvDosham);
        UiUtil.setTextViewWithBoldPrefix(context, "Food Habit :", datingProfile.getFoodHabitsName(), fragmentViewProfileBinding.tvFoodHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Drinking Habit :", datingProfile.getDrinkingStatusName(), fragmentViewProfileBinding.tvDrinkingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Smoking Habit :", datingProfile.getSmokingType(), fragmentViewProfileBinding.tvSmokingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Physical Status :", datingProfile.getPhysicalStatusName(), fragmentViewProfileBinding.tvPhysicalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Living With :", datingProfile.getLivingWithName(), fragmentViewProfileBinding.tvLivingWith);
        UiUtil.setTextViewWithBoldPrefix(context, "Owns a Car :", datingProfile.getOwnCarType(), fragmentViewProfileBinding.tvOwnCar);
        UiUtil.setTextViewWithBoldPrefix(context, "Owns a House :", datingProfile.getOwnHouseType(), fragmentViewProfileBinding.tvOwnHouse);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setLayoutParams() {

        int width = fragmentViewProfileBinding.cvImage.getMeasuredWidth();
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, width);
        params.bottomMargin = AppUtil.dpToPx(context, 28);
        fragmentViewProfileBinding.cvImage.setLayoutParams(params);
    }

    private void setUnmatch() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait));

        LikeDislike likeDislike = new LikeDislike();
        String userId = AppPrefs.getPrefsUserId(context);
        likeDislike.setProfileId(userId);
        likeDislike.setReactionType(Constants.REACTION_TYPE_UN_MATCH);
        likeDislike.setTargetProfileId(datingProfile.getProfileId());

        if (isDating)
            DataFetcher.setDatingLikeDislike(context, new Gson().toJson(likeDislike), onLikeDislikeSuccessListener,
                    LikeDislikeResponse.class, onErrorListener);
        else
            DataFetcher.setMatrimonyLikeDislike(context, new Gson().toJson(likeDislike), onLikeDislikeSuccessListener,
                    LikeDislikeResponse.class, onErrorListener);
    }

    private Response.Listener<LikeDislikeResponse> onLikeDislikeSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (getActivity() != null)
            if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getLikeDislike() != null) {
                UiUtil.showToast(context, getString(R.string.unmatched));
                if (getGlobalFragmentInteractionListener() != null)
                    getGlobalFragmentInteractionListener().setFragmentResult(Activity.RESULT_OK, null);
            } else
                UiUtil.showToast(context, getString(R.string.err_occurred));
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chat, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chats:
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.getFirstName()));
                intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.getProfileId());
                intent.putExtra(Constants.EXTRA_IS_DATING, isDating);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
