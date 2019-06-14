package vedam.subkuch.network;

import android.content.Context;

import com.android.volley.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vedam.subkuch.network.models.DataPart;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;

/**
 * Created by nansari on 04/14/16.
 */
public class DataFetcher {

    public static <T> void registerUser(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/UserProfile/AddProfile", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void editProfileMain(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/UserProfile/EditProfileMain", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void sendOtp(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener,
                                   String countryCode, String phoneNumber) {

        String url = String.format("%s/api/login/otps/get/%s/%s", NetworkConstants.END_POINT3, countryCode, phoneNumber);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getFeatures(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String userId) {

        String url = String.format("%s/api/features/users/%s/get", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getCountries(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/GetAllcountry", NetworkConstants.END_POINT);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getCities(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/GetAllCity", NetworkConstants.END_POINT);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getCategories(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/Directory/GetCategories", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getSubCategories(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String categoryId) {
        String url = String.format("%s/Directory/GetSubCategories?CategoryId=%s", NetworkConstants.END_POINT2, categoryId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getDirectoryDetails(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener
            , String categoryId, String subCategoryId, String search) {
        String url = String.format("%s/Directory/GetBusiness?SubCategoryId=%s&search=%s", NetworkConstants.END_POINT2, subCategoryId, AppUtil.deNull(search));
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getEvents(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener,
                                     int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Events/GetEvents?PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT2, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getPhoneBook(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/Phoonebookcategory?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getPhoneBookDetails(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, String categoryId) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/Phonebook?userid=%s&categoryId=%s", NetworkConstants.END_POINT, userId, categoryId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobsCategory(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/Jobs/GetCategories", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobs(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, String categoryId, String search
            , int pageIndex, int pageSize) {
        String url = String.format(Locale.US, "%s/Jobs/GetJobs?CategoryId=%s&JobTitle=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT2,
                categoryId, AppUtil.deNull(search), pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getMovies(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Movies/GetMovies", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addEvent(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Events/AddEvent", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addQuestion(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/AskmePost", NetworkConstants.END_POINT);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addReview(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/Directory/ReviewRating", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addJobs(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/Jobs/AddJob", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addBusiness(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/Directory/AddBusiness", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void getOffers(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/GetAlloffer?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getAskCategories(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/GetAskmecategory?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getVehicles(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/GetVehicleBycity?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getDestinationCities(Context context, Response.Listener<T> updateSuccessListener,
                                                Class<T> repClass, Response.ErrorListener errorListener, String vehicletype) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/GetDesinationCity?userid=%s&vehicletype=%s", NetworkConstants.END_POINT, userId, vehicletype);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getVehicleTimings(Context context, Response.Listener<T> updateSuccessListener,
                                             Class<T> repClass, Response.ErrorListener errorListener, String vehicletype, String cityName) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/GetVehicleTimingDetails?userid=%s&vehicletype=%s&Cityname=%s", NetworkConstants.END_POINT, userId, vehicletype, cityName);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void updateLocation(Context context, Response.Listener<T> updateSuccessListener,
                                          Class<T> repClass, Response.ErrorListener errorListener, String latitude, String longitude) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/AllAPI/UserCurrentLocation?userid=%s&Latitude=%s&Longitude=%s",
                NetworkConstants.END_POINT, userId, latitude, longitude);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getAskConversation(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener,
                                              String categoryId, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/AllAPI/GetAskmebycategory?userid=%s&categoryid=%s&PageSize=%d&PageNumber=%d",
                NetworkConstants.END_POINT, userId, categoryId, pageSize, pageIndex);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addAskReply(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/AskmeReplay", NetworkConstants.END_POINT);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void getJobTypes(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetJobTypes", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobQualifications(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetJobQualifications", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobExperiences(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetJobExpereince", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobSalaries(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetJobSalaries", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getJobProfile(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/api/Jobs/ViewJobProfile?ProfileId=%s", NetworkConstants.END_POINT3, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addJobProfile(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Jobs/AddJobProfile", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void uploadJobProfileImage(Context context, Map<String, DataPart> dataPartMap, Response.Listener<T> updateSuccessListener, Class<T> repClass,
                                                 Response.ErrorListener errorListener) {
        Map<String, String> map = new HashMap<>();
        map.put(NetworkConstants.ProfileId, AppPrefs.getPrefsUserId(context));
        String url = String.format("%s/api/Jobs/UploadJobProfileImage", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithMultipartBody(context, url, null, updateSuccessListener, map, dataPartMap, repClass, errorListener);
    }

    public static <T> void uploadMatrimonialProfileImage(Context context, Map<String, DataPart> dataPartMap, Response.Listener<T> updateSuccessListener, Class<T> repClass,
                                                         Response.ErrorListener errorListener) {
        Map<String, String> map = new HashMap<>();
        map.put(NetworkConstants.ProfileId, AppPrefs.getPrefsUserId(context));
        String url = String.format("%s/api/Matrimony/UploadProfileImage", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithMultipartBody(context, url, null, updateSuccessListener, map, dataPartMap, repClass, errorListener);
    }

    public static <T> void uploadDatingProfileImage(Context context, Map<String, DataPart> dataPartMap, Response.Listener<T> updateSuccessListener, Class<T> repClass,
                                                         Response.ErrorListener errorListener) {
        Map<String, String> map = new HashMap<>();
        map.put(NetworkConstants.ProfileId, AppPrefs.getPrefsUserId(context));
        String url = String.format("%s/api/Dating/UploadProfileImage", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithMultipartBody(context, url, null, updateSuccessListener, map, dataPartMap, repClass, errorListener);
    }

    public static <T> void uploadEventImage(Context context, Map<String, DataPart> dataPartMap, Response.Listener<T> updateSuccessListener, Class<T> repClass,
                                                    Response.ErrorListener errorListener, String eventId) {
        Map<String, String> map = new HashMap<>();
        map.put(NetworkConstants.EventId, eventId);
        String url = String.format("%s/api/Events/UploadEventImage", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithMultipartBody(context, url, null, updateSuccessListener, map, dataPartMap, repClass, errorListener);
    }

    public static <T> void getInbox(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/api/Inbox/GetInbox?UserProfileId=%s", NetworkConstants.END_POINT3, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getProfile(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format("%s/api/UserProfile/ViewProfile?ProfileId=%s", NetworkConstants.END_POINT3, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void setAccessPin(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/UserProfile/SetAccessPin", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void verifyAccessPin(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/UserProfile/VerifyAccessPin", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void getDatingProfile(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Dating/GetProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getMatrimonialProfile(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Matrimony/GetProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }


    public static <T> void setMatrimonyLikeDislike(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Matrimony/SetLikeDislike", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void setDatingLikeDislike(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Dating/SetLikeDislike", NetworkConstants.END_POINT3);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void getMatrimonialMatchedProfiles(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Matrimony/GetMatchedProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getDatingMatchedProfiles(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Dating/GetMatchedProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getMatrimonialMatchedChatProfiles(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Matrimony/GetMatchedChatProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getDatingMatchedChatProfiles(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Dating/GetMatchedChatProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getAllTransportBookings(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Transport/GetBooking?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getMyTransportBookings(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, int pageIndex, int pageSize) {
        String userId = AppPrefs.getPrefsUserId(context);
        String url = String.format(Locale.US, "%s/api/Transport/GetMyBooking?ProfileId=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT3, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getLabourRequirement(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetTransportCoolie", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getVehicleType(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Master/GetTransportTypes", NetworkConstants.END_POINT3);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addTransport(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Transport/AddTransport", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void setTransportBookingComplete(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, String transportId) {

        String url = String.format("%s/api/Transport/MarkComplete?TransportId=%s&UserId=%s", NetworkConstants.END_POINT2,
                transportId, AppPrefs.getPrefsUserId(context));
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, null, repClass, errorListener);
    }

    public static <T> void getShareContent(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Referral/ShareContent", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getWalletDetails(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Referral/GetMyWallet", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getMyReferral(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String url = String.format("%s/api/Referral/GetMyReferral", NetworkConstants.END_POINT2);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addReferral(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/UserProfile/LinkReferral", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void withdraw(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/Referral/Withdrawal", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addStaffTrackLocation(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/api/BusinessLocation/InsertLocation", NetworkConstants.END_POINT2);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }
}