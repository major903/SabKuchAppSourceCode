package vedam.subkuch.ui.matrimonial.preference.presenter;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.models.GetSmokingResponse;
import vedam.subkuch.network.WebServices;
import vedam.subkuch.network.handler.GetAllCityHandler;
import vedam.subkuch.network.handler.GetBodyTypeHandler;
import vedam.subkuch.network.handler.GetComplexionHandler;
import vedam.subkuch.network.handler.GetDoshamHandler;
import vedam.subkuch.network.handler.GetDrinkingHabitsHandler;
import vedam.subkuch.network.handler.GetFoodHabitsHandler;
import vedam.subkuch.network.handler.GetGotrasHandler;
import vedam.subkuch.network.handler.GetLivingHandler;
import vedam.subkuch.network.handler.GetMaritalStatusHandler;
import vedam.subkuch.network.handler.GetMasterCastHandler;
import vedam.subkuch.network.handler.GetMothertongueHandler;
import vedam.subkuch.network.handler.GetNakshatrasHandler;
import vedam.subkuch.network.handler.GetOccupationHandler;
import vedam.subkuch.network.handler.GetPhysicalStatusHandler;
import vedam.subkuch.network.handler.GetPreferenceHandler;
import vedam.subkuch.network.handler.GetQualificationHandler;
import vedam.subkuch.network.handler.GetReligionHandler;
import vedam.subkuch.network.handler.ResponseHandler;
import vedam.subkuch.network.handler.UpdatePreferencesHandler;
import vedam.subkuch.network.models.GetBodyTypeBean;
import vedam.subkuch.network.models.GetCityResponse;
import vedam.subkuch.network.models.GetComplexionBean;
import vedam.subkuch.network.models.GetDoshamBean;
import vedam.subkuch.network.models.GetDrinkingHabits;
import vedam.subkuch.network.models.GetFoodHabitsBean;
import vedam.subkuch.network.models.GetGotrasBean;
import vedam.subkuch.network.models.GetMaritalStatusResponse;
import vedam.subkuch.network.models.GetMothertongueBean;
import vedam.subkuch.network.models.GetNakshatrasBean;
import vedam.subkuch.network.models.GetOccupationBean;
import vedam.subkuch.network.models.GetOwnCarResponse;
import vedam.subkuch.network.models.GetOwnHouseResponse;
import vedam.subkuch.network.models.GetPhysicalStatusBean;
import vedam.subkuch.network.models.GetQualificationBean;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getPreferencesResponse.GetPreferenceResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.matrimonial.preference.view.PerferenceFragmentView;


public class PerferenceFragmentPresenter implements PerferenceFragmentPresenterHandler {
    private PerferenceFragmentView view;

    public PerferenceFragmentPresenter(PerferenceFragmentView view) {
        this.view = view;
    }


