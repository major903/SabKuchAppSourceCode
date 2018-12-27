package vedam.subkuch.network;

import android.content.Context;

import com.android.volley.Response;

import java.lang.reflect.Type;
import java.util.Locale;

import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;

/**
 * Created by nansari on 04/14/16.
 */
public class DataFetcher {

    public static <T> void registerUser(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/users/register", NetworkConstants.HOST_NAME);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void sendOtp(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener,
                                   String countryCode, String phoneNumber) {

        String url = String.format("%s/login/otps/get/%s/%s", NetworkConstants.HOST_NAME, countryCode, phoneNumber);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getFeatures(Context context, Response.Listener<T> updateSuccessListener, Type repClass, Response.ErrorListener errorListener, String userId) {

        String url = String.format("%s/features/users/%s/get", NetworkConstants.HOST_NAME, userId);
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
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        String url = String.format(Locale.US, "%s/AllAPI/GetEvent?userid=%s&PageIndex=%d&PageSize=%d", NetworkConstants.END_POINT, userId, pageIndex, pageSize);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getPhoneBook(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        String url = String.format("%s/AllAPI/Phoonebookcategory?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void getPhoneBookDetails(Context context, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener, String categoryId) {
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
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
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        String url = String.format("%s/AllAPI/GetMovies?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }

    public static <T> void addEvent(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/addevent", NetworkConstants.END_POINT);
        HelperVolley.callApiWithBody(context, url, null, updateSuccessListener, json, repClass, errorListener);
    }

    public static <T> void addQuestion(Context context, String json, Response.Listener<T> updateSuccessListener, Class<T> repClass, Response.ErrorListener errorListener) {

        String url = String.format("%s/AllAPI/Postaskme", NetworkConstants.END_POINT);
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
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        String url = String.format("%s/AllAPI/GetAlloffer?userid=%s", NetworkConstants.END_POINT, userId);
        HelperVolley.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener);
    }
}