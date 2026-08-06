package vedam.subkuch.base

import android.Manifest
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Address
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import vedam.subkuch.network.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnFragmentInteractionListener
import vedam.subkuch.interfaces.ScreenChangeListener
import vedam.subkuch.locationProvider.LocationCallbacks
import vedam.subkuch.locationProvider.LocationProvider
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.ErrorResponse
import vedam.subkuch.network.models.WithdrawalRequest
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.profile.RegisterUserActivity
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.LogUtils
import vedam.subkuch.utils.TargetScreen
import vedam.subkuch.utils.UiUtil

/**
 * Created by msharm6 on 6/12/2016.
 */
abstract class BaseActivity : AppCompatActivity(), ScreenChangeListener,
    OnFragmentInteractionListener, LocationCallbacks {
    open var mToolbar: Toolbar? = null
    private var isAddressRequested = false
    private var shouldForce = false

    @JvmField
    protected var onErrorListener = Response.ErrorListener { error: ApiError ->
        LogUtils.LOGD("ERROR", error.message)
        onErrorReceived(error)
    }

    protected fun onErrorReceived(error: ApiError) {
        if (error is NetworkError) {
            UiUtil.showToast(this, this.getString(R.string.connectionError))
        } else if (error is TimeoutError) {
            UiUtil.showToast(this, this.getString(R.string.timeoutError))
        } else if (error is ParseError) {
            UiUtil.showToast(this, getString(R.string.err_parsing))
        } else if (error is AuthFailureError || error.networkResponse != null &&
            error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED
        ) {
            logout()
        } else {
            parseAndShowError(error)
        }
        UiUtil.cancelProgressDialog()
    }

    protected fun parseAndShowError(error: ApiError) {
        val networkResponse = error.networkResponse
        if (networkResponse != null && networkResponse.data != null) {
            val response = String(networkResponse.data)
            LogUtils.LOGE(TAG, "response error:$response")
            try {
                val errorResponse = Gson().fromJson(response, ErrorResponse::class.java)
                if (!TextUtils.isEmpty(errorResponse.returnMessage)) UiUtil.showToast(
                    this,
                    errorResponse.returnMessage
                ) else if (!TextUtils.isEmpty(errorResponse.message)) UiUtil.showToast(
                    this,
                    errorResponse.message
                ) else UiUtil.showToast(this, getString(R.string.err_occurred))
            } catch (exception: Exception) {
                FirebaseCrashlytics.getInstance().recordException(exception)
                exception.printStackTrace()
                UiUtil.showToast(this, getString(R.string.err_occurred))
            }
        } else {
            UiUtil.showToast(this, getString(R.string.err_unknown))
        }
    }

    override fun logout() {
        AppPrefs.getInstance(this).sharedPreferences.edit().clear().apply()
        val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent(this, RegisterUserActivity::class.java).addFlags(flags))
        UiUtil.showToast(this, getString(R.string.err_unauthorized))
    }

    override fun setTitle(title: CharSequence) {
        var isToolbarSet = true
        if (mToolbar == null) isToolbarSet = setToolbar()
        if (isToolbarSet) {
            val toolbarTitle = mToolbar!!.findViewById<TextView>(R.id.tv_toolbar_title)
            toolbarTitle.text = title
        }
    }

    override fun setTitle(resourceId: Int) {
        var isToolbarSet = true
        if (mToolbar == null) isToolbarSet = setToolbar()
        if (isToolbarSet) {
            val toolbarTitle = mToolbar!!.findViewById<TextView>(R.id.tv_toolbar_title)
            toolbarTitle.text = getString(resourceId)
        }
    }

    /**
     * Set back button on Toolbar
     */
    protected fun setToolbarBackButton() {
        if (mToolbar == null) setToolbar()
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureNavigationBarAppearance()
        //        AnalyticsManager.setupGoogleAnalyticsForActivity(this, this.getClass().getName());
    }

    private fun configureNavigationBarAppearance() {
        val isLightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK !=
                Configuration.UI_MODE_NIGHT_YES
        // Keep the app surface light; only the system navigation area follows night mode.
        window.decorView.setBackgroundColor(Color.WHITE)
        @Suppress("DEPRECATION")
        window.navigationBarColor = if (isLightMode) Color.WHITE else Color.BLACK
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = isLightMode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = true
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        applyNavigationBarInsets()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        applyNavigationBarInsets()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        applyNavigationBarInsets()
    }

    private fun applyNavigationBarInsets() {
        val contentView = findViewById<View>(android.R.id.content) ?: return
        val screenRoot = (contentView as? ViewGroup)?.getChildAt(0)
        if (screenRoot?.fitsSystemWindows == true) return

        val initialPaddingLeft = contentView.paddingLeft
        val initialPaddingTop = contentView.paddingTop
        val initialPaddingRight = contentView.paddingRight
        val initialPaddingBottom = contentView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, windowInsets ->
            val navigationBars: Insets =
                windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(
                initialPaddingLeft + navigationBars.left,
                initialPaddingTop,
                initialPaddingRight + navigationBars.right,
                initialPaddingBottom + navigationBars.bottom
            )
            windowInsets
        }
        ViewCompat.requestApplyInsets(contentView)
    }

    private fun setToolbar(): Boolean {
        mToolbar = findViewById(R.id.toolbar)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            if (supportActionBar != null) supportActionBar!!.setDisplayShowTitleEnabled(false)
            return true
        }
        return false
    }

    fun getToolbar(): Toolbar? {
        if (mToolbar == null) setToolbar()
        return mToolbar
    }

    @JvmOverloads
    fun replaceFragment(
        containerId: Int, fragment: Fragment?, tag: String? = null, addToBackStack: Boolean = false,
        @AnimRes enterAnim: Int = 0, @AnimRes exitAnim: Int = 0,
        @AnimRes enterAnimPop: Int = 0, @AnimRes exitAnimPop: Int = 0
    ) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop)
        }
        ft.replace(containerId, fragment!!, tag)
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commit()
    }

    protected fun addFragmentWithAnimation(
        containerId: Int,
        fragment: Fragment?,
        tag: String?,
        addToBackStack: Boolean
    ) {
        addFragment(
            containerId,
            fragment,
            tag,
            addToBackStack,
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    }

    @JvmOverloads
    fun addFragment(
        containerId: Int, fragment: Fragment?, tag: String? = null, addToBackStack: Boolean = false,
        @AnimRes enterAnim: Int = 0, @AnimRes exitAnim: Int = 0,
        @AnimRes enterAnimPop: Int = 0, @AnimRes exitAnimPop: Int = 0
    ) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop)
        }
        ft.add(containerId, fragment!!, tag)
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commit()
    }

    @JvmOverloads
    fun addFragmentWithStateLoss(
        containerId: Int, fragment: Fragment?, tag: String? = null, addToBackStack: Boolean = false,
        @AnimRes enterAnim: Int = 0, @AnimRes exitAnim: Int = 0,
        @AnimRes enterAnimPop: Int = 0, @AnimRes exitAnimPop: Int = 0
    ) {
        val manager = supportFragmentManager
        val ft = manager.beginTransaction()
        if (enterAnim != 0 || exitAnim != 0 || enterAnimPop != 0 || exitAnimPop != 0) {
            ft.setCustomAnimations(enterAnim, exitAnim, enterAnimPop, exitAnimPop)
        }
        ft.add(containerId, fragment!!, tag)
        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        ft.commitAllowingStateLoss()
    }

    override fun onScreenChange(
        targetScreen: TargetScreen,
        bundle: Bundle,
        finishCurrentActivity: Boolean,
        flags: Int
    ) {
        val intent = Intent(applicationContext, targetScreen.targetScreenClass)
        if (bundle != null) intent.putExtras(bundle)
        if (flags != 0) intent.addFlags(flags)
        startActivity(intent)
        if (finishCurrentActivity) finish()
    }

    override fun handleActivityIntent(intent: Intent) {
        startActivity(intent)
    }

    override fun handleServiceIntent(intent: Intent) {
        startService(intent)
    }

    override fun handleActivityForResultIntent(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateBack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun navigateBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    /**
     * Request user's current location and delivers the result in the [.onLocationChanged] callback
     */
    override fun requestLocation(shouldForce: Boolean) {
        isAddressRequested = false
        this.shouldForce = shouldForce
        requestLocationProvider()
    }

    /**
     * Request user's current address and delivers the result in the [.onAddressChanged] callback
     */
    override fun requestAddress(shouldForce: Boolean) {
        isAddressRequested = true
        this.shouldForce = shouldForce
        requestLocationProvider()
    }

    override fun setFragmentResult(result: Int, data: Intent) {
        setResult(result, data)
        finishActivity()
    }

    override fun finishActivity() {
        finish()
    }

    //Start Location
    private fun requestLocationProvider() {
        val locationProvider = LocationProvider.getInstance()
        if (isAddressRequested) locationProvider.requestAddress(this) else locationProvider.requestLocation(
            this
        )
    }

    override fun onNoLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constants.PERMISSION_REQUEST_READ_LOCATION
        )
    }

    override fun onLocationChanged(location: Location?) {

    }

    override fun onAddressChanged(address: Address) {}
    override fun onGpsOff(exception: ResolvableApiException) {
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            exception.startResolutionForResult(
                this,
                Constants.REQUEST_CHECK_SETTINGS
            )
        } catch (e: SendIntentException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            // Ignore the error.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.REQUEST_CHECK_SETTINGS -> if (resultCode == RESULT_OK) {
                requestLocationProvider()
            } else finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == Constants.PERMISSION_REQUEST_READ_LOCATION) { // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationProvider()
            } else {
                if (shouldForce) {
                    startAppSettings()
                    finish()
                }
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun startAppSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

     open fun withdraw() {
        UiUtil.showProgressDialog(this, getString(R.string.please_wait))
        val withdrawalRequest = WithdrawalRequest()
        withdrawalRequest.setAmount("10")
        withdrawalRequest.setVendorCode("180204")
        DataFetcher.withdraw(
            this, Gson().toJson(withdrawalRequest), onWithdrawalSuccessListener,
            AddResponse::class.java, onErrorListener
        )
    }

    private val onWithdrawalSuccessListener =
        Response.Listener { response: AddResponse? ->
            UiUtil.cancelProgressDialog()
            var errorMessage: String? = getString(R.string.err_occurred)
            if (response != null && !TextUtils.isEmpty(response.returnMessage)) errorMessage =
                response.returnMessage
            if (response != null && response.returnMessage == Constants.SUCCESS) {
                moneyWithdrawn()
            } else UiUtil.showToast(this, errorMessage!!)
        }

    open fun moneyWithdrawn() {}

    companion object {
        const val TAG = "BaseActivity"
    }
}
