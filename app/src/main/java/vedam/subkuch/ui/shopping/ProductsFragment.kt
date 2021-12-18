package vedam.subkuch.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentProductsBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.models.shopping.BaseShoppingResponse
import vedam.subkuch.network.models.shopping.Product
import vedam.subkuch.utils.ItemOffsetDecoration
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil

class ProductsFragment : BaseFragment(), OnListViewItemClickListener {

    private var binding: FragmentProductsBinding? = null
    private var productsAdapter: ProductAdapter? = null
    private var subcategoryId: String? = null
    private var subcategoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subcategoryId = arguments?.getString(Constants.EXTRA_SUB_CATEGORY_ID)
        subcategoryName = arguments?.getString(Constants.EXTRA_SUB_CATEGORY_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        getProducts()
        setTitle(subcategoryName)
    }

    private fun initUI() {
        productsAdapter = ProductAdapter(this)
        binding?.rvProducts?.adapter = productsAdapter
        val dimen = resources.getDimensionPixelSize(R.dimen.margin_8dp)
        val itemDecoration = ItemOffsetDecoration(2, dimen, false)
        binding?.rvProducts?.addItemDecoration(itemDecoration)
    }

    private fun getProducts() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val type = object : TypeToken<BaseShoppingResponse<Product>>() {}.type
        DataFetcher.getProducts(context, onProductsSuccessListener, type, onErrorListener, subcategoryId)
    }

    private val onProductsSuccessListener: Response.Listener<BaseShoppingResponse<Product>> = Response.Listener { response ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == true) {
            if (response.result?.list?.size ?: 0 > 0) {
                loadProducts(response.result?.list)
            } else UiUtil.showToast(context, getString(R.string.no_products_found))
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun loadProducts(response: List<Product>?) {
        if (response != null && response.isNotEmpty()) {
            productsAdapter?.submitList(response)
        }
    }

    companion object {
        fun newInstance(subcategoryId: String?, name: String?) = ProductsFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.EXTRA_SUB_CATEGORY_ID, subcategoryId)
                putString(Constants.EXTRA_SUB_CATEGORY_NAME, name)
            }
        }
    }

    override fun <E> onItemClick(item: E, position: Int, view: View?, action: ListItemClickAction?) {

    }
}