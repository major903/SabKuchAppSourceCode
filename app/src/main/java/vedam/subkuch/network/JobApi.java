package vedam.subkuch.network;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/** Retrofit endpoints used by the Jobs and Job Mela features. */
public interface JobApi {

    @GET("https://sabkuch.sabkuchworld.com/AllAPI/GetAllCity")
    Call<ResponseBody> getCities();

    @GET("api/Jobs/GetCategories")
    Call<ResponseBody> getCategories();

    @GET("api/Jobs/GetJobs")
    Call<ResponseBody> getJobs(
            @Query("CategoryId") String categoryId,
            @Query("JobTitle") String jobTitle,
            @Query("PageIndex") int pageIndex,
            @Query("PageSize") int pageSize,
            @Query("Gender") int gender
    );

    @POST("Jobs/AddJob")
    Call<ResponseBody> addJob(@Body RequestBody requestBody);

    @GET("api/Master/GetJobTypes")
    Call<ResponseBody> getJobTypes();

    @GET("api/Master/GetJobQualifications")
    Call<ResponseBody> getJobQualifications();

    @GET("api/Master/GetJobExpereince")
    Call<ResponseBody> getJobExperiences();

    @GET("api/Master/GetJobSalaries")
    Call<ResponseBody> getJobSalaries();

    @GET("api/Jobs/ViewJobProfile")
    Call<ResponseBody> getJobProfile(@Query("ProfileId") String profileId);

    @POST("api/Jobs/AddJobProfile")
    Call<ResponseBody> addJobProfile(@Body RequestBody requestBody);

    @Multipart
    @POST("api/Jobs/UploadJobProfileImage")
    Call<ResponseBody> uploadJobProfileImage(
            @Part("ProfileId") RequestBody profileId,
            @Part List<MultipartBody.Part> files
    );
}
