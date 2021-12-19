package vedam.subkuch.ui.shopping

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentShoppingBinding
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.models.shopping.BaseShoppingResponse
import vedam.subkuch.network.models.shopping.Product
import vedam.subkuch.network.models.shopping.ShoppingSubCategory
import vedam.subkuch.utils.ItemOffsetDecoration
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil
import java.util.*

class ShoppingFragment : BaseFragment(), OnListViewItemClickListener {

    private var binding: FragmentShoppingBinding? = null

    private var subCatAdapter: SubCatAdapter? = null
    private var productsAdapter: ProductAdapter? = null
    private val productsList = ArrayList<Product>()
    var layoutManager: GridLayoutManager? = null
    private var loading = true
    private var pageNo = 1
    private val pageSize = 20
    private var hasMoreProjects = true
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        getSubcategories()
        getProducts()
    }

    private fun initUI() {
        subCatAdapter = SubCatAdapter(this)
        productsAdapter = ProductAdapter(this)
        layoutManager = GridLayoutManager(context, 2)
        binding?.rvSubcategories?.adapter = subCatAdapter
        binding?.rvProducts?.adapter = productsAdapter
        binding?.rvProducts?.layoutManager = layoutManager
        val dimen = resources.getDimensionPixelSize(R.dimen.margin_8dp)
        val itemDecoration = ItemOffsetDecoration(2, dimen, false)
        binding?.rvProducts?.addItemDecoration(itemDecoration)
        binding?.rvProducts?.addOnScrollListener(ShoppingOnScrollListener())
    }

    private fun getSubcategories() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val type = object : TypeToken<BaseShoppingResponse<ShoppingSubCategory>>() {}.type
        DataFetcher.getShoppingSubCategories(context, onSubcategoriesSuccessListener, type, onErrorListener)
    }

    private fun getProducts() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val type = object : TypeToken<BaseShoppingResponse<Product>>() {}.type
        DataFetcher.getHomeProducts(context, onProductsSuccessListener, type, onErrorListener, pageNo, pageSize)
    }

    private val onSubcategoriesSuccessListener: Response.Listener<BaseShoppingResponse<ShoppingSubCategory>> = Response.Listener { response ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == true) {
            if (response.result?.list?.size ?: 0 > 0) {
                loadSubcategories(response.result?.list)
            } else UiUtil.showToast(context, getString(R.string.no_data))
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private val onProductsSuccessListener: Response.Listener<BaseShoppingResponse<Product>> = Response.Listener { response ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == true) {
            if (response.result?.list?.size ?: 0 > 0) {
                hasMoreProjects = response.result?.list?.size!! >= pageSize
                loading = true
                loadProducts(response.result?.list)
            } else UiUtil.showToast(context, getString(R.string.no_products_found))
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun loadSubcategories(list: List<ShoppingSubCategory>?) {
        subCatAdapter?.submitList(list)
    }

    private fun loadProducts(response: List<Product>?) {
        if (response != null && response.isNotEmpty()) {
            pageNo++
            productsList.addAll(response)
            productsAdapter?.submitList(productsList) {
                productsAdapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newInstance() = ShoppingFragment()
    }

    inner class ShoppingOnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) //check for scroll down
            {
//                binding?.rvSubcategories?.hide()
                val visibleItemCount: Int = layoutManager!!.childCount
                val totalItemCount: Int = layoutManager!!.itemCount
                val pastVisibleItems: Int = layoutManager!!.findFirstVisibleItemPosition()
                if (loading) {
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        loading = false
                        if (hasMoreProjects) getProducts()
                    }
                }
                return
            }
//            if (dy < 0) {
//                binding?.rvSubcategories?.show()
//            }
        }
    }

    override fun <E> onItemClick(item: E, position: Int, view: View?, action: ListItemClickAction?) {
        if (item is ShoppingSubCategory)
            addFragmentWithAnimation(R.id.content_frame, ProductsFragment.newInstance(item.ShoppingSubcatid, item.Name), null, true)
    }
}

fun View.fadeOut() {
    val duration = resources.getInteger(android.R.integer.config_shortAnimTime)
    animate().alpha(0f).setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    visibility = View.GONE
                    alpha = 1f
                }
            })
}

fun View.fadeIn() {
    val duration = resources.getInteger(android.R.integer.config_shortAnimTime)
    alpha = 0f
    visibility = View.VISIBLE
    animate().alpha(1f).setDuration(duration.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {

                }
            })
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}