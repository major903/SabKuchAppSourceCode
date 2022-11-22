package vedam.subkuch.ui.shopping

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        val type = object : TypeToken<BaseShoppingResponse<Product>>() {}.type
        DataFetcher.getShoppingProductDetails(mContext, onSubcategoriesSuccessListener, type, onErrorListener, shoppingId
                ?: "")
    }

    private fun initUI() {
        binding?.ivShare?.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        Dexter.withContext(mContext).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val text = "${mTitle}\n\nSharing this item with you. If you wish to do window shopping in your city install Sabkuch App from the link given below. \n" +
                                "\n" +
                                "https://play.google.com/store/apps/details?id=vedam.subkuch&referrer=KK47"
                        ShareUtils.shareImageWithMessage(requireContext(), imageUrl, text, null, object : ShareUtilsListener {
                            override fun onShareStarted() {
                                UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
                            }

                            override fun onShared() {
                                UiUtil.cancelProgressDialog()
                            }

                            override fun onShareError(e: Exception?) {
                                Toast.makeText(
                                        mContext,
                                        R.string.err_unknown,
                                        Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onTargetAppNotInstalledError() {
                                Toast.makeText(
                                        mContext,
                                        R.string.err_unknown,
                                        Toast.LENGTH_LONG
                                ).show()
                            }

                        })
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        p0?.let {
                            if (it.isPermanentlyDenied) {
                                activity?.let { act ->
                                    ShareUtils.showSettingsDialog(act)
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                }).check()
    }

    private val onSubcategoriesSuccessListener: Response.Listener<BaseShoppingResponse<Product>> = Response.Listener { response ->
        UiUtil.cancelProgressDialog()
        if (activity != null) if (response != null && response.status == true) {
            if (response.result?.list?.size ?: 0 > 0) {
                loadUI(response.result?.list)
            } else UiUtil.showToast(mContext, getString(R.string.no_data))
        } else UiUtil.showToast(mContext, getString(R.string.err_occurred))
    }

    private fun loadUI(list: List<Product>?) {

        UiUtil.setImageView(ImageSetter.ImageBuilder(mContext)
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
                str += SimpleSpanBuilder.Companion.Span("Rs. ${it.Price}", ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.reddish_brown)))
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

interface ShareUtilsListener {
    fun onShareStarted()
    fun onShared()
    fun onShareError(e: Exception?)
    fun onTargetAppNotInstalledError()
}