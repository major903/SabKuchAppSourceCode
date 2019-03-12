package vedam.subkuch.ui.matrimonial.viewProfile;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentViewProfileBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.ui.matrimonial.models.DatingProfile;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.ImageSetter;
import vedam.subkuch.utils.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends BaseFragment {

    private DatingProfile datingProfile;
    private FragmentViewProfileBinding fragmentViewProfileBinding;

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
        if (getArguments() != null)
            datingProfile = getArguments().getParcelable(Constants.EXTRA_DATA);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentViewProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_profile, container, false);
        return fragmentViewProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {

        if (datingProfile.getImagesList() != null && datingProfile.getImagesList().length > 0) {
            String imageLink = datingProfile.getImagesList()[0].getImage();
            UiUtil.setImageView(new ImageSetter.ImageBuilder(context)
                    .setImageLink(imageLink)
                    .setDefaults()
                    .setTarget(fragmentViewProfileBinding.ivPicture)
                    .build());
        } else
            fragmentViewProfileBinding.ivPicture.setVisibility(View.GONE);

        String fullName = AppUtil.getFullName(datingProfile.getFirstName(), datingProfile.getLastName());
        fragmentViewProfileBinding.tvName.setText(AppUtil.getNameAndAge(fullName, datingProfile.getAge()));

        UiUtil.setTextView(fragmentViewProfileBinding.tvAbout, datingProfile.getAboutMe());
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
        UiUtil.setTextViewWithBoldPrefix(context, "Car Status :", datingProfile.getOwnCarType(), fragmentViewProfileBinding.tvOwnCar);
        UiUtil.setTextViewWithBoldPrefix(context, "House Status :", datingProfile.getOwnHouseType(), fragmentViewProfileBinding.tvOwnHouse);
    }
}
