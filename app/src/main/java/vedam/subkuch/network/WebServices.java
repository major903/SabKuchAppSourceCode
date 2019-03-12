package vedam.subkuch.network;

import android.content.Context;
import android.content.Intent;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vedam.subkuch.R;
import vedam.subkuch.network.handler.DeleteImageHandler;
import vedam.subkuch.network.handler.GetAllCityHandler;
import vedam.subkuch.network.handler.GetAnnualIncomeHandler;
import vedam.subkuch.network.handler.GetBodyTypeHandler;
import vedam.subkuch.network.handler.GetComplexionHandler;
import vedam.subkuch.network.handler.GetCountryHandler;
import vedam.subkuch.network.handler.GetDoshamHandler;
import vedam.subkuch.network.handler.GetDrinkingHabitsHandler;
import vedam.subkuch.network.handler.GetFoodHabitsHandler;
import vedam.subkuch.network.handler.GetGotrasHandler;
import vedam.subkuch.network.handler.GetLikeDislikeHandler;
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
import vedam.subkuch.network.handler.GetSearchProfileHandler;
import vedam.subkuch.network.handler.GetUserDetailHandler;
import vedam.subkuch.network.handler.InsertImageHandler;
import vedam.subkuch.network.handler.ResponseHandler;
import vedam.subkuch.network.handler.UpdateMaterimonialHandler;
import vedam.subkuch.network.handler.UpdatePreferencesHandler;
import vedam.subkuch.network.models.GetAllCountries;
import vedam.subkuch.network.models.GetAnnualIncome;
import vedam.subkuch.network.models.GetBodyTypeBean;
import vedam.subkuch.network.models.GetCityResponse;
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
import vedam.subkuch.network.models.deleteImage.DeleteImageResponse;
import vedam.subkuch.network.models.getLikeDislikeResponse.GetLikeDislikeStatus;
import vedam.subkuch.network.models.getLiving.GetLivingResponse;
import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;
import vedam.subkuch.network.models.getPreferencesResponse.GetPreferenceResponse;
import vedam.subkuch.network.models.getReligion.GetReligionResponse;
import vedam.subkuch.network.models.insertImage.InsertImageResponse;
import vedam.subkuch.network.models.searchProfile.SearchProfile;
import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;
import vedam.subkuch.ui.profile.RegisterUserActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

/**
 * Created by Mobile on 3/15/2017.
 */

public class WebServices {
    private final WebApi api;
    private static WebServices mInstance;
    private String bearer;
    private Context context;


    public WebServices(Context context) {
        mInstance = this;
        this.context = context;
        bearer = AppPrefs.getPrefsToken(context);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        api = new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build().create(WebApi.class);


    }

