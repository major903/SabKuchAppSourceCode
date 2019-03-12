package vedam.subkuch.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
import vedam.subkuch.network.models.insertProfileSearchResponse.InsertProfilePreferenceResponse;
import vedam.subkuch.network.models.searchProfile.SearchProfile;
import vedam.subkuch.network.models.updateMatrimonial.MatrimonialRequest;
import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;

/**
 * Created by Mobile on 3/15/2017.
 */

public interface WebApi {
    @GET("Master/GetReligions")
    Call<GetReligionResponse> getReligion(@Header("Authorization") String bearer);

    @GET("Master/GetCountries")
    Call<GetAllCountries> getCountries(@Header("Authorization") String bearer);

    @GET("Master/GetCities")
    Call<GetCityResponse> getAllCity(@Header("Authorization") String bearer);

    @GET("Master/GetCasts")
    Call<GetMasterCastResponse> getMasterCast(@Header("Authorization") String bearer);

    @GET("Master/GetMaritalStatus")
    Call<GetMaritalStatusResponse> getMaritalStatus(@Header("Authorization") String bearer);

    @GET("Master/GetLivingWith")
    Call<GetLivingResponse> getLiving(@Header("Authorization") String bearer);

    @GET("Master/GetGotras")
    Call<GetGotrasBean> getGotras(@Header("Authorization") String bearer);

    @GET("Master/GetBodyTypes")
    Call<GetBodyTypeBean> getBodytype(@Header("Authorization") String bearer);

    @GET("Master/GetNakshatras")
    Call<GetNakshatrasBean> getNakshatras(@Header("Authorization") String bearer);

    @GET("Master/GetQualifications")
    Call<GetQualificationBean> getQualification(@Header("Authorization") String bearer);

    @GET("Master/GetComplexions")
    Call<GetComplexionBean> getComplexion(@Header("Authorization") String bearer);

    @GET("Master/GetDosham")
    Call<GetDoshamBean> getDosham(@Header("Authorization") String bearer);

    @GET("Master/GetFoodHabits")
    Call<GetFoodHabitsBean> getFoodHabits(@Header("Authorization") String bearer);

    @GET("Master/GetDrinkingHabits")
    Call<GetDrinkingHabits> getDrinkingHabits(@Header("Authorization") String bearer);

    @GET("Master/GetAnualIncome")
    Call<GetAnnualIncome> getAnnualIncome(@Header("Authorization") String bearer);

    @GET("Master/GetPhysicalStatus")
    Call<GetPhysicalStatusBean> getPhysicalStatus(@Header("Authorization") String bearer);

    @GET("Master/GetOccupations")
    Call<GetOccupationBean> getOccupation(@Header("Authorization") String bearer);

    @GET("Master/GetMotherTounges")
    Call<GetMothertongueBean> getMothertongue(@Header("Authorization") String bearer);

    @GET("Master/GetHeight")
    Call<GetHeightResponse> getHeight(@Header("Authorization") String bearer);

    @GET("Master/GetWeight")
    Call<GetWeightResponse> getWeight(@Header("Authorization") String bearer);

    @GET("Master/GetSmoking")
    Call<GetSmokingResponse> getSmoking(@Header("Authorization") String bearer);

    @GET("Master/GetOwnCar")
    Call<GetOwnCarResponse> getOwnCar(@Header("Authorization") String bearer);

    @GET("Master/GetOWnHouse")
    Call<GetOwnHouseResponse> getOwnHouse(@Header("Authorization") String bearer);

    @POST("UserProfile/EditProfileMatrimonial")
    Call<UpdateMatrimonialResponse> updateMatrimonialProfile(@Header("Authorization") String bearer, @Body UpdateProfileRequest profileRequest);

    @POST("UserProfile/EditProfileDating")
    Call<UpdateMatrimonialResponse> updateDatingProfile(@Header("Authorization") String bearer, @Body UpdateProfileRequest profileRequest);

