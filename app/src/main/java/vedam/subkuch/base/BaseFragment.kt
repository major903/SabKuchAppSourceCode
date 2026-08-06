package vedam.subkuch.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import vedam.subkuch.network.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import vedam.subkuch.R
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnFragmentInteractionListener
import vedam.subkuch.interfaces.ScreenChangeListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.NetworkConstants
import vedam.subkuch.network.models.ErrorResponse
import vedam.subkuch.network.models.Image
import vedam.subkuch.network.models.WithdrawalRequest
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.matrimonial.preference.ItemAdapter
import vedam.subkuch.uicomponent.SlideShowDialogFragment
import vedam.subkuch.utils.LogUtils
import vedam.subkuch.utils.UiUtil

/**
 * Created by nansari on 6/17/2016.
 */
abstract class BaseFragment : Fragment(), OnRefreshListener {
    @JvmField
    var mContext: Context? = null
    var globalFragmentInteractionListener: OnFragmentInteractionListener? = null
        private set
    var screenChangeListener: ScreenChangeListener? = null
        private set
    private val swipeRefreshLayout: SwipeRefreshLayout? = null

    @JvmField
    protected var mPopupWindow: PopupWindow? = null

    @JvmField
    protected var onErrorListener = Response.ErrorListener { error: ApiError ->
        LogUtils.LOGD("ERROR", error.message)
        if (activity != null) onErrorReceived(error)
    }

    protected open fun onErrorReceived(error: ApiError) {
        if (error is NetworkError) {
            UiUtil.showToast(mContext, getString(R.string.connectionError))
        } else if (error is TimeoutError) {
            UiUtil.showToast(mContext, getString(R.string.timeoutError))
        } else if (error is ParseError) {
            UiUtil.showToast(mContext, getString(R.string.err_parsing))
        } else if (error is AuthFailureError || error.networkResponse != null &&
            error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED
        ) {
            logout()
        } else {
            parseAndShowError(error)
        }
        UiUtil.cancelProgressDialog()
    }

    fun logout() {
        if (globalFragmentInteractionListener != null) globalFragmentInteractionListener!!.logout()
    }

