package vedam.subkuch.ui.shopping

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentProductDetailsBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.models.shopping.BaseShoppingResponse
import vedam.subkuch.network.models.shopping.Product
import vedam.subkuch.utils.ImageSetter
import vedam.subkuch.utils.ShareUtils
import vedam.subkuch.utils.SimpleSpanBuilder
import vedam.subkuch.utils.UiUtil

class ProductDetailsFragment : BaseFragment() {

    private var binding: FragmentProductDetailsBinding? = null
    private var shoppingId: String? = null
    private var mTitle: String? = null
    private var imageUrl: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_details, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoppingId = arguments?.getString(Constants.EXTRA_SHOPPING_ID)
        mTitle = arguments?.getString(Constants.EXTRA_NAME) ?: ""
        setTitle(mTitle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        getProductDetails()
    }

    private fun getProductDetails() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        val type = object : TypeToken<BaseShoppingResponse<Product>>() {}.type
        DataFetcher.getShoppingProductDetails(context, onSubcategoriesSuccessListener, type, onErrorListener, shoppingId
                ?: "")
    }

    private fun initUI() {
        binding?.ivShare?.setOnClickListener {
            val text = "${mTitle}\nSharing this item with you. If you wish to do window shopping in your city install Sabkuch App from the link given below. \n" +
                    "\n" +
                    "https://play.google.com/store/apps/details?id=vedam.subkuch&referrer=KK47"
            ShareUtils.shareImageWithMessage(context, imageUrl, text, null)
        }
    }

    private val onSubcategoriesSuccessListener: Response.Listener<BaseShoppingResponse<Product>> = Response.Listener { response ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == true) {
            if (response.result?.list?.size ?: 0 > 0) {
                loadUI(response.result?.list)
            } else UiUtil.showToast(context, getString(R.string.no_data))
        } else UiUtil.showToast(context, getString(R.string.err_occurred))
    }

    private fun loadUI(list: List<Product>?) {

        UiUtil.setImageView(ImageSetter.ImageBuilder(context)
                .setImageLink(list?.get(0)?.Image1)
                .setDefaults()
                .setTarget(binding?.ivProduct)
                .build())
        imageUrl = list?.get(0)?.Image1
        UiUtil.setTextView(binding?.tvName, list?.get(0)?.ItemName)
        UiUtil.setTextView(binding?.tvBrand, list?.get(0)?.BrandName)
        UiUtil.setTextView(binding?.tvItemCode, list?.get(0)?.ItemCode)
        UiUtil.setTextView(binding?.tvDescription, list?.get(0)?.ItemDescriptionLong
                ?: list?.get(0)?.ItemDescriptionShort)

        var str = SimpleSpanBuilder("")
        list?.forEach {
            if (it.Price != null)
                str += SimpleSpanBuilder.Companion.Span("Rs. ${it.Price}", ForegroundColorSpan(ContextCompat.getColor(context, R.color.reddish_brown)))
            if (it.VendorName != null)
                str += SimpleSpanBuilder.Companion.Span(" ${it.VendorName}", ForegroundColorSpan(Color.BLACK))
            if (it.Distance != null)
                str += SimpleSpanBuilder.Companion.Span(" ${it.Distance} Kms away\n", ForegroundColorSpan(Color.BLACK))
        }
        UiUtil.setTextView(binding?.tvPrice, str.build())
    }

    companion object {
        fun newInstance(shoppingId: String?, itemName: String?) = ProductDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(Constants.EXTRA_SHOPPING_ID, shoppingId)
                putString(Constants.EXTRA_NAME, itemName)
            }
        }
    }
}