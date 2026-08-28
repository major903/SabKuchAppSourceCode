package vedam.subkuch.ui.jobs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentJobsBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.Response
import vedam.subkuch.network.models.Profile
import vedam.subkuch.network.models.ShareResponse
import vedam.subkuch.ui.jobs.jobmela.JobMelaActivity
import vedam.subkuch.ui.jobs.models.Job
import vedam.subkuch.ui.jobs.models.JobCategory
import vedam.subkuch.ui.jobs.models.Post
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.ShareUtils
import vedam.subkuch.utils.UiUtil
import java.util.Locale

class JobsFragment : BaseFragment(), OnListViewItemClickListener {
    private var _binding: FragmentJobsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val categoryId: String by lazy {
        arguments?.getString(Constants.EXTRA_CATEGORY_ID).orEmpty()
    }
    private val jobs = arrayListOf<Job>()
    private lateinit var adapter: JobsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private var shownError: String? = null
    private var genderLookupAttempted = false

    private val viewModel: JobsViewModel by viewModels {
        JobsViewModelFactory(categoryId, AppPrefs.getPrefsUserGender(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installMenu(R.menu.add_search) { item ->
            if (item.itemId == R.id.action_add) {
                startActivity(Intent(requireContext(), AddJobsActivity::class.java))
                true
            } else {
                false
            }
        }

        layoutManager = LinearLayoutManager(requireContext())
        adapter = JobsAdapter(requireContext(), jobs, this)
        binding.rvJobs.apply {
            layoutManager = this@JobsFragment.layoutManager
            setHasFixedSize(true)
            adapter = this@JobsFragment.adapter
            addOnScrollListener(PagingScrollListener())
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                viewModel.search(binding.etSearch.text?.toString().orEmpty())
                true
            } else {
                false
            }
        }
        binding.etSearch.addTextChangedListener(SimpleTextWatcher {
            binding.ibClose.isVisible = binding.etSearch.text?.isNotEmpty() == true
        })
        binding.ibClose.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.clearSearch()
        }

        loadCurrentGenderIfNeeded()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    override fun onDestroyView() {
        UiUtil.cancelProgressDialog()
        _binding = null
        super.onDestroyView()
    }

    private fun loadCurrentGenderIfNeeded() {
        if (AppPrefs.isUserGenderFromCurrentApi(requireContext()) || genderLookupAttempted) return
        genderLookupAttempted = true
        DataFetcher.getUserProfile(
            requireContext(),
            Response.Listener { profile: Profile? ->
                val gender = profile?.gender.toGenderCode()
                AppPrefs.getInstance(requireContext()).sharedPreferences.edit()
                    .putInt(AppPrefs.PREFS_USER_GENDER, gender)
                    .putBoolean(AppPrefs.PREFS_USER_GENDER_CURRENT_API, true)
                    .apply()
                viewModel.updateGender(gender)
            },
            Profile::class.java,
            Response.ErrorListener {
                // Jobs already loaded with the saved gender. A profile lookup must not block it.
            }
        )
    }

    private fun render(state: JobsUiState) {
        if (state.isLoading) {
            UiUtil.showProgressDialog(requireContext(), getString(R.string.please_wait))
        } else {
            UiUtil.cancelProgressDialog()
        }

        if (jobs != state.jobs) {
            jobs.clear()
            jobs.addAll(state.jobs)
            adapter.notifyDataSetChanged()
        }
        binding.rvJobs.isVisible = jobs.isNotEmpty()
        binding.tvEmptyJobs.isVisible = !state.isLoading && jobs.isEmpty() && state.errorMessage == null

        state.errorMessage?.let(::showErrorOnce) ?: run { shownError = null }
    }

    private fun showErrorOnce(message: String?) {
        val displayMessage = message?.takeIf { it.isNotBlank() } ?: getString(R.string.err_occurred)
        if (shownError != displayMessage) {
            shownError = displayMessage
            UiUtil.showToast(requireContext(), displayMessage)
        }
    }

    override fun <E> onItemClick(item: E, position: Int, view: View?, action: ListItemClickAction?) {
        when (item) {
            is Job -> getShareMessage(item)
            is Post -> startActivity(
                Intent(requireContext(), JobMelaActivity::class.java)
                    .putExtra(Constants.EXTRA_DATA, item)
            )
        }
    }

    private fun getShareMessage(job: Job) {
        UiUtil.showProgressDialog(requireContext(), R.string.loading)
        DataFetcher.getShareContent(
            requireContext(),
            Response.Listener { response: ShareResponse? ->
                UiUtil.cancelProgressDialog()
                if (response?.returnMessage == Constants.SUCCESS && response.returnData != null) {
                    ShareUtils.shareMessage(
                        requireContext(),
                        "Sharing this job ad with you. If you want to find jobs near your home install Sabkuch App from the link given below. \n\n" +
                            "https://play.google.com/store/apps/details?id=vedam.subkuch&referrer=${getCode(response.returnData)}\n\n" +
                            getShareJobPost(job),
                        null
                    )
                } else {
                    UiUtil.showToast(requireContext(), getString(R.string.err_occurred))
                }
            },
            ShareResponse::class.java,
            onErrorListener
        )
    }

    private fun getCode(value: String): String = value.substringAfter("referrer=", "")

    private fun getShareJobPost(job: Job): CharSequence? {
        val posts = job.posts ?: return null
        if (posts.isEmpty()) return null
        val jobTitles = posts.mapIndexed { index, post ->
            if (posts.size == 1) post.jobTitle.orEmpty() else "${index + 1}) ${post.jobTitle.orEmpty()}"
        }.joinToString(" ")
        return String.format(
            Locale.US,
            "%s dealing in %s located in %s is looking for %s\n%s",
            job.organisationName,
            job.dealingIn,
            job.city,
            jobTitles.trim(),
            job.howToContact
        )
    }

    private inner class PagingScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy <= 0) return
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            if (lastVisible >= adapter.itemCount - PAGING_THRESHOLD) {
                viewModel.loadNextPage()
            }
        }
    }

    private fun String?.toGenderCode(): Int = when {
        this == "1" || equals("male", ignoreCase = true) -> 1
        this == "2" || equals("female", ignoreCase = true) -> 2
        else -> 0
    }

    companion object {
        private const val PAGING_THRESHOLD = 4

        @JvmStatic
        fun newInstance(jobCategory: JobCategory) = JobsFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.EXTRA_CATEGORY_NAME, jobCategory.jobCategoryName)
                putString(Constants.EXTRA_CATEGORY_ID, jobCategory.jobCategoryId)
            }
        }
    }
}