    public static WebServices getInstance() {
        return mInstance;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    public void getMasterCast(final GetMasterCastHandler handler, String religionID) {
        Call<GetMasterCastResponse> callback = api.getMasterCast(bearer);
        callback.enqueue(new Callback<GetMasterCastResponse>() {
            @Override
            public void onResponse(Call<GetMasterCastResponse> call, Response<GetMasterCastResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetMasterCastResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }


    public void getGotra(String masterCastId, final GetGotrasHandler handler) {
        Call<GetGotrasBean> callback = api.getGotras(bearer);
        callback.enqueue(new Callback<GetGotrasBean>() {
            @Override
            public void onResponse(Call<GetGotrasBean> call, Response<GetGotrasBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetGotrasBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getReligion(final GetReligionHandler handler) {
        Call<GetReligionResponse> callback = api.getReligion(bearer);
        callback.enqueue(new Callback<GetReligionResponse>() {
            @Override
            public void onResponse(Call<GetReligionResponse> call, Response<GetReligionResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetReligionResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getLiving(final GetLivingHandler handler) {
        Call<GetLivingResponse> callback = api.getLiving(bearer);
        callback.enqueue(new Callback<GetLivingResponse>() {
            @Override
            public void onResponse(Call<GetLivingResponse> call, Response<GetLivingResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetLivingResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }


    public void getUserDetail(String UserID, boolean isDating, final GetUserDetailHandler handler) {
        Call<GetUserDetailResponse> callback;
        if (isDating)
            callback = api.getDatingUserDetail(bearer, UserID);
        else
            callback = api.getMatrimonyUserDetail(bearer, UserID);

        callback.enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }


    public void deleteImage(String imageid, final DeleteImageHandler handler) {
        Call<DeleteImageResponse> callback = api.deleteImage(imageid);
        callback.enqueue(new Callback<DeleteImageResponse>() {
            @Override
            public void onResponse(Call<DeleteImageResponse> call, Response<DeleteImageResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<DeleteImageResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void insertUpdateImage(String userId, String imageid, String imageData, final InsertImageHandler handler) {

        Call<InsertImageResponse> callback = api.insertUpdateImage(userId, imageid, imageData);
        callback.enqueue(new Callback<InsertImageResponse>() {
            @Override
            public void onResponse(Call<InsertImageResponse> call, Response<InsertImageResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<InsertImageResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getPreference(boolean isDating, final GetPreferenceHandler handler, String userId) {

        Call<GetPreferenceResponse> callback;
        if (isDating)
            callback = api.getDatingPreference(bearer, userId);
        else
            callback = api.getMatrimonialPreference(bearer, userId);
        callback.enqueue(new Callback<GetPreferenceResponse>() {
            @Override
            public void onResponse(Call<GetPreferenceResponse> call, Response<GetPreferenceResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetPreferenceResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }


  /*  public void updatePreferences(String userid, String minage, String maxage, String Religion,
                                  String Caste, String Livingwith, String OwnHouse, String Toweight, String Fromweight, String minHeight, String maxHeight,
                                  String minDistance, String maxDistance, String selectedCityId, String selectedSubCastId,
                                  String selectedNakshatraId, String selectedBodyTypeId, String selectedComplexionId, String selectedOccupationId,
                                  String selectedQualificationId, String minIncome, String maxMaxIncome, String smoking, String drinking,
                                  String selectedFoodHabitesId, String selectedMothertoungeId, String selectedPhysicalstatusId, String Matrialstatus, String selectedDoshamId, final UpdatePreferencesHandler handler) {

        Call<InsertProfilePreferenceResponse> callback = api.updatePreferences(userid, minage, maxage, Religion, Caste, Livingwith, OwnHouse, Toweight, Fromweight, minHeight, maxHeight, minDistance, maxDistance, selectedCityId, selectedSubCastId, selectedNakshatraId, selectedBodyTypeId, selectedComplexionId, selectedOccupationId, selectedQualificationId, minIncome, maxMaxIncome, smoking, drinking, selectedFoodHabitesId + "", selectedMothertoungeId, selectedPhysicalstatusId, Matrialstatus, selectedDoshamId);
        callback.enqueue(new Callback<InsertProfilePreferenceResponse>() {
            @Override
            public void onResponse(Call<InsertProfilePreferenceResponse> call, Response<InsertProfilePreferenceResponse> response) {
                if (response!=null && response.body()!=null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<InsertProfilePreferenceResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }*/


    public void getSearchProfile(final GetSearchProfileHandler handler, String userId, String mPageIndex, String mPageSize) {

        Call<SearchProfile> callback = api.profileSearch(userId, mPageIndex, mPageSize);
        callback.enqueue(new Callback<SearchProfile>() {
            @Override
            public void onResponse(Call<SearchProfile> call, Response<SearchProfile> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<SearchProfile> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getDislike(final GetLikeDislikeHandler handler, String userId, String userid2, String isDislike) {

        Call<GetLikeDislikeStatus> callback = api.getIsDislike(userId, userid2, isDislike);
        callback.enqueue(new Callback<GetLikeDislikeStatus>() {
            @Override
            public void onResponse(Call<GetLikeDislikeStatus> call, Response<GetLikeDislikeStatus> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetLikeDislikeStatus> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getLike(final GetLikeDislikeHandler handler, String userId, String userid2, String isLike) {

        Call<GetLikeDislikeStatus> callback = api.getIsLike(userId, userid2, isLike);
        callback.enqueue(new Callback<GetLikeDislikeStatus>() {
            @Override
            public void onResponse(Call<GetLikeDislikeStatus> call, Response<GetLikeDislikeStatus> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetLikeDislikeStatus> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }


    public void getComplexion(final GetComplexionHandler handler) {
        Call<GetComplexionBean> callback = api.getComplexion(bearer);
        callback.enqueue(new Callback<GetComplexionBean>() {
            @Override
            public void onResponse(Call<GetComplexionBean> call, Response<GetComplexionBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetComplexionBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getOccupation(final GetOccupationHandler handler) {
        Call<GetOccupationBean> callback = api.getOccupation(bearer);
        callback.enqueue(new Callback<GetOccupationBean>() {
            @Override
            public void onResponse(Call<GetOccupationBean> call, Response<GetOccupationBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetOccupationBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getQualification(final GetQualificationHandler handler) {
        Call<GetQualificationBean> callback = api.getQualification(bearer);
        callback.enqueue(new Callback<GetQualificationBean>() {
            @Override
            public void onResponse(Call<GetQualificationBean> call, Response<GetQualificationBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetQualificationBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getFoodhabits(final GetFoodHabitsHandler handler) {
        Call<GetFoodHabitsBean> callback = api.getFoodHabits(bearer);
        callback.enqueue(new Callback<GetFoodHabitsBean>() {
            @Override
            public void onResponse(Call<GetFoodHabitsBean> call, Response<GetFoodHabitsBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetFoodHabitsBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getPhysicalstatus(final GetPhysicalStatusHandler handler) {
        Call<GetPhysicalStatusBean> callback = api.getPhysicalStatus(bearer);
        callback.enqueue(new Callback<GetPhysicalStatusBean>() {
            @Override
            public void onResponse(Call<GetPhysicalStatusBean> call, Response<GetPhysicalStatusBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetPhysicalStatusBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getDosham(final GetDoshamHandler handler) {
        Call<GetDoshamBean> callback = api.getDosham(bearer);
        callback.enqueue(new Callback<GetDoshamBean>() {
            @Override
            public void onResponse(Call<GetDoshamBean> call, Response<GetDoshamBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetDoshamBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getDrinkingHabits(final GetDrinkingHabitsHandler handler) {
        Call<GetDrinkingHabits> callback = api.getDrinkingHabits(bearer);
        callback.enqueue(new Callback<GetDrinkingHabits>() {
            @Override
            public void onResponse(Call<GetDrinkingHabits> call, Response<GetDrinkingHabits> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetDrinkingHabits> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getAnnualIncome(final GetAnnualIncomeHandler handler) {
        Call<GetAnnualIncome> callback = api.getAnnualIncome(bearer);
        callback.enqueue(new Callback<GetAnnualIncome>() {
            @Override
            public void onResponse(Call<GetAnnualIncome> call, Response<GetAnnualIncome> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetAnnualIncome> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getHeight(final ResponseHandler handler) {
        Call<GetHeightResponse> callback = api.getHeight(bearer);
        callHeight(callback, handler);
    }

    public void getWeight(final ResponseHandler handler) {
        Call<GetWeightResponse> callback = api.getWeight(bearer);
        callWeight(callback, handler);
    }

    public void getOwnCar(final ResponseHandler handler) {
        Call<GetOwnCarResponse> callback = api.getOwnCar(bearer);
        callOwnCar(callback, handler);
    }

    public void getOwnHouse(final ResponseHandler handler) {
        Call<GetOwnHouseResponse> callback = api.getOwnHouse(bearer);
        callOwnHouse(callback, handler);
    }

    public void getSmoking(final ResponseHandler handler) {
        Call<GetSmokingResponse> callback = api.getSmoking(bearer);
        callSmoking(callback, handler);
    }

    private void callHeight(Call<GetHeightResponse> callback, ResponseHandler handler) {

        callback.enqueue(new Callback<GetHeightResponse>() {
            @Override
            public void onResponse(Call<GetHeightResponse> call, Response<GetHeightResponse> response) {
                if (response != null && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetHeightResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    private void callWeight(Call<GetWeightResponse> callback, ResponseHandler handler) {

        callback.enqueue(new Callback<GetWeightResponse>() {
            @Override
            public void onResponse(Call<GetWeightResponse> call, Response<GetWeightResponse> response) {
                if (response != null && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetWeightResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    private void callOwnCar(Call<GetOwnCarResponse> callback, ResponseHandler handler) {

        callback.enqueue(new Callback<GetOwnCarResponse>() {
            @Override
            public void onResponse(Call<GetOwnCarResponse> call, Response<GetOwnCarResponse> response) {
                if (response != null && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetOwnCarResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    private void callOwnHouse(Call<GetOwnHouseResponse> callback, ResponseHandler handler) {

        callback.enqueue(new Callback<GetOwnHouseResponse>() {
            @Override
            public void onResponse(Call<GetOwnHouseResponse> call, Response<GetOwnHouseResponse> response) {
                if (response != null && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetOwnHouseResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    private void callSmoking(Call<GetSmokingResponse> callback, ResponseHandler handler) {

        callback.enqueue(new Callback<GetSmokingResponse>() {
            @Override
            public void onResponse(Call<GetSmokingResponse> call, Response<GetSmokingResponse> response) {
                if (response != null && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetSmokingResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getNakshakra(final GetNakshatrasHandler handler) {
        Call<GetNakshatrasBean> callback = api.getNakshatras(bearer);
        callback.enqueue(new Callback<GetNakshatrasBean>() {
            @Override
            public void onResponse(Call<GetNakshatrasBean> call, Response<GetNakshatrasBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetNakshatrasBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getMatirialStatus(final GetMaritalStatusHandler handler) {
        Call<GetMaritalStatusResponse> callback = api.getMaritalStatus(bearer);
        callback.enqueue(new Callback<GetMaritalStatusResponse>() {
            @Override
            public void onResponse(Call<GetMaritalStatusResponse> call, Response<GetMaritalStatusResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetMaritalStatusResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getBodytype(final GetBodyTypeHandler handler) {

        Call<GetBodyTypeBean> callback = api.getBodytype(bearer);
        callback.enqueue(new Callback<GetBodyTypeBean>() {
            @Override
            public void onResponse(Call<GetBodyTypeBean> call, Response<GetBodyTypeBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetBodyTypeBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getMothertouge(final GetMothertongueHandler handler) {
        Call<GetMothertongueBean> callback = api.getMothertongue(bearer);
        callback.enqueue(new Callback<GetMothertongueBean>() {
            @Override
            public void onResponse(Call<GetMothertongueBean> call, Response<GetMothertongueBean> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetMothertongueBean> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

   /* public void updateMaterimonial(String userID, String religionId, String casteId, String gotraId, String ownCar, String ownHouse, String livingWithId, String accessPin, String prefferdtype, String height, String weight, String age, String firstaLocationLatitudes, String firstalocationlongitudes, String nakshakra, String bodytype, String complexion, String occupation, String qualification, String anualIncome, String isSmoking, String isDrinking, String foodhabits, String mothertouge, String physicalstatus, String matrialstatus, String dosham, final UpdateMaterimonialHandler handler) {
        Call<UpdateMatrimonialResponse> callback = api.updateMatrimonial(userID, religionId, casteId, gotraId, ownCar, ownHouse, livingWithId, accessPin, prefferdtype, height, weight, firstaLocationLatitudes, firstalocationlongitudes, age, nakshakra, bodytype, complexion, occupation, qualification, anualIncome, isSmoking, isDrinking, foodhabits, mothertouge, physicalstatus, matrialstatus, dosham);
        callback.enqueue(new Callback<UpdateMatrimonialResponse>() {
            @Override
            public void onResponse(Call<UpdateMatrimonialResponse> call, Response<UpdateMatrimonialResponse> response) {
                if (response!=null && response.body()!=null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<UpdateMatrimonialResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }*/

    public void getAllCity(final GetAllCityHandler handler) {
        Call<GetCityResponse> callback = api.getAllCity(bearer);
        callback.enqueue(new Callback<GetCityResponse>() {
            @Override
            public void onResponse(Call<GetCityResponse> call, Response<GetCityResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetCityResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void updateProfile(boolean isDating, final UpdateMaterimonialHandler handler, UpdateProfileRequest updateProfileRequest) {
        Call<UpdateMatrimonialResponse> callback;
        if (isDating)
            callback = api.updateDatingProfile(bearer, updateProfileRequest);
        else
            callback = api.updateMatrimonialProfile(bearer, updateProfileRequest);

        callback.enqueue(new Callback<UpdateMatrimonialResponse>() {
            @Override
            public void onResponse(Call<UpdateMatrimonialResponse> call, Response<UpdateMatrimonialResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<UpdateMatrimonialResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    public void getAllCountries(final GetCountryHandler handler) {
        Call<GetAllCountries> callback = api.getCountries(bearer);
        callback.enqueue(new Callback<GetAllCountries>() {
            @Override
            public void onResponse(Call<GetAllCountries> call, Response<GetAllCountries> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<GetAllCountries> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });

    }

    public void editPreferences(boolean isDating, MatrimonialRequest matrimonialRequest, final UpdatePreferencesHandler handler) {

        Call<UpdateMatrimonialResponse> callback;
        if (isDating)
            callback = api.editDatingPreference(bearer, matrimonialRequest);
        else
            callback = api.editMatrimonialPreference(bearer, matrimonialRequest);
        callback.enqueue(new Callback<UpdateMatrimonialResponse>() {
            @Override
            public void onResponse(Call<UpdateMatrimonialResponse> call, Response<UpdateMatrimonialResponse> response) {
                if (response != null && response.body() != null && response.body().getReturnMessage().equalsIgnoreCase("success")) {
                    handler.onSuccess(response.body());
                } else {
                    handleError(response);
                    handler.onError("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<UpdateMatrimonialResponse> call, Throwable t) {
                handler.onError(t.getMessage());
            }
        });
    }

    private <T> void handleError(Response<T> response) {
        if (response != null && response.code() == NetworkConstants.CODE_UNAUTHORIZED) {
            logout();
        }
    }

    private void logout() {
        if(context==null)
            return;
        AppPrefs.getInstance(context).getSharedPreferences().edit().clear().apply();
        int flags = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
        context.startActivity(new Intent(context, RegisterUserActivity.class).addFlags(flags));
        UiUtil.showToast(context, context.getString(R.string.err_unauthorized));
    }
}