    protected open fun parseAndShowError(error: ApiError) {
        val networkResponse = error.networkResponse
        if (networkResponse != null && networkResponse.data != null) {
            val response = String(networkResponse.data)
            LogUtils.LOGE(BaseActivity.TAG, "response error:$response")
            try {
                val errorResponse = Gson().fromJson(response, ErrorResponse::class.java)
                if (!TextUtils.isEmpty(errorResponse.returnMessage)) UiUtil.showToast(
                    mContext,
                    errorResponse.returnMessage
                ) else if (!TextUtils.isEmpty(errorResponse.message)) UiUtil.showToast(
                    mContext,
                    errorResponse.message
                ) else UiUtil.showToast(mContext, getString(R.string.err_occurred))
            } catch (exception: Exception) {
                FirebaseCrashlytics.getInstance().recordException(exception)
                exception.printStackTrace()
                UiUtil.showToast(mContext, getString(R.string.err_occurred))
            }
        } else {
            UiUtil.showToast(mContext, getString(R.string.err_unknown))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeViewIfPresent(view)
    }

    /**
     * The id should always be swipe_refresh
     *
     * @param view View inflated
     */
    private fun initSwipeViewIfPresent(view: View) {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//        if (swipeRefreshLayout != null)
//            swipeRefreshLayout.setOnRefreshListener(this);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        screenChangeListener =
            if (context is ScreenChangeListener) context else throw RuntimeException(
                context.toString()
                        + " must implement ScreenChangeListener"
            )
        if (context is OnFragmentInteractionListener) {
            globalFragmentInteractionListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        globalFragmentInteractionListener = null
        screenChangeListener = null
        mContext = null
    }

    protected fun addFragment(
        containerId: Int, fragment: Fragment?, tag: String?, addToBackStack: Boolean,
        @AnimRes enterAnim: Int, @AnimRes exitAnim: Int,
        @AnimRes enterAnimPop: Int, @AnimRes exitAnimPop: Int
    ) {
        if (mContext is BaseActivity) {
            (mContext as BaseActivity).addFragment(
                containerId, fragment, tag, addToBackStack,
                enterAnim, exitAnim, enterAnimPop, exitAnimPop
            )
        }
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

    protected fun replaceFragment(
        containerId: Int, fragment: Fragment?, tag: String?, addToBackStack: Boolean,
        @AnimRes enterAnim: Int, @AnimRes exitAnim: Int,
        @AnimRes enterAnimPop: Int, @AnimRes exitAnimPop: Int
    ) {
        if (mContext is BaseActivity) {
            (mContext as BaseActivity).replaceFragment(
                containerId, fragment, tag, addToBackStack,
                enterAnim, exitAnim, enterAnimPop, exitAnimPop
            )
        }
    }

    /**
     * Call this method to set title of toolbar in the parent activity from fragment
     *
     * @param title Title to be set
     */
    protected fun setTitle(title: String?) {
        if (mContext is BaseActivity) (mContext as BaseActivity).title = title!!
    }

    override fun getContext(): Context? {
        return mContext
    }

    override fun onRefresh() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.isRefreshing = false
    }

    fun onBackPressed() {
        if (mContext is BaseActivity) (mContext as BaseActivity).navigateBack()
    }

    /**
     * requests parent activity to get [Address] corresponding to current location
     */
    protected fun getAddress(shouldForce: Boolean) {
        if (globalFragmentInteractionListener != null) {
            globalFragmentInteractionListener!!.requestAddress(shouldForce)
        }
    }

    //START_GALLERY
    protected fun setGallery(alImage: ArrayList<Image?>?, selectedPosition: Int, isUrls: Boolean) {
        val bundle = Bundle()
        bundle.putSerializable(Constants.EXTRA_IMAGE_ITEMS, alImage)
        bundle.putInt(Constants.EXTRA_POSITION, selectedPosition)
        bundle.putBoolean(Constants.EXTRA_IS_IMAGE_URLS, isUrls)
        val ft = fragmentManager!!.beginTransaction()
        val newFragment = SlideShowDialogFragment.newInstance(bundle)
        newFragment.show(ft, "slideshow")
    }

    fun baseshowFeedbackMessage(view: View?, message: String?) {
        try {
            val snakbar = Snackbar.make(
                view!!, message!!, Snackbar.LENGTH_LONG
            )
            val tv = snakbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            snakbar.view.setBackgroundColor(
                ContextCompat.getColor(
                    activity!!,
                    android.R.color.white
                )
            )
            if (snakbar.isShown) {
                snakbar.dismiss()
            }
            snakbar.show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun showPopWindow(view: View, adapter: ItemAdapter?) {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val customView = LayoutInflater.from(activity).inflate(R.layout.view_pop_window, null)
        val recylcerView = customView.findViewById<RecyclerView>(R.id.recyclerView)
        recylcerView.layoutManager = LinearLayoutManager(activity)
        recylcerView.adapter = adapter
        mPopupWindow = PopupWindow(customView, view.width, WindowManager.LayoutParams.WRAP_CONTENT)
        mPopupWindow!!.isOutsideTouchable = true
        mPopupWindow!!.setBackgroundDrawable(BitmapDrawable())
        mPopupWindow!!.showAsDropDown(view, 0, 10)
    }

    fun checkPermission(permission: Array<String?>): Int {
        var permissionNeeded = 0
        if (Build.VERSION.SDK_INT >= 23) {
            for (i in permission.indices) {
                val result = ContextCompat.checkSelfPermission(activity!!, permission[i]!!)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionNeeded++
                }
            }
        }
        return permissionNeeded
    }

    protected fun requestLocation() {
        if (globalFragmentInteractionListener != null) globalFragmentInteractionListener!!.requestLocation(
            false
        )
    }

    open fun withdraw() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        val withdrawalRequest = WithdrawalRequest()
        withdrawalRequest.setAmount("10")
        withdrawalRequest.setVendorCode("180204")
        DataFetcher.withdraw(
            mContext, Gson().toJson(withdrawalRequest), onWithdrawalSuccessListener,
            AddResponse::class.java, onErrorListener
        )
    }

    private val onWithdrawalSuccessListener =
        Response.Listener { response: AddResponse? ->
            UiUtil.cancelProgressDialog()
            var errorMessage: String? = getString(R.string.err_occurred)
            if (activity == null)
                return@Listener
            if (response != null && !TextUtils.isEmpty(response.returnMessage)) errorMessage =
                response.returnMessage
            if (response != null && response.returnMessage == Constants.SUCCESS) {
                moneyWithdrawn()
            } else UiUtil.showToast(mContext, errorMessage!!)
        }

    open fun moneyWithdrawn() {}
}
