package vedam.subkuch.ui.events

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityLearnWebsiteBinding

/** Displays Sabkuch course pages without sending the user to an external browser. */
class LearnWebsiteActivity : BaseActivity() {
    private lateinit var binding: ActivityLearnWebsiteBinding
    private lateinit var initialUrl: String
    private var failedUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnWebsiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarBackButton()
        setTitle(intent.getStringExtra(EXTRA_TITLE).orEmpty().ifBlank {
            getString(R.string.learn_course_details)
        })

        initialUrl = intent.getStringExtra(EXTRA_URL).orEmpty()
        if (!isTrustedWebUrl(Uri.parse(initialUrl))) {
            Toast.makeText(this, R.string.learn_purchase_link_unavailable, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        configureWebView()
        binding.btnLearnWebRetry.setOnClickListener {
            loadPage(failedUrl ?: initialUrl)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webLearnWebsite.canGoBack()) {
                    binding.webLearnWebsite.goBack()
                } else {
                    finish()
                }
            }
        })

        if (savedInstanceState == null) {
            loadPage(initialUrl)
        } else {
            if (binding.webLearnWebsite.restoreState(savedInstanceState) == null) {
                loadPage(initialUrl)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView() = with(binding.webLearnWebsite) {
        setBackgroundColor(getColor(R.color.white))
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false
            mediaPlaybackRequiresUserGesture = true
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            allowFileAccess = false
            allowContentAccess = false
            setGeolocationEnabled(false)
            safeBrowsingEnabled = true
        }
        CookieManager.getInstance().apply {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(this@with, false)
        }
        webViewClient = CourseWebViewClient()
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                binding.progressLearnWebsite.progress = newProgress
                binding.progressLearnWebsite.isVisible = newProgress in 0..99
            }
        }
    }

    private fun loadPage(url: String) {
        failedUrl = null
        binding.layoutLearnWebError.isVisible = false
        binding.webLearnWebsite.isVisible = true
        binding.progressLearnWebsite.isVisible = true
        binding.webLearnWebsite.loadUrl(url)
    }

    private fun showLoadError(url: String?) {
        failedUrl = url?.takeIf { it.isNotBlank() } ?: initialUrl
        binding.progressLearnWebsite.isVisible = false
        binding.webLearnWebsite.isVisible = false
        binding.layoutLearnWebError.isVisible = true
    }

    private fun handleNavigation(uri: Uri): Boolean {
        if (isTrustedWebUrl(uri)) return false
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (exception: Exception) {
            Toast.makeText(this, R.string.learn_web_link_error, Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun isTrustedWebUrl(uri: Uri): Boolean {
        if (!uri.scheme.equals("https", ignoreCase = true)) return false
        val host = uri.host?.lowercase().orEmpty()
        return host == SABKUCH_HOST || host.endsWith(".$SABKUCH_HOST")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.webLearnWebsite.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        binding.webLearnWebsite.apply {
            stopLoading()
            webChromeClient = null
            webViewClient = WebViewClient()
            loadUrl("about:blank")
            clearHistory()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }

    private inner class CourseWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean =
            handleNavigation(request.url)

        @Deprecated("Used for WebView implementations that call the legacy navigation callback")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean =
            handleNavigation(Uri.parse(url))

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            binding.layoutLearnWebError.isVisible = false
            binding.webLearnWebsite.isVisible = true
            binding.progressLearnWebsite.isVisible = true
        }

        override fun onPageFinished(view: WebView, url: String) {
            binding.progressLearnWebsite.isVisible = false
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            if (request.isForMainFrame) showLoadError(request.url.toString())
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            if (request.isForMainFrame && errorResponse.statusCode >= 400) {
                showLoadError(request.url.toString())
            }
        }

        override fun onReceivedSslError(
            view: WebView,
            handler: SslErrorHandler,
            error: SslError
        ) {
            handler.cancel()
            showLoadError(error.url)
        }
    }

    companion object {
        private const val EXTRA_URL = "extra_url"
        private const val EXTRA_TITLE = "extra_title"
        private const val SABKUCH_HOST = "sabkuchworld.com"

        @JvmStatic
        fun newIntent(context: Context, url: String, title: String?): Intent =
            Intent(context, LearnWebsiteActivity::class.java)
                .putExtra(EXTRA_URL, url)
                .putExtra(EXTRA_TITLE, title)
    }
}
