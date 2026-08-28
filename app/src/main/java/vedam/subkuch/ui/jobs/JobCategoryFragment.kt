package vedam.subkuch.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.ui.jobs.models.JobCategory
import vedam.subkuch.utils.UiUtil

class JobCategoryFragment : BaseFragment() {
    private var listView: ListView? = null
    private var shownError: String? = null

    private val viewModel: JobCategoriesViewModel by viewModels {
        JobCategoriesViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_directory, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(android.R.id.list)
        listView?.setOnItemClickListener { _, _, position, _ ->
            val category = listView?.adapter?.getItem(position) as? JobCategory ?: return@setOnItemClickListener
            addFragment(
                R.id.content_frame,
                JobsFragment.newInstance(category),
                null,
                true,
                0,
                0,
                0,
                0
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    override fun onDestroyView() {
        listView = null
        super.onDestroyView()
    }

    private fun render(state: JobCategoriesUiState) {
        if (state.isLoading) {
            UiUtil.showProgressDialog(requireContext(), getString(R.string.please_wait))
            return
        }
        UiUtil.cancelProgressDialog()

        if (state.errorMessage != null) {
            showErrorOnce(state.errorMessage)
            return
        }

        shownError = null
        if (state.categories.isEmpty()) {
            UiUtil.showToast(requireContext(), getString(R.string.no_data))
        } else {
            listView?.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                state.categories
            )
        }
    }

    private fun showErrorOnce(message: String?) {
        val displayMessage = message?.takeIf { it.isNotBlank() } ?: getString(R.string.err_occurred)
        if (shownError != displayMessage) {
            shownError = displayMessage
            UiUtil.showToast(requireContext(), displayMessage)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = JobCategoryFragment()
    }
}
