package vedam.subkuch.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vedam.subkuch.network.models.DataEntryListResponse;
import vedam.subkuch.network.models.DataEntryRequest;
import vedam.subkuch.network.models.RegistrationMasterResponse;
import vedam.subkuch.network.models.learn.LearnHomeResponse;
import vedam.subkuch.network.models.learn.MyCoursesResponse;
import vedam.subkuch.ui.jobs.models.AddResponse;

/** Typed Retrofit endpoints for the newer registration and contribution API. */
public interface RegistrationApi {

    @GET("api/Learn/GetLearnHome")
    Call<LearnHomeResponse> getLearnHome();

    @GET("api/Learn/GetMyCourses")
    Call<MyCoursesResponse> getMyCourses();

    @GET("api/Learn/GetLearnHome")
    Call<ResponseBody> getLearnHomeRaw();

    @GET("api/Learn/GetMyCourses")
    Call<ResponseBody> getMyCoursesRaw();

    @GET("api/Master/GetStates")
    Call<RegistrationMasterResponse> getStates();

    @GET("api/Master/GetDistricts")
    Call<RegistrationMasterResponse> getDistricts();

    @GET("api/DataEntry/GetUniqueDataEntries")
    Call<DataEntryListResponse> getUniqueDataEntries(
            @Query("UserId") int userId,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize
    );

    @POST("api/DataEntry/AddDataEntry")
    Call<AddResponse> addDataEntry(@Body DataEntryRequest request);
}