    @Override
    public void getPerference(String userid, boolean isDating) {
        view.showProgressBar();
        WebServices.getInstance().getPreference(isDating, new GetPreferenceHandler() {
            @Override
            public void onSuccess(GetPreferenceResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetPreference(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        }, userid);
    }

    @Override
    public void getMasterCast(String religionId) {
        view.showProgressBar();
        WebServices.getInstance().getMasterCast(new GetMasterCastHandler() {
            @Override
            public void onSuccess(GetMasterCastResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetMasterCast(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        }, religionId);
    }

    @Override
    public void getReligion() {
        view.showProgressBar();
        WebServices.getInstance().getReligion(new GetReligionHandler() {
            @Override
            public void onSuccess(GetReligionResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetReligion(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getLiving() {
        view.showProgressBar();
        WebServices.getInstance().getLiving(new GetLivingHandler() {
            @Override
            public void onSuccess(GetLivingResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetLiving(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

   /* @Override
    public void updatePreferences( String userid, String minage, String maxage, String Religion, String Caste, String Livingwith, String OwnHouse, String Toweight, String Fromweight, String minHeight, String maxHeight, String minDistance, String maxDistance, String selectedCityId, String selectedSubCastId, String selectedNakshatraId,
                                  String selectedBodyTypeId, String selectedComplexionId, String selectedOccupationId,
                                  String selectedQualificationId, String minIncome, String maxMaxIncome, String smoking,
                                  String drinking, String selectedFoodHabitesId, String selectedMothertoungeId, String selectedPhysicalstatusId,
                                  String Matrialstatus, String selectedDoshamId) {
        view.showProgressBar();
        WebServices.getInstance().updatePreferences( userid,  minage,  maxage,  Religion,  Caste,  Livingwith,  OwnHouse,  Toweight,  Fromweight,  minHeight,  maxHeight,  minDistance,  maxDistance,  selectedCityId+"",  selectedSubCastId+"",  selectedNakshatraId+"",  selectedBodyTypeId+"",  selectedComplexionId+"",  selectedOccupationId+"",  selectedQualificationId+"",  minIncome,  maxMaxIncome,  smoking,  drinking,  selectedFoodHabitesId+"",  selectedMothertoungeId+"",  selectedPhysicalstatusId+"",  Matrialstatus+"",  selectedDoshamId+"",new UpdatePreferencesHandler() {
            @Override
            public void onSuccess(InsertProfilePreferenceResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyUpdatePreferences(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }*/


    @Override
    public void getGotra(String masterCastId) {
        view.showProgressBar();
        WebServices.getInstance().getGotra(masterCastId, new GetGotrasHandler() {
            @Override
            public void onSuccess(GetGotrasBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetGotra(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getBodytype() {
        view.showProgressBar();
        WebServices.getInstance().getBodytype(new GetBodyTypeHandler() {
            @Override
            public void onSuccess(GetBodyTypeBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetBodyType(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getComplexion() {
        view.showProgressBar();
        WebServices.getInstance().getComplexion(new GetComplexionHandler() {
            @Override
            public void onSuccess(GetComplexionBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetComplexion(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getOccupation() {
        view.showProgressBar();
        WebServices.getInstance().getOccupation(new GetOccupationHandler() {
            @Override
            public void onSuccess(GetOccupationBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetOccupation(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getQualification() {
        view.showProgressBar();
        WebServices.getInstance().getQualification(new GetQualificationHandler() {
            @Override
            public void onSuccess(GetQualificationBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetQualification(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getFoodhabits() {
        view.showProgressBar();
        WebServices.getInstance().getFoodhabits(new GetFoodHabitsHandler() {
            @Override
            public void onSuccess(GetFoodHabitsBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetFoodHabits(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getDrinkingHabits() {
        view.showProgressBar();
        WebServices.getInstance().getDrinkingHabits(new GetDrinkingHabitsHandler() {
            @Override
            public void onSuccess(GetDrinkingHabits response) {
                view.hideProgressBar();
                view.onSuccessfullyGetDrinkingHabits(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getPhysicalstatus() {
        view.showProgressBar();
        WebServices.getInstance().getPhysicalstatus(new GetPhysicalStatusHandler() {
            @Override
            public void onSuccess(GetPhysicalStatusBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetPhysicalstatus(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getDosham() {
        view.showProgressBar();
        WebServices.getInstance().getDosham(new GetDoshamHandler() {
            @Override
            public void onSuccess(GetDoshamBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetDosham(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getMothertouge() {
        view.showProgressBar();
        WebServices.getInstance().getMothertouge(new GetMothertongueHandler() {
            @Override
            public void onSuccess(GetMothertongueBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetMothertongue(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    public void getNakshakra() {
        view.showProgressBar();
        WebServices.getInstance().getNakshakra(new GetNakshatrasHandler() {
            @Override
            public void onSuccess(GetNakshatrasBean response) {
                view.hideProgressBar();
                view.onSuccessfullyGetNakshatras(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getAllCity() {
        view.showProgressBar();
        WebServices.getInstance().getAllCity(new GetAllCityHandler() {
            @Override
            public void onSuccess(GetCityResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetCity(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getMatrialStatus() {
        view.showProgressBar();
        WebServices.getInstance().getMatirialStatus(new GetMaritalStatusHandler() {
            @Override
            public void onSuccess(GetMaritalStatusResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetMaritalStatus(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void editPreferences(MatrimonialRequest matrimonialRequest, boolean isDating) {
        view.showProgressBar();
        WebServices.getInstance().editPreferences(isDating, matrimonialRequest, new UpdatePreferencesHandler() {
            @Override
            public void onSuccess(UpdateMatrimonialResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyUpdatePreferences(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void getOwnCar() {

        view.showProgressBar();

        WebServices.getInstance().getOwnCar(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                view.hideProgressBar();
                GetOwnCarResponse response = (GetOwnCarResponse) object;
                if (response.getReturnMessage().equals(Constants.SUCCESS))
                    view.onSuccessfullyGetOwnCar(response);
                else
                    showError("Something went wrong");
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    @Override
    public void getOwnHouse() {

        view.showProgressBar();

        WebServices.getInstance().getOwnHouse(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                view.hideProgressBar();
                GetOwnHouseResponse response = (GetOwnHouseResponse) object;
                if (response.getReturnMessage().equals(Constants.SUCCESS))
                    view.onSuccessfullyGetOwnHouse(response);
                else
                    showError("Something went wrong");
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    @Override
    public void getSmoking() {

        view.showProgressBar();

        WebServices.getInstance().getSmoking(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                view.hideProgressBar();
                GetSmokingResponse response = (GetSmokingResponse) object;
                if (response.getReturnMessage().equals(Constants.SUCCESS))
                    view.onSuccessfullyGetSmoking(response);
                else
                    showError("Something went wrong");
            }

            @Override
            public void onError(String message) {
                showError(message);
            }
        });
    }

    private void showError(String message) {
        view.hideProgressBar();
        view.showFeedBackMessage(message);
    }

}
