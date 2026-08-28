package vedam.subkuch.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import vedam.subkuch.network.models.BaseGetMasterModel
import vedam.subkuch.network.models.GeneralResponse
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.jobs.models.CitiesResponse
import vedam.subkuch.ui.jobs.models.JobCategoryResponse
import vedam.subkuch.ui.jobs.models.JobExperienceResponse
import vedam.subkuch.ui.jobs.models.JobMelaRequest
import vedam.subkuch.ui.jobs.models.JobQualificationResponse
import vedam.subkuch.ui.jobs.models.JobResponse
import vedam.subkuch.ui.jobs.models.JobSalaryResponse

/** Coroutine endpoints for the Jobs browsing flow. */
interface JobsApi {
    @GET("api/Jobs/GetCategories")
    suspend fun getCategories(): Response<JobCategoryResponse>

    @GET("api/Jobs/GetJobs")
    suspend fun getJobs(
        @Query("CategoryId") categoryId: String,
        @Query("JobTitle") jobTitle: String,
        @Query("PageIndex") pageIndex: Int,
        @Query("PageSize") pageSize: Int,
        @Query("Gender") gender: Int
    ): Response<JobResponse>

    @GET("https://sabkuch.sabkuchworld.com/AllAPI/GetAllCity")
    suspend fun getCities(): Response<CitiesResponse>

    @POST("Jobs/AddJob")
    suspend fun addJob(@Body requestBody: RequestBody): Response<AddResponse>

    @GET("api/Master/GetJobQualifications")
    suspend fun getJobQualifications(): Response<JobQualificationResponse>

    @GET("api/Master/GetJobExpereince")
    suspend fun getJobExperiences(): Response<JobExperienceResponse>

    @GET("api/Master/GetJobSalaries")
    suspend fun getJobSalaries(): Response<JobSalaryResponse>

    @GET("api/Jobs/ViewJobProfile")
    suspend fun getJobProfile(@Query("ProfileId") profileId: String): Response<BaseGetMasterModel<JobMelaRequest>>

    @POST("api/Jobs/AddJobProfile")
    suspend fun addJobProfile(@Body requestBody: RequestBody): Response<GeneralResponse>

    @Multipart
    @POST("api/Jobs/UploadJobProfileImage")
    suspend fun uploadJobProfileImage(
        @Part("ProfileId") profileId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<GeneralResponse>
}
