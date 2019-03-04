package vedam.subkuch.ui.dating.editProfile.view;

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
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;

public interface EditProfileFragmentView {
    void showProgressBar();

    void hideProgressBar();

    void showFeedBackMessage(String message);

    void onSuccessfullyGetMasterCast(GetMasterCastResponse response);

    void onSuccessfullyGetGotra(GetGotrasBean response);

    void onSuccessfullyGetReligion(GetReligionResponse response);

    void onSuccessfullyGetLiving(GetLivingResponse response);

    void onSuccessfullyUpdateProfile(UpdateMatrimonialResponse response);

    void onSuccessfullyGetUserDetail(GetUserDetailResponse response);

    void onSuccessfullyGetComplexion(GetComplexionBean response);

    void onSuccessfullyGetOccupation(GetOccupationBean response);

    void onSuccessfullyGetQualification(GetQualificationBean response);

    void onSuccessfullyGetFoodHabits(GetFoodHabitsBean response);

    void onSuccessfullyGetDrinkingHabits(GetDrinkingHabits response);

    void onSuccessfullyGetAnnualIncome(GetAnnualIncome response);

    void onSuccessfullyGetPhysicalstatus(GetPhysicalStatusBean response);

    void onSuccessfullyGetDosham(GetDoshamBean response);

    void onSuccessfullyGetNakshatras(GetNakshatrasBean response);

    void onSuccessfullyGetBodyType(GetBodyTypeBean response);

    void onSuccessfullyGetMothertongue(GetMothertongueBean response);

    void onSuccessfullyMaritalStatus(GetMaritalStatusResponse response);
}
