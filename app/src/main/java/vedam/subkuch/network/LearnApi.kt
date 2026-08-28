package vedam.subkuch.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import vedam.subkuch.network.models.learn.CourseDetailsResponse
import vedam.subkuch.network.models.learn.CoursesByCategoryResponse
import vedam.subkuch.network.models.learn.LearnHomeResponse
import vedam.subkuch.network.models.learn.MyCoursesResponse
import vedam.subkuch.network.models.learn.SearchCoursesResponse

/** Coroutine Retrofit contract for the Learn feature. */
interface LearnApi {
    @GET("api/Learn/GetLearnHome")
    suspend fun getLearnHome(): Response<LearnHomeResponse>

    @GET("api/Learn/GetMyCourses")
    suspend fun getMyCourses(): Response<MyCoursesResponse>

    @GET("api/Learn/GetCourseDetails")
    suspend fun getCourseDetails(@Query("courseId") courseId: Int): Response<CourseDetailsResponse>

    @GET("api/Learn/SearchCourses")
    suspend fun searchCourses(@Query("query") query: String): Response<SearchCoursesResponse>

    @GET("api/Learn/GetCoursesByCategory")
    suspend fun getCoursesByCategory(
        @Query("categoryId") categoryId: Int
    ): Response<CoursesByCategoryResponse>
}
