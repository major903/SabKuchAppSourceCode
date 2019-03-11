package vedam.subkuch.ui.matrimonial.editProfile.presenter;

import vedam.subkuch.network.models.UserDetail.UpdateProfileRequest;

public interface EditProfileFragmentPresenterHandler {

    void getMasterCast(String religionId);

    void getGotra(String masterCastId);

    void getReligion();

    void getLiving();

    void getNakshakra();

    void updateMaterimonial(String UserID, String ReligionId, String CasteId, String gotraId, String OwnCar, String OwnHouse, String LivingWithId, String AccessPin, String Prefferdtype, String height, String Weight, String age, String firstaLocationLatitudes, String firstalocationlongitudes, String nakshakra, String bodytype, String complexion, String occupation, String qualification, String anualIncome, String isSmoking, String isDrinking, String foodhabits, String mothertouge, String physicalstatus, String matrialstatus, String dosham);

    void getUserDetail(String userId);

    void getBodytype();

    void getComplexion();

    void getOccupation();

    void getQualification();

    void getFoodhabits();

    void getPhysicalstatus();

    void getDosham();

    void getHeight();

    void getWeight();

    void getOwnCar();

    void getOwnHouse();

    void getSmoking();

    void getMothertouge();

    void getMatrialStatus();

    void getDrinkingHabits();

    void getAnnualIncome();

    void updateProfile(UpdateProfileRequest updateProfileRequest);
}
