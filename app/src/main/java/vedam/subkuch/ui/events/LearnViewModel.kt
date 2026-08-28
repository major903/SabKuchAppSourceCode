package vedam.subkuch.ui.events

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vedam.subkuch.network.models.learn.LearnCategory
import vedam.subkuch.network.models.learn.LearnCourse

class LearnViewModel(
    private val repository: LearnRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LearnUiState())
    val uiState: StateFlow<LearnUiState> = _uiState.asStateFlow()

    private var allCourses: List<LearnCourse> = emptyList()
    private var requestJob: Job? = null

    init {
        loadLearnHome()
    }

    fun selectCategory(categoryId: Int) {
        requestJob?.cancel()
        if (categoryId == MY_COURSES_CATEGORY_ID) {
            loadMyCourses()
            return
        }

        if (categoryId == ALL_COURSES_CATEGORY_ID) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                selectedCategoryId = categoryId,
                courses = allCourses,
                query = "",
                hasError = false
            )
            return
        }

        loadCategory(categoryId)
    }

    fun searchCourses(query: String) {
        requestJob?.cancel()
        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) {
            selectCategory(ALL_COURSES_CATEGORY_ID)
            return
        }

        requestJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedCategoryId = ALL_COURSES_CATEGORY_ID,
                hasError = false,
                query = normalizedQuery
            )
            val courses = repository.searchCourses(normalizedQuery)
            _uiState.value = if (courses == null) {
                _uiState.value.copy(isLoading = false, courses = emptyList(), hasError = true)
            } else {
                _uiState.value.copy(isLoading = false, courses = courses)
            }
        }
    }

    private fun loadLearnHome() {
        requestJob = viewModelScope.launch {
            _uiState.value = LearnUiState(isLoading = true)
            val result = repository.getLearnHome()
            if (result == null) {
                _uiState.value = LearnUiState(hasError = true)
                return@launch
            }

            allCourses = result.courses
            _uiState.value = LearnUiState(
                categories = result.categories,
                courses = allCourses,
                isHomeLoaded = true
            )
        }
    }

    private fun loadMyCourses() {
        requestJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedCategoryId = MY_COURSES_CATEGORY_ID,
                query = "",
                hasError = false
            )
            val courses = repository.getMyCourses()
            _uiState.value = if (courses == null) {
                _uiState.value.copy(isLoading = false, courses = emptyList(), hasError = true)
            } else {
                _uiState.value.copy(isLoading = false, courses = courses)
            }
        }
    }

    private fun loadCategory(categoryId: Int) {
        requestJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedCategoryId = categoryId,
                query = "",
                hasError = false
            )
            val courses = repository.getCoursesByCategory(categoryId)
            _uiState.value = if (courses == null) {
                _uiState.value.copy(isLoading = false, courses = emptyList(), hasError = true)
            } else {
                _uiState.value.copy(isLoading = false, courses = courses)
            }
        }
    }

    companion object {
        const val ALL_COURSES_CATEGORY_ID = 0
        const val MY_COURSES_CATEGORY_ID = -1
        private const val SEARCH_DEBOUNCE_MILLIS = 350L
    }
}

data class LearnUiState(
    val categories: List<LearnCategory> = emptyList(),
    val courses: List<LearnCourse> = emptyList(),
    val isLoading: Boolean = false,
    val isHomeLoaded: Boolean = false,
    val selectedCategoryId: Int = LearnViewModel.ALL_COURSES_CATEGORY_ID,
    val query: String = "",
    val hasError: Boolean = false
)

class LearnViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val applicationContext = context.applicationContext

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LearnViewModel::class.java)) {
            return LearnViewModel(LearnRepository(applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
