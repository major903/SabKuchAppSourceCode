package vedam.subkuch.ui.directory

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.android.volley.Response
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.*
import vedam.subkuch.R
import vedam.subkuch.base.BaseListFragment
import vedam.subkuch.databinding.FragmentDirectoryBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.getCategories
import vedam.subkuch.network.DataFetcher.searchBusiness
import vedam.subkuch.ui.directory.models.Business
import vedam.subkuch.ui.directory.models.Category
import vedam.subkuch.ui.directory.models.CategoryResponse
import vedam.subkuch.ui.directory.models.DirectoryDetailResponse
import vedam.subkuch.ui.shopping.show
import vedam.subkuch.utils.Extensions.Companion.hideKeyboard
import vedam.subkuch.utils.Extensions.Companion.textChanges
import vedam.subkuch.utils.UiUtil
import java.util.*

class DirectoryFragment : BaseListFragment() {
    private var categories: ArrayList<Category>? = null
    private var binding: FragmentDirectoryBinding? = null
    private var businesses: ArrayList<Business> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FragmentDirectoryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        getCategories()
        initUI()
    }

    private fun initUI() {
        binding?.cvSearch?.show()
        binding?.etSearch!!.textChanges()
            .debounce(300)
            .flatMapLatest {
                showAutoSuggestion(it)
            }
            .launchIn(lifecycleScope)

        binding?.etSearch?.setOnItemClickListener { _, _, position, _ ->

            try {

                val business = businesses[position]

                binding?.etSearch?.setText("")
                binding?.etSearch?.dismissDropDown()
                binding?.etSearch?.hideKeyboard()

                addFragmentWithAnimation(
                    R.id.content_frame, DetailFragment.newInstance(business),
                    null, true
                )
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private val onSearchSuccessListener = Response.Listener { response: DirectoryDetailResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == Constants.TRUE) {
            businesses = response.businessesResult.businesses
            val adapter = ArrayAdapter(
                context, android.R.layout.simple_dropdown_item_1line,
                businesses
            )
            binding?.etSearch?.setAdapter(adapter)
            binding?.etSearch?.showDropDown()
//            loadValues(businesses)
        } else UiUtil.showToast(context, getString(R.string.no_data))
    }

    /**
     * Shows auto suggestion to chip input - shows the typed text as suggestion
     * won't make any API call
     * */
    private fun showAutoSuggestion(
        term: CharSequence?
    ): Flow<List<String>> {

        if (term?.trim()?.length ?: 0 > 2)
            searchBusiness(
                context,
                onSearchSuccessListener,
                DirectoryDetailResponse::class.java,
                onErrorListener,
                term
            )
        // returning dummy flow emit
        return flow {
            emit(listOf())
        }
    }

    private fun getCategories() {
        UiUtil.showProgressDialog(context, R.string.please_wait)
        getCategories(
            context,
            onCategorySuccessListener,
            CategoryResponse::class.java,
            onErrorListener
        )
    }

    private val onCategorySuccessListener = Response.Listener { response: CategoryResponse? ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == Constants.TRUE) {
            categories = response.categoryResult.categories
            loadValues()
        } else UiUtil.showToast(context, getString(R.string.no_data))
    }

    private fun loadValues() {
        val adapter = ArrayAdapter(
            context, android.R.layout.simple_list_item_1,
            android.R.id.text1, categories!!
        )
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val intent = Intent(
            activity,
            SubDirectoryActivity::class.java
        )
        val category = categories!![position]
        intent.putExtra(
            Constants.EXTRA_CATEGORY_ID,
            category.categoryId
        )
        intent.putExtra(Constants.EXTRA_CATEGORY_NAME, category.name)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.directory, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            startActivity(Intent(activity, AddDirectoryActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(): DirectoryFragment {
            return DirectoryFragment()
        }
    }
}