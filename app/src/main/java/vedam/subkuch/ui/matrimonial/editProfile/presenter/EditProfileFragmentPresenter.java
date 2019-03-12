package vedam.subkuch.ui.matrimonial.editProfile.presenter;

import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.GetSmokingResponse;
import vedam.subkuch.network.GetWeightResponse;
import vedam.subkuch.network.WebServices;
import vedam.subkuch.network.handler.GetAnnualIncomeHandler;
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
import vedam.subkuch.network.handler.GetQualificationHandler;
import vedam.subkuch.network.handler.GetReligionHandler;
import vedam.subkuch.network.handler.GetUserDetailHandler;
import vedam.subkuch.network.handler.ResponseHandler;
import vedam.subkuch.network.handler.UpdateMaterimonialHandler;
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
import vedam.subkuch.network.models.UserDetail.GetUserDetailResponse;
import vedam.subkuch.network.models.UserDetail.UpdateProfileRequest;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.matrimonial.editProfile.view.EditProfileFragmentView;


public class EditProfileFragmentPresenter implements EditProfileFragmentPresenterHandler {
    private EditProfileFragmentView view;

    public EditProfileFragmentPresenter(EditProfileFragmentView view) {
        this.view = view;
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


    @Override
    public void getUserDetail(String userId) {
        view.showProgressBar();
        WebServices.getInstance().getUserDetail(userId, new GetUserDetailHandler() {
            @Override
            public void onSuccess(GetUserDetailResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyGetUserDetail(response);
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
    public void getAnnualIncome() {
        view.showProgressBar();
        WebServices.getInstance().getAnnualIncome(new GetAnnualIncomeHandler() {
            @Override
            public void onSuccess(GetAnnualIncome response) {
                view.hideProgressBar();
                view.onSuccessfullyGetAnnualIncome(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });

    }

    @Override
    public void getHeight() {
        view.showProgressBar();

        WebServices.getInstance().getHeight(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                view.hideProgressBar();
                GetHeightResponse response = (GetHeightResponse) object;
                if (response.getReturnMessage().equals(Constants.SUCCESS))
                    view.onSuccessfullyGetHeight(response);
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
    public void getWeight() {
        view.showProgressBar();

        WebServices.getInstance().getWeight(new ResponseHandler() {

            @Override
            public void onSuccess(Object object) {
                GetWeightResponse response = (GetWeightResponse) object;
                view.hideProgressBar();
                if (response.getReturnMessage().equals(Constants.SUCCESS))
                    view.onSuccessfullyGetWeight(response);
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
    public void updateMaterimonial(String UserID, String ReligionId, String CasteId, String gotraId, String OwnCar, String OwnHouse, String LivingWithId, String AccessPin, String Prefferdtype, String height, String Weight, String age, String firstaLocationLatitudes, String firstalocationlongitudes, String nakshakra, String bodytype, String complexion, String occupation, String qualification, String anualIncome, String isSmoking, String isDrinking, String foodhabits, String mothertouge, String physicalstatus, String matrialstatus, String dosham) {
     /*   view.showProgressBar();
        WebServices.getInstance().updateMaterimonial(UserID, ReligionId, CasteId, gotraId, OwnCar, OwnHouse, LivingWithId, AccessPin, Prefferdtype, height, Weight, age, firstaLocationLatitudes, firstalocationlongitudes, nakshakra, bodytype, complexion, occupation, qualification, anualIncome, isSmoking, isDrinking, foodhabits, mothertouge, physicalstatus, matrialstatus, dosham, new UpdateMaterimonialHandler() {
            @Override
            public void onSuccess(UpdateMatrimonialResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyUpdateProfile(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });*/
    }

    @Override
    public void getMatrialStatus() {
        view.showProgressBar();
        WebServices.getInstance().getMatirialStatus(new GetMaritalStatusHandler() {
            @Override
            public void onSuccess(GetMaritalStatusResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyMaritalStatus(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        });
    }

    @Override
    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        view.showProgressBar();
        WebServices.getInstance().updateProfile(new UpdateMaterimonialHandler() {
            @Override
            public void onSuccess(UpdateMatrimonialResponse response) {
                view.hideProgressBar();
                view.onSuccessfullyUpdateProfile(response);
            }

            @Override
            public void onError(String message) {
                view.hideProgressBar();
                view.showFeedBackMessage(message);
            }
        }, updateProfileRequest);
    }
}
