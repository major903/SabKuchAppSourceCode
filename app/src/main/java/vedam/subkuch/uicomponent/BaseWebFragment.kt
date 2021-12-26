package vedam.subkuch.uicomponent

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.helpers.Constants
import vedam.subkuch.utils.UiUtil

class BaseWebFragment : BaseFragment() {
    private var url: String? = null

    companion object {
        fun newInstance(extras: Bundle?): BaseWebFragment {
//            val args = Bundle()
//            args.putString(Constants.EXTRA_URL, url)
//            args.putString(Constants.EXTRA_NAME, name)
            val fragment = BaseWebFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(Constants.EXTRA_URL)
        setTitle(arguments?.getString(Constants.EXTRA_NAME))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.findViewById<WebView>(R.id.webview)
        webView.loadUrl(url!!)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (activity != null)
                UiUtil.showProgressDialog(context, getString(R.string.loading))
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            UiUtil.cancelProgressDialog()
        }
    }
}