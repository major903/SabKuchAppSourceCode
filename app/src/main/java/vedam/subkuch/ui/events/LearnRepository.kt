package vedam.subkuch.ui.events

import android.content.Context
import kotlinx.coroutines.CancellationException
import retrofit2.create
import vedam.subkuch.network.LearnApi
import vedam.subkuch.network.RegistrationApiClient
import vedam.subkuch.network.models.learn.LearnCategory
import vedam.subkuch.network.models.learn.LearnCourse
import vedam.subkuch.network.models.learn.LearnCourseDetailsData

class LearnRepository(context: Context) {
    private val api = RegistrationApiClient.getRetrofit(context.applicationContext)
        .create<LearnApi>()

    suspend fun getLearnHome(): LearnHomeData? = try {
        val response = api.getLearnHome()
        val data = response.body()?.returnData
        if (!response.isSuccessful || data?.courses == null) {
            null
        } else {
            LearnHomeData(
                categories = data.categories?.toList().orEmpty(),
                courses = data.courses.toList()
            )
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        null
    }

    suspend fun getMyCourses(): List<LearnCourse>? = try {
        val response = api.getMyCourses()
        if (!response.isSuccessful || response.body() == null) {
            null
        } else {
            response.body()!!.returnData?.toList().orEmpty()
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        null
    }

    suspend fun getCourseDetails(courseId: Int): LearnCourseDetailsData? = try {
        val response = api.getCourseDetails(courseId)
        if (!response.isSuccessful) null else response.body()?.returnData
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        null
    }

    suspend fun searchCourses(query: String): List<LearnCourse>? = try {
        val response = api.searchCourses(query)
        if (!response.isSuccessful) {
            null
        } else {
            response.body()?.returnData?.courses?.toList().orEmpty()
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        null
    }

    suspend fun getCoursesByCategory(categoryId: Int): List<LearnCourse>? = try {
        val response = api.getCoursesByCategory(categoryId)
        if (!response.isSuccessful) {
            null
        } else {
            response.body()?.returnData?.courses?.toList().orEmpty()
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        null
    }
}

data class LearnHomeData(
    val categories: List<LearnCategory>,
    val courses: List<LearnCourse>
)
