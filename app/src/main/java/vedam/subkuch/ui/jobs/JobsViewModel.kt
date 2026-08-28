package vedam.subkuch.ui.jobs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job as CoroutineJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vedam.subkuch.ui.jobs.models.Job
import vedam.subkuch.ui.jobs.models.JobCategory
import vedam.subkuch.ui.jobs.models.Post

class JobCategoriesViewModel(
    private val repository: JobsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JobCategoriesUiState(isLoading = true))
    val uiState: StateFlow<JobCategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun retry() = loadCategories()

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = JobCategoriesUiState(isLoading = true)
            _uiState.value = when (val result = repository.getCategories()) {
                is JobsResult.Success -> JobCategoriesUiState(categories = result.value)
                is JobsResult.Error -> JobCategoriesUiState(errorMessage = result.message)
            }
        }
    }
}

data class JobCategoriesUiState(
    val categories: List<JobCategory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class JobsViewModel(
    private val repository: JobsRepository,
    private val categoryId: String,
    initialGender: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(JobsUiState(isLoading = true))
    val uiState: StateFlow<JobsUiState> = _uiState.asStateFlow()

    private var requestJob: CoroutineJob? = null
    private var gender = initialGender
    private var search = ""
    private var nextPage = FIRST_PAGE
    private var hasMore = true
    private val loadedJobs = arrayListOf<Job>()

    init {
        refresh()
    }

    fun search(query: String) {
        search = query.trim()
        refresh()
    }

    fun clearSearch() {
        if (search.isEmpty()) return
        search = ""
        refresh()
    }

    fun updateGender(updatedGender: Int) {
        if (gender == updatedGender) return
        gender = updatedGender
        refresh()
    }

    fun loadNextPage() {
        if (_uiState.value.isLoading || !hasMore) return
        loadPage(reset = false)
    }

    private fun refresh() {
        requestJob?.cancel()
        nextPage = FIRST_PAGE
        hasMore = true
        loadedJobs.clear()
        loadPage(reset = true)
    }

    private fun loadPage(reset: Boolean) {
        requestJob?.cancel()
        requestJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                jobs = if (reset) emptyList() else loadedJobs.toList()
            )
            when (val result = repository.getJobs(categoryId, search, nextPage, PAGE_SIZE, gender)) {
                is JobsResult.Success -> {
                    val returnedJobs = result.value
                    hasMore = returnedJobs.size >= PAGE_SIZE
                    val matchingJobs = filterJobsForCurrentUser(returnedJobs, gender)
                    if (matchingJobs.isEmpty() && loadedJobs.isEmpty()) hasMore = false
                    loadedJobs += matchingJobs
                    nextPage += 1
                    _uiState.value = JobsUiState(
                        jobs = loadedJobs.toList(),
                        hasMore = hasMore
                    )
                }

                is JobsResult.Error -> {
                    _uiState.value = JobsUiState(
                        jobs = loadedJobs.toList(),
                        hasMore = hasMore,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    private fun filterJobsForCurrentUser(jobs: List<Job>, userGender: Int): List<Job> {
        if (userGender == ANY_GENDER) return jobs
        return jobs.mapNotNull { job ->
            val matchingPosts: ArrayList<Post> = job.posts
                ?.filter { post -> post.gender == ANY_GENDER || post.gender == userGender }
                ?.let { ArrayList(it) }
                ?: arrayListOf()
            if (matchingPosts.isEmpty()) {
                null
            } else {
                job.posts = matchingPosts
                job
            }
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
        const val PAGE_SIZE = 20
        const val ANY_GENDER = 0
    }
}

data class JobsUiState(
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val hasMore: Boolean = true,
    val errorMessage: String? = null
)

class JobCategoriesViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobCategoriesViewModel::class.java)) {
            return JobCategoriesViewModel(JobsRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class JobsViewModelFactory(
    private val categoryId: String,
    private val gender: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobsViewModel::class.java)) {
            return JobsViewModel(JobsRepository(), categoryId, gender) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
