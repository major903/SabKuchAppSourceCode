package vedam.subkuch.network

import android.content.Context
import com.google.gson.Gson
import vedam.subkuch.BuildConfig
import vedam.subkuch.network.Response
import vedam.subkuch.network.models.DataPart
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import java.lang.reflect.Type
import java.util.*

/**
 * Created by nansari on 04/14/16.
 */
object DataFetcher {
    @JvmStatic
    fun <T> registerUser(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ): Boolean {
        if (!isRegistrationApiConfigured()) return false
        val url = registrationUrl("users/Register")
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
        return true
    }

    @JvmStatic
    fun <T> updateUser(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = registrationUrl("UserProfile/EditProfile")
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> sendOtp(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        countryCode: String?,
        phoneNumber: String?
    ) {
        val url = registrationUrl("login/otps/get/$countryCode/$phoneNumber")
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> verifyOtp(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        countryCode: String?,
        phoneNumber: String?,
        otp: String?
    ) {
        val url = registrationUrl("login/otps/verify/$countryCode/$phoneNumber/$otp")
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    fun <T> getFeatures(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url =
            String.format("%s/api/Feature/GetByCity?UserId=%s", NetworkConstants.END_POINT2, userId)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getFeatures2(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(Locale.US,
            "%s/api/Feature/GetFeature2?UserId=%s",
            NetworkConstants.END_POINT2,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getBroadcastMessage(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/api/Message/GetBroadCastMessage?id=%s",
            NetworkConstants.END_POINT2,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getCountries(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/AllAPI/GetAllcountry", NetworkConstants.END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getCities(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        JobApiClient.enqueue(
            JobApiClient.getApi().getCities(), repClass, updateSuccessListener, errorListener
        )
    }

    @JvmStatic
    fun <T> getRegistrationCountries(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = registrationUrl("Master/GetCountries")
        val headers = hashMapOf("CompanyId" to BuildConfig.REGISTRATION_COMPANY_ID)
        NetworkGateway.callGetApiWithHeaders(context, url, null, updateSuccessListener, repClass, errorListener, headers)
    }

    @JvmStatic
    fun <T> getRegistrationStates(
        context: Context?, updateSuccessListener: Response.Listener<T>?, repClass: Type?, errorListener: Response.ErrorListener?
    ) = getRegistrationMasterData(context, "GetStates", updateSuccessListener, repClass, errorListener)

    @JvmStatic
    fun <T> getRegistrationDistricts(
        context: Context?, updateSuccessListener: Response.Listener<T>?, repClass: Type?, errorListener: Response.ErrorListener?
    ) = getRegistrationMasterData(context, "GetDistricts", updateSuccessListener, repClass, errorListener)

    @JvmStatic
    fun <T> getRegistrationLanguages(
        context: Context?, updateSuccessListener: Response.Listener<T>?, repClass: Type?, errorListener: Response.ErrorListener?
    ) = getRegistrationMasterData(context, "GetLanguages", updateSuccessListener, repClass, errorListener)

    private fun <T> getRegistrationMasterData(
        context: Context?, path: String, updateSuccessListener: Response.Listener<T>?, repClass: Type?, errorListener: Response.ErrorListener?
    ) {
        val url = registrationUrl("Master/$path")
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun isRegistrationApiConfigured(): Boolean = BuildConfig.REGISTRATION_API_BASE_URL.isNotBlank()

    private fun registrationUrl(path: String): String =
        "${BuildConfig.REGISTRATION_API_BASE_URL.trimEnd('/')}/api/$path"

    @JvmStatic
    fun <T> getMenus(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Menu/GetMenus", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url =
            String.format("%s/api/JobsOld/Directory/GetCategories", NetworkConstants.SABKUCH2_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> searchBusiness(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        searchTerm: CharSequence?
    ) {
        val url = String.format(
            "%s/api/JobsOld/Directory/SearchBusinesses?SearchText=%s",
            NetworkConstants.SABKUCH2_END_POINT, searchTerm ?: ""
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getSubCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        categoryId: String?
    ) {
        val url = String.format(
            "%s/api/JobsOld/Directory/GetSubCategories?CategoryId=%s",
            NetworkConstants.SABKUCH2_END_POINT,
            categoryId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getShoppingSubCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
    ) {
        val url = String.format(
            "%s/api/Shopping/GetShoppingSubcategories",
            NetworkConstants.END_POINT2
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getProducts(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        subCategoryId: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val url = String.format(Locale.US,
            "%s/api/Shopping/GetProducts?subcategoryId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            subCategoryId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getHomeProducts(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val url = String.format(Locale.US,
            "%s/api/Shopping/GetHomeProducts?PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getShoppingProductDetails(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        shoppingId: String,
    ) {
        val url = String.format(
            "%s/api/Shopping/GetProductDetails?shoppingId=%s",
            NetworkConstants.END_POINT2,
            shoppingId

        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getUtilitySubCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url =
            String.format("%s/api/PublicUtility/GetSubCategories", NetworkConstants.SABKUCH2_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getDirectoryDetails(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        categoryId: String?,
        subCategoryId: String?,
        search: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val url = String.format(Locale.US,
            "%s/api/JobsOld/Directory/GetBusiness?SubCategoryCityId=%s&CategoryId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.SABKUCH2_END_POINT,
            subCategoryId,
            categoryId,
            pageIndex,
            pageSize,
            AppUtil.deNull(search),
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getPublicUtilities(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        subCategoryId: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/PublicUtility/GetPublicUtilities?SubCategoryCityId=%s&UserId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.SABKUCH2_END_POINT,
            subCategoryId,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getPhoneBook(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/Phoonebookcategory?userid=%s",
            NetworkConstants.END_POINT,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getPhoneBookDetails(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        categoryId: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/Phonebook?userid=%s&categoryId=%s",
            NetworkConstants.END_POINT,
            userId,
            categoryId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMovies(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Movies/GetMovies", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> addEvent(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Events/AddEvent", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addQuestion(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/AllAPI/AskmePost", NetworkConstants.END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addReview(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/Directory/ReviewRating", NetworkConstants.SABKUCH2_END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addBusiness(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/JobsOld/Directory/AddBusiness", NetworkConstants.SABKUCH2_END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getOffers(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Offers/GetOffers", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getAskCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/GetAskmecategory?userid=%s",
            NetworkConstants.END_POINT,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getVehicles(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/GetVehicleBycity?userid=%s",
            NetworkConstants.END_POINT,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getDestinationCities(
        context: Context?, updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?, errorListener: Response.ErrorListener?, vehicletype: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/GetDesinationCity?userid=%s&vehicletype=%s",
            NetworkConstants.END_POINT,
            userId,
            vehicletype
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getVehicleTimings(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        vehicletype: String?,
        cityName: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/AllAPI/GetVehicleTimingDetails?userid=%s&vehicletype=%s&Cityname=%s",
            NetworkConstants.END_POINT,
            userId,
            vehicletype,
            cityName
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> updateLocation(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        latitude: String?,
        longitude: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/api/UserProfile/SaveCurrentLocation",
            NetworkConstants.JOB_END_POINT
        )
        val body = Gson().toJson(
            mapOf(
                "UserId" to userId,
                "Latitude" to latitude,
                "Longitude" to longitude
            )
        )
        NetworkGateway.callApiWithBody(
            context, url, null, updateSuccessListener, body, repClass, errorListener
        )
    }

    @JvmStatic
    fun <T> getAskConversation(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        categoryId: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/AllAPI/GetAskmebycategory?userid=%s&categoryid=%s&PageSize=%d&PageNumber=%d",
            NetworkConstants.END_POINT,
            userId,
            categoryId,
            pageSize,
            pageIndex
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> addAskReply(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/AllAPI/AskmeReplay", NetworkConstants.END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getJobTypes(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        JobApiClient.enqueue(
            JobApiClient.getApi().getJobTypes(), repClass, updateSuccessListener, errorListener
        )
    }

    @JvmStatic
    fun <T> uploadMatrimonialProfileImage(
        context: Context?,
        dataPartMap: Map<String?, DataPart?>?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val map: MutableMap<String, String> = HashMap()
        map[NetworkConstants.ProfileId] = AppPrefs.getPrefsUserId(context)
        val url = String.format("%s/api/Matrimony/UploadProfileImage", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithMultipartBody(
            context,
            url,
            null,
            updateSuccessListener,
            map,
            dataPartMap,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> uploadDatingProfileImage(
        context: Context?,
        dataPartMap: Map<String?, DataPart?>?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val map: MutableMap<String, String> = HashMap()
        map[NetworkConstants.ProfileId] = AppPrefs.getPrefsUserId(context)
        val url = String.format("%s/api/Dating/UploadProfileImage", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithMultipartBody(
            context,
            url,
            null,
            updateSuccessListener,
            map,
            dataPartMap,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> uploadEventImage(
        context: Context?,
        dataPartMap: Map<String?, DataPart?>?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        eventId: String
    ) {
        val map: MutableMap<String, String> = HashMap()
        map[NetworkConstants.EventId] = eventId
        val url = String.format("%s/api/Events/UploadEventImage", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithMultipartBody(
            context,
            url,
            null,
            updateSuccessListener,
            map,
            dataPartMap,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getInbox(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            "%s/api/Inbox/GetInbox?UserProfileId=%s",
            NetworkConstants.END_POINT2,
            userId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getUserProfile(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = registrationUrl("Users")
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> setAccessPin(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/UserProfile/SetAccessPin", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> verifyAccessPin(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/UserProfile/VerifyAccessPin", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getDatingProfile(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Dating/GetProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMatrimonialProfile(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Matrimony/GetProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> setMatrimonyLikeDislike(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Matrimony/SetLikeDislike", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> setDatingLikeDislike(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Dating/SetLikeDislike", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getMatrimonialMatchedProfiles(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Matrimony/GetMatchedProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getDatingMatchedProfiles(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Dating/GetMatchedProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    fun <T> getMatrimonialMatchedChatProfiles(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Matrimony/GetMatchedChatProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    fun <T> getDatingMatchedChatProfiles(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Dating/GetMatchedChatProfiles?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getAllTransportBookings(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Transport/GetBooking?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMyTransportBookings(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Transport/GetMyBooking?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getLabourRequirement(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Master/GetTransportCoolie", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getVehicleType(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Master/GetTransportTypes", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> addTransport(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Transport/AddTransport", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> setTransportBookingComplete(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        transportId: String?
    ) {
        val url = String.format(
            "%s/api/Transport/MarkComplete?TransportId=%s&UserId=%s", NetworkConstants.END_POINT2,
            transportId, AppPrefs.getPrefsUserId(context)
        )
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            null,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getShareContent(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Referral/ShareContent", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getWalletDetails(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format(Locale.US, "%s/api/Withdrawal/GetBalance", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getWalletTerms(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format(Locale.US, "%s/api/Referral/GetMyWallet", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMyReferral(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Referral/GetMyReferral", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> addReferral(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/UserProfile/LinkReferral", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> withdraw(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Referral/Withdrawal", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addWithdrawal(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format(
            Locale.US,
            "%s/api/Withdrawal/AddWithdrawal",
            NetworkConstants.JOB_END_POINT
        )
        NetworkGateway.callApiWithBodyNoRetry(
            context, url, null, updateSuccessListener, json, repClass, errorListener
        )
    }

    @JvmStatic
    fun <T> transferFunds(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = registrationUrl("Transfer/AddTransfer")
        // A transfer must never be retried automatically because that could move funds twice.
        NetworkGateway.callApiWithBodyNoRetry(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addDataEntry(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ): Boolean {
        if (!isRegistrationApiConfigured()) return false
        val url = registrationUrl("DataEntry/AddDataEntry")
        NetworkGateway.callApiWithBodyNoRetry(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
        return true
    }

    @JvmStatic
    fun <T> getUniqueDataEntries(
        context: Context?,
        userId: Int,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ): Boolean {
        if (!isRegistrationApiConfigured()) return false
        val url = registrationUrl("DataEntry/GetUniqueDataEntries?UserId=$userId&pageIndex=1&pageSize=10")
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
        return true
    }

    @JvmStatic
    fun <T> getProviders(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Needs/GetProviders", NetworkConstants.END_POINT2)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getAllNeeds(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        providerId: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Needs/GetAllNeeds?UserId=%s&NeedProviderId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            providerId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMyNeeds(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        providerId: String?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Needs/GetMyNeeds?UserId=%s&NeedProviderId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.END_POINT2,
            userId,
            providerId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> setNeedBookingComplete(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        needId: String?
    ) {
        val url = String.format(
            "%s/api/Needs/MarkComplete?NeedId=%s&UserId=%s", NetworkConstants.END_POINT2,
            needId, AppPrefs.getPrefsUserId(context)
        )
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            null,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> addNeed(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format("%s/api/Needs/PostNeed", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getClassifiedsCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format(Locale.US, "%s/api/Classified/Categories", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getClassifiedSubCategories(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        categoryId: String?
    ) {
        val url = String.format(
            Locale.US,
            "%s/api/Classified/SubCategories?CategoryId=%s",
            NetworkConstants.JOB_END_POINT,
            categoryId
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> addClassified(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?
    ) {
        val url = String.format(Locale.US, "%s/api/Classified/AddClassifiedAds", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> updateClassified(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url =
            String.format(Locale.US, "%s/api/Classified/UpdateClassifiedAds", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> deleteClassified(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?,
        adId: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Classified/DeleteAd?ClassifiedAdId=%s&UserId=%s",
            NetworkConstants.JOB_END_POINT,
            adId,
            userId
        )
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            null,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> uploadClassifiedImage(
        context: Context?,
        dataPartMap: Map<String?, DataPart?>?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        classifiedId: String
    ) {
        val map: MutableMap<String, String> = HashMap()
        map[NetworkConstants.ClassifiedAdId] = classifiedId
        val url = String.format(Locale.US, "%s/api/Classified/UploadImage", NetworkConstants.JOB_END_POINT)
        NetworkGateway.callApiWithMultipartBody(
            context,
            url,
            null,
            updateSuccessListener,
            map,
            dataPartMap,
            repClass,
            errorListener
        )
    }

    @JvmStatic
    fun <T> getClassifieds(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int,
        subCategoryId: String?
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Classified/GetClassifiedAds?SubCategoryId=%s&ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.JOB_END_POINT,
            subCategoryId,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> getMyClassifieds(
        context: Context?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Type?,
        errorListener: Response.ErrorListener?,
        pageIndex: Int,
        pageSize: Int
    ) {
        val userId = AppPrefs.getPrefsUserId(context)
        val url = String.format(
            Locale.US,
            "%s/api/Classified/MyClassifiedAds?ProfileId=%s&PageIndex=%d&PageSize=%d",
            NetworkConstants.JOB_END_POINT,
            userId,
            pageIndex,
            pageSize
        )
        NetworkGateway.callGetApi(context, url, null, updateSuccessListener, repClass, errorListener)
    }

    @JvmStatic
    fun <T> registerForPush(
        context: Context?,
        json: String?,
        updateSuccessListener: Response.Listener<T>?,
        repClass: Class<T>?,
        errorListener: Response.ErrorListener?
    ) {
        val url =
            String.format("%s/api/PushNotification/RegisterDevice", NetworkConstants.END_POINT2)
        NetworkGateway.callApiWithBody(
            context,
            url,
            null,
            updateSuccessListener,
            json,
            repClass,
            errorListener
        )
    }
}