   /* @GET("UserProfile/EditProfile")
    Call<UpdateMatrimonialResponse> updateMatrimonial(@Query("ProfileId") String UserID, @Query("ReligionId") String ReligionId,
                                                      @Query("CasteId") String CasteId, @Query("Gotraid") String GotraId,
                                                      @Query("OwnCar") String OwnCar, @Query("OwnHouse") String OwnHouse,
                                                      @Query("LivingWithId") String LivingWithId, @Query("AccessPin")
                                                              String AccessPin, @Query("Prefferdtype") String Prefferdtype,
                                                      @Query("height") String height, @Query("Weight") String Weight,
                                                      @Query("Latitude") String latitudes,
                                                      @Query("Longitude") String longitude, @Query("Age")
                                                              String Age, @Query("Nakshakraid") String Nakshakra, @Query("BodyTypeid")
                                                              String Bodytype, @Query("Complexionid") String Complexion,
                                                      @Query("Occupationid") String Occupation,
                                                      @Query("Qualificationid") String Qualification, @Query("AnualIncomeid")
                                                              String Anualincome, @Query("IsSmoking") String Issmoking,
                                                      @Query("DrinkingStatusid") String Isdrinking, @Query("FoodHabitsid")
                                                              String Foodhabits, @Query("MotherTougeid") String Mothertouge,
                                                      @Query("PhysicalStatusid") String Physicalstatus, @Query("MatrialStatusid")
                                                              String Matrialstatus, @Query("Doshamid") String Dosham);
*/


    @GET("UserProfile/ViewProfile")
    Call<GetUserDetailResponse> getMatrimonyUserDetail(@Header("Authorization") String bearer, @Query("ProfileId") String userid);

    @GET("Dating/ViewProfile")
    Call<GetUserDetailResponse> getDatingUserDetail(@Header("Authorization") String bearer, @Query("ProfileId") String userid);

    @GET("deleteimge")
    Call<DeleteImageResponse> deleteImage(@Query("imageid") String imageid);

    @FormUrlEncoded
    @POST("insertupdateimage")
    Call<InsertImageResponse> insertUpdateImage(@Field("userid") String userid, @Field("imageid") String imageid, @Field("imegedata") String imegedata);

    @GET("Matrimony/ViewPreference")
    Call<GetPreferenceResponse> getMatrimonialPreference(@Header("Authorization") String bearer, @Query("ProfileId") String userid);

    @GET("Dating/ViewPreference")
    Call<GetPreferenceResponse> getDatingPreference(@Header("Authorization") String bearer, @Query("ProfileId") String userid);


    @POST("Matrimony/EditPreference")
    Call<UpdateMatrimonialResponse> editMatrimonialPreference(@Header("Authorization") String bearer, @Body MatrimonialRequest profileRequest);

    @POST("Dating/EditPreference")
    Call<UpdateMatrimonialResponse> editDatingPreference(@Header("Authorization") String bearer, @Body MatrimonialRequest profileRequest);

    @GET("editPreferences")
    Call<InsertProfilePreferenceResponse> updatePreferences(@Query("userid") String userid, @Query("Toage") String minage, @Query("FromAge") String maxage, @Query("Religion") String Religion,
                                                            @Query("Caste") String Caste, @Query("Livingwith") String Livingwith, @Query("Ownhouse") String OwnHouse,
                                                            @Query("Toweight") String Toweight, @Query("Fromweight") String Fromweight,
                                                            @Query("Toheight") String minHeight, @Query("Fromheight") String maxHeight,
                                                            @Query("Tolocation") String minDistance, @Query("Fromlocation") String maxDistance,
                                                            @Query("City") String selectedCityId, @Query("Gotra") String selectedSubCastId,
                                                            @Query("Nakshakra") String selectedNakshatraId, @Query("Bodytype") String selectedBodyTypeId,
                                                            @Query("Complexion") String selectedComplexionId, @Query("Occupation") String selectedOccupationId,
                                                            @Query("Qualification") String selectedQualificationId, @Query("ToAnualIncome") String minIncome, @Query("FromAnualIncome") String maxMaxIncome,
                                                            @Query("Issmoking") String smoking, @Query("Isdrinking") String drinking,
                                                            @Query("Foodhabits") String selectedFoodHabitesId, @Query("Mothertouge") String selectedMothertoungeId,
                                                            @Query("Physicalstatus") String selectedPhysicalstatusId, @Query("Matrialstatus") String Matrialstatus, @Query("Dosham") String selectedDoshamId);

    @GET("ProfileSearch")
    Call<SearchProfile> profileSearch(@Query("userid") String userid, @Query("pageIndex") String pageIndex, @Query("pageSize") String pageSize);

    @GET("matrimoniallike")
    Call<GetLikeDislikeStatus> getIsLike(@Query("userid1") String userid, @Query("userid2") String userid2, @Query("like") String like);

    @GET("matrimonialdislike")
    Call<GetLikeDislikeStatus> getIsDislike(@Query("userid1") String userid, @Query("userid2") String userid2, @Query("dislike") String dislike);


}
