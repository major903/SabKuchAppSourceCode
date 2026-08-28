package vedam.subkuch.ui.events

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentEventBinding
import vedam.subkuch.network.models.learn.LearnCourse
import vedam.subkuch.utils.UiUtil

class EventFragment : BaseFragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val learnViewModel: LearnViewModel by viewModels {
        LearnViewModelFactory(requireContext())
    }

    private val categoryChips = arrayListOf<TextView>()
    private lateinit var courseAdapter: LearnCourseAdapter
    private var filtersCreated = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseAdapter = LearnCourseAdapter(requireContext(), ::openCourse, ::openCourse)
        binding.lvLearnCourses.adapter = courseAdapter
        binding.etLearnSearch.doAfterTextChanged { text ->
            learnViewModel.searchCourses(text?.toString().orEmpty())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                learnViewModel.uiState.collect(::render)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        filtersCreated = false
        categoryChips.clear()
    }

    private fun render(state: LearnUiState) {
        showLoading(state.isLoading)

        if (state.isHomeLoaded && !filtersCreated) {
            createCategoryFilters(state.categories)
            filtersCreated = true
        }
        updateSelectedCategory(state.selectedCategoryId)

        when {
            state.hasError -> {
                binding.lvLearnCourses.isVisible = false
                showEmpty(getString(R.string.learn_load_error))
            }
            state.isHomeLoaded -> showCourses(
                state.courses,
                state.selectedCategoryId == LearnViewModel.MY_COURSES_CATEGORY_ID
            )
            else -> Unit
        }
    }

    private fun createCategoryFilters(categories: List<vedam.subkuch.network.models.learn.LearnCategory>) {
        binding.llLearnCategories.removeAllViews()
        categoryChips.clear()
        addCategoryChip(getString(R.string.learn_all), LearnViewModel.ALL_COURSES_CATEGORY_ID)
        addCategoryChip(getString(R.string.learn_my_courses), LearnViewModel.MY_COURSES_CATEGORY_ID)
        categories.forEach { category ->
            addCategoryChip(category.name, category.courseCategoryId)
        }
    }

    private fun addCategoryChip(title: String?, categoryId: Int) {
        val chip = TextView(requireContext()).apply {
            text = title.orEmpty()
            tag = categoryId
            gravity = Gravity.CENTER
            textSize = 14f
            setPadding(dp(18), 0, dp(18), 0)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dp(42)
            ).apply {
                marginEnd = dp(8)
            }
            setOnClickListener {
                binding.etLearnSearch.text?.clear()
                learnViewModel.selectCategory(categoryId)
            }
        }
        categoryChips += chip
        binding.llLearnCategories.addView(chip)
    }

    private fun updateSelectedCategory(categoryId: Int) {
        categoryChips.forEach { chip ->
            val selected = chip.tag == categoryId
            chip.setBackgroundResource(
                if (selected) R.drawable.bg_learn_chip_selected else R.drawable.bg_learn_chip_unselected
            )
            chip.setTextColor(
                ContextCompat.getColor(requireContext(), if (selected) R.color.white else R.color.brand_blue)
            )
            chip.setTypeface(null, if (selected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun openCourse(course: LearnCourse) {
        startActivity(LearnCourseDetailsActivity.newIntent(requireContext(), course.courseId))
    }

    private fun showCourses(courses: List<LearnCourse>, enrolledCourses: Boolean) {
        courseAdapter.setCourses(ArrayList(courses), enrolledCourses)
        binding.lvLearnCourses.isVisible = courses.isNotEmpty()
        val emptyMessage = when {
            enrolledCourses -> R.string.learn_no_my_courses
            binding.etLearnSearch.text.isNullOrBlank() -> R.string.learn_no_courses
            else -> R.string.learn_no_search_results
        }
        showEmpty(if (courses.isEmpty()) getString(emptyMessage) else null)
    }

    private fun showLoading(loading: Boolean) {
        binding.progressLearn.isVisible = false
        if (loading) {
            UiUtil.showProgressDialog(requireContext(), R.string.please_wait)
            showEmpty(null)
        } else {
            UiUtil.cancelProgressDialog()
        }
    }

    private fun showEmpty(message: String?) {
        binding.tvLearnEmpty.isVisible = message != null
        if (message != null) binding.tvLearnEmpty.text = message
    }

    companion object {
        @JvmStatic
        fun newInstance() = EventFragment()
    }
}
