package vedam.subkuch.ui.jobs

import kotlinx.coroutines.CancellationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.create
import vedam.subkuch.network.JobApiClient
import vedam.subkuch.network.JobsApi
import vedam.subkuch.network.models.BaseGetMasterModel
import vedam.subkuch.network.models.DataPart
import vedam.subkuch.network.models.GeneralResponse
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.jobs.models.CitiesResponse
import vedam.subkuch.ui.jobs.models.Job
import vedam.subkuch.ui.jobs.models.JobCategory
import vedam.subkuch.ui.jobs.models.JobExperienceResponse
import vedam.subkuch.ui.jobs.models.JobMelaRequest
import vedam.subkuch.ui.jobs.models.JobQualificationResponse
import vedam.subkuch.ui.jobs.models.JobSalaryResponse

class JobsRepository(
    private val api: JobsApi = JobApiClient.getRetrofit().create()
) {
    suspend fun getCategories(): JobsResult<List<JobCategory>> = try {
        val response = api.getCategories()
        val body = response.body()
        if (!response.isSuccessful || body?.returnCode != SUCCESS_CODE) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body.returnData?.toList().orEmpty())
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getJobs(
        categoryId: String,
        search: String,
        pageIndex: Int,
        pageSize: Int,
        gender: Int
    ): JobsResult<List<Job>> = try {
        val response = api.getJobs(categoryId, search, pageIndex, pageSize, gender)
        val body = response.body()
        if (!response.isSuccessful || body?.status != SUCCESS_STATUS) {
            JobsResult.Error(body?.message)
        } else {
            JobsResult.Success(body.jobsResult?.jobs?.toList().orEmpty())
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getCities(): JobsResult<CitiesResponse> = try {
        val response = api.getCities()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun addJob(json: String): JobsResult<AddResponse> = try {
        val requestBody = json.toRequestBody(JSON_MEDIA_TYPE)
        val response = api.addJob(requestBody)
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getJobQualifications(): JobsResult<JobQualificationResponse> = try {
        val response = api.getJobQualifications()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getJobExperiences(): JobsResult<JobExperienceResponse> = try {
        val response = api.getJobExperiences()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getJobSalaries(): JobsResult<JobSalaryResponse> = try {
        val response = api.getJobSalaries()
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun getJobProfile(profileId: String): JobsResult<BaseGetMasterModel<JobMelaRequest>> = try {
        val response = api.getJobProfile(profileId)
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun addJobProfile(json: String): JobsResult<GeneralResponse> = try {
        val requestBody = json.toRequestBody(JSON_MEDIA_TYPE)
        val response = api.addJobProfile(requestBody)
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    suspend fun uploadJobProfileImage(
        profileId: String,
        dataPartMap: Map<String?, DataPart?>
    ): JobsResult<GeneralResponse> = try {
        val profileIdBody = profileId.toRequestBody(TEXT_MEDIA_TYPE)
        val files = ArrayList<MultipartBody.Part>()
        dataPartMap.forEach { (name, dataPart) ->
            if (!name.isNullOrEmpty() && dataPart?.content != null) {
                val contentType = dataPart.type?.takeIf { it.isNotBlank() }
                    ?: DEFAULT_CONTENT_TYPE
                val body = dataPart.content.toRequestBody(contentType.toMediaType())
                files.add(MultipartBody.Part.createFormData(name, dataPart.fileName, body))
            }
        }
        val response = api.uploadJobProfileImage(profileIdBody, files)
        val body = response.body()
        if (!response.isSuccessful || body == null) {
            JobsResult.Error(body?.returnMessage)
        } else {
            JobsResult.Success(body)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        JobsResult.Error()
    }

    private companion object {
        const val SUCCESS_CODE = "1"
        const val SUCCESS_STATUS = "true"
        const val DEFAULT_CONTENT_TYPE = "application/octet-stream"
        val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
        val TEXT_MEDIA_TYPE = "text/plain".toMediaType()
    }
}

sealed interface JobsResult<out T> {
    data class Success<T>(val value: T) : JobsResult<T>
    data class Error(val message: String? = null) : JobsResult<Nothing>
}
