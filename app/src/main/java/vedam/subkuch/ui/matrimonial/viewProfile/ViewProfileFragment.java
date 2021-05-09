package vedam.subkuch.ui.matrimonial.viewProfile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
    private FragmentViewProfileBinding binding;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        bindCallback();
    }

    private void bindCallback() {

        binding.fabDislike.setOnClickListener(v -> showWarning());
    }

    private void showWarning() {

        UiUtil.showConfirmationDialog(context, getString(R.string.are_you_sure_unmatch), (dialog, which) -> setUnmatch(), (dialog, which) -> dialog.dismiss(), true);
    }

    private void initUI() {

        if (datingProfile.getImagesList() != null && datingProfile.getImagesList().size() > 0) {
            String imageLink = datingProfile.getImagesList().get(0).getImage();
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(imageLink)
                    .setPlaceholderResource(R.drawable.placeholder)
                    .setErrorResource(R.drawable.placeholder)
                    .setTarget(binding.ivProfile)
                    .build());
        } else
            binding.ivProfile.setBackgroundResource(R.drawable.placeholder);

        binding.cvImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.cvImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setLayoutParams();
            }
        });

        binding.tvName.setText(AppUtil.getNameAndAge(datingProfile.getFirstName(), datingProfile.getAge()));

        UiUtil.setTextView(binding.tvAbout, datingProfile.getAboutMe());
        UiUtil.setTextView(binding.tvDistance, datingProfile.getDistance());
        UiUtil.setTextViewWithBoldPrefix(context, "Marital Status :", datingProfile.getMaritalStatusName(), binding.tvMaritalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Mother Tongue :", datingProfile.getMothertongueName(), binding.tvMotherTongue);
        UiUtil.setTextViewWithBoldPrefix(context, "Occupation :", datingProfile.getOccupationName(), binding.tvOccupation);
        UiUtil.setTextViewWithBoldPrefix(context, "Qualification :", datingProfile.getQualificationName(), binding.tvQualification);
        UiUtil.setTextViewWithBoldPrefix(context, "Annual Income :", datingProfile.getIncome(), binding.tvAnnualIncome);
        UiUtil.setTextViewWithBoldPrefix(context, "Body Type :", datingProfile.getBodyTypeName(), binding.tvBodyType);
        UiUtil.setTextViewWithBoldPrefix(context, "Complexion :", datingProfile.getComplexionName(), binding.tvComplexion);
        UiUtil.setTextViewWithBoldPrefix(context, "Height :", datingProfile.getHeight(), binding.tvHeight);
        if (!isDating)
            UiUtil.setTextViewWithBoldPrefix(context, "Weight :", datingProfile.getWeight(), binding.tvWeight);
        else
            binding.tvWeight.setVisibility(View.GONE);
        UiUtil.setTextViewWithBoldPrefix(context, "Religion :", datingProfile.getReligionName(), binding.tvReligion);
        UiUtil.setTextViewWithBoldPrefix(context, "Caste :", datingProfile.getMasterCastName(), binding.tvCaste);
        UiUtil.setTextViewWithBoldPrefix(context, "Gothra :", datingProfile.getGotraName(), binding.tvGothra);
        UiUtil.setTextViewWithBoldPrefix(context, "Nakshatra :", datingProfile.getNakshatraName(), binding.tvNakshatra);
        UiUtil.setTextViewWithBoldPrefix(context, "Dosham :", datingProfile.getDoshamName(), binding.tvDosham);
        UiUtil.setTextViewWithBoldPrefix(context, "Food Habit :", datingProfile.getFoodHabitsName(), binding.tvFoodHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Drinking Habit :", datingProfile.getDrinkingStatusName(), binding.tvDrinkingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Smoking Habit :", datingProfile.getSmokingType(), binding.tvSmokingHabit);
        UiUtil.setTextViewWithBoldPrefix(context, "Physical Status :", datingProfile.getPhysicalStatusName(), binding.tvPhysicalStatus);
        UiUtil.setTextViewWithBoldPrefix(context, "Living With :", datingProfile.getLivingWithName(), binding.tvLivingWith);
        UiUtil.setTextViewWithBoldPrefix(context, "Owns a Car :", datingProfile.getOwnCarType(), binding.tvOwnCar);
        UiUtil.setTextViewWithBoldPrefix(context, "Owns a House :", datingProfile.getOwnHouseType(), binding.tvOwnHouse);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setLayoutParams() {

        int width = binding.cvImage.getMeasuredWidth();
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(width, width);
        params.bottomMargin = AppUtil.dpToPx(context, 28);
        binding.cvImage.setLayoutParams(params);
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

    private final Response.Listener<LikeDislikeResponse> onLikeDislikeSuccessListener = response -> {

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
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.chat2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_chats) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.getFirstName()));
            intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.getProfileId());
            intent.putExtra(Constants.EXTRA_IS_DATING, isDating);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
