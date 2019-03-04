package vedam.subkuch.ui.dating.preference.presenter;

import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;

public interface PerferenceFragmentPresenterHandler {

    void getPerference(String userId);

    void getMasterCast(String religionId);

    void getReligion();

    void getLiving();

   /* void updatePreferences( String userid, String minage, String maxage, String Religion, String Caste, String Livingwith, String OwnHouse,
                           String Toweight, String Fromweight, String minHeight, String maxHeight, String minDistance, String maxDistance,
                           String selectedCityId, String selectedSubCastId, String selectedNakshatraId, String selectedBodyTypeId,
                           String selectedComplexionId, String selectedOccupationId, String selectedQualificationId, String minIncome,
                           String maxMaxIncome, String smoking, String drinking, String selectedFoodHabitesId, String selectedMothertoungeId,
                           String selectedPhysicalstatusId, String Matrialstatus, String selectedDoshamId);

*/
    void getGotra(String s);

    void getBodytype();

    void getComplexion();

    void getOccupation();

    void getQualification();

    void getFoodhabits();

    void getDrinkingHabits();

    void getPhysicalstatus();

    void getDosham();

    void getMothertouge();

    void getNakshakra();

    void getAllCity();

    void getMatrialStatus();

    void editPreferences(MatrimonialRequest matrimonialRequest);
}
