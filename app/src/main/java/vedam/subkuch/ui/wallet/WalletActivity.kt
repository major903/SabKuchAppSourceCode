package vedam.subkuch.ui.wallet

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityWalletBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.getMyReferral
import vedam.subkuch.network.DataFetcher.getWalletDetails
import vedam.subkuch.network.models.referral.MyReferral
import vedam.subkuch.network.models.referral.MyReferralResponse
import vedam.subkuch.network.models.referral.ReferralDetails
import vedam.subkuch.network.models.wallet.Wallet
import vedam.subkuch.network.models.wallet.WalletResponse
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

class WalletActivity : BaseActivity() {
    private var activityWalletBinding: ActivityWalletBinding? = null
    private val requestStack = Stack<Any>()
    private var walletResponse: WalletResponse? = null
    private var myReferralResponse: MyReferralResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWalletBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        setTitle(R.string.my_wallet)
        setToolbarBackButton()
        startAPICalls()
    }

    private fun startAPICalls() {
        requestStack.add(Any())
        requestStack.add(Any())
        UiUtil.showProgressDialog(this, getString(R.string.loading))
        getWalletDetails()
        getMyReferrals()
    }

    private fun getWalletDetails() {
        getWalletDetails(
            this,
            onWalletSuccessListener,
            WalletResponse::class.java,
            onErrorListener
        )
    }

    private fun getMyReferrals() {
        getMyReferral(
            this,
            onMyReferralSuccessListener,
            MyReferralResponse::class.java,
            onErrorListener
        )
    }
    private val onWalletSuccessListener = Response.Listener { response: WalletResponse? ->
        if (!requestStack.isEmpty()) requestStack.pop()
        walletResponse = response
        checkFlagAndLoadUI()
    }
    private val onMyReferralSuccessListener = Response.Listener { response: MyReferralResponse? ->
        if (!requestStack.isEmpty()) requestStack.pop()
        myReferralResponse = response
        checkFlagAndLoadUI()
    }

    private fun checkFlagAndLoadUI() {
        if (requestStack.isEmpty()) {
            UiUtil.cancelProgressDialog()
            loadUI()
        }
    }

    private fun loadUI() {
        if (walletResponse != null && walletResponse!!.returnMessage == Constants.SUCCESS) {
            if (walletResponse!!.returnData != null) {
                bindData(walletResponse!!.returnData)
                hideViews(walletResponse!!.returnData)
            } else {
                UiUtil.showToast(this, getString(R.string.no_data))
                activityWalletBinding!!.llContainer.visibility = View.INVISIBLE
            }
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred))
            activityWalletBinding!!.llContainer.visibility = View.INVISIBLE
        }
    }

    private fun bindData(data: Wallet) {
        val walletDetails = data.wallet
        val profileData = data.profileData
        val termsCondition = data.termsConditions
        activityWalletBinding!!.llContainer.visibility = View.VISIBLE
        UiUtil.setTextViewWithBoldPrefix(
            this,
            "Member Name : ",
            AppUtil.getFullName(profileData.firstName, profileData.lastName),
            activityWalletBinding!!.tvName
        )
        UiUtil.setTextViewWithBoldPrefix(
            this,
            "Referral Code : ",
            profileData.refferalCode,
            activityWalletBinding!!.tvMobile
        )
        UiUtil.setTextViewWithBoldPrefix(
            this,
            "Total Earnings : ",
            walletDetails.totalReferralIncome,
            activityWalletBinding!!.tvTotalEarnings
        )
        //        UiUtil.setTextViewWithBoldPrefix(this, "Points Earned : ", walletDetails.getTotalPointsEarned(), activityWalletBinding.tvPointsEarned);
        activityWalletBinding!!.tvPointsEarned.visibility = View.GONE
        UiUtil.setTextViewWithBoldPrefix(
            this,
            "Withdrawal : ",
            walletDetails.totalWithdrawal,
            activityWalletBinding!!.tvWithdrawal
        )
        UiUtil.setTextViewWithBoldPrefix(
            this,
            "Available Amount : ",
            walletDetails.availableBalance,
            activityWalletBinding!!.tvAvailableAmount
        )
        UiUtil.setTextView(activityWalletBinding!!.tvTncTitle, termsCondition.title)
        setTnc(termsCondition.description)
        val isReferralDataAvailable = isReferralDataAvailable
        if (isReferralDataAvailable) {
            activityWalletBinding!!.rlSubContainer.visibility = View.VISIBLE
            activityWalletBinding!!.tvReferralHeading.visibility = View.VISIBLE
            setVenueListener(
                activityWalletBinding!!.tvReferee,
                activityWalletBinding!!.ivTriangle,
                activityWalletBinding!!.rlSubContainer,
                myReferralResponse!!.returnData
            )
            setVenue(
                activityWalletBinding!!.tvReferee,
                activityWalletBinding!!.ivTriangle,
                myReferralResponse!!.returnData
            )
        } else {
            activityWalletBinding!!.rlSubContainer.visibility = View.GONE
            activityWalletBinding!!.tvReferralHeading.visibility = View.GONE
        }
    }

    private val isReferralDataAvailable: Boolean
        private get() = myReferralResponse != null && myReferralResponse!!.returnData != null && myReferralResponse!!.returnData.referralDetails != null && !myReferralResponse!!.returnData.referralDetails.isEmpty()

    private fun setTnc(description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) activityWalletBinding!!.tvTnc.text =
            Html.fromHtml(
                AppUtil.deNull(description),
                Html.FROM_HTML_MODE_LEGACY
            ) else activityWalletBinding!!.tvTnc.text = Html.fromHtml(AppUtil.deNull(description))
        activityWalletBinding!!.tvTnc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setVenueListener(
        tvVenue: TextView,
        ivTriangle: ImageView,
        rlSubContainer: RelativeLayout,
        referralDetails: ReferralDetails
    ) {
        if (referralDetails.referralDetails.size > 2) {
            ivTriangle.visibility = View.VISIBLE
            rlSubContainer.setOnClickListener { v: View? ->
                if (referralDetails.isExpanded) {
                    referralDetails.isExpanded = false
                    setVenue(tvVenue, ivTriangle, referralDetails)
                } else {
                    referralDetails.isExpanded = true
                    setVenue(tvVenue, ivTriangle, referralDetails)
                }
            }
        } else {
            ivTriangle.visibility = View.GONE
            rlSubContainer.setOnClickListener(null)
        }
    }

    private fun setVenue(
        tvName: TextView,
        ivTriangle: ImageView,
        referralDetails: ReferralDetails
    ) {
        if (referralDetails.referralDetails.size > 2) if (referralDetails.isExpanded) {
            tvName.text = getFullNamesString(referralDetails.referralDetails)
            ivTriangle.setImageResource(R.drawable.baseline_expand_less_black_24dp)
        } else {
            ivTriangle.setImageResource(R.drawable.baseline_expand_more_black_24dp)
            if (referralDetails.referralDetails.size > 0) {
                val myReferral = referralDetails.referralDetails[0]
                tvName.text = AppUtil.getFullName(myReferral.firstName, myReferral.lastName)
            } else tvName.text = ""
        } else {
            tvName.text = getFullNamesString(referralDetails.referralDetails)
        }
    }

    private fun getFullNamesString(myReferrals: ArrayList<MyReferral>): String {
        val fullNames = StringBuilder()
        for (i in myReferrals.indices) {
            val myReferral = myReferrals[i]
            if (i == myReferrals.size - 1) fullNames.append(
                AppUtil.getFullName(
                    myReferral.firstName,
                    myReferral.lastName
                )
            ) else fullNames.append(AppUtil.getFullName(myReferral.firstName, myReferral.lastName))
                .append("\n")
        }
        return fullNames.toString()
    }

    private fun hideViews(data: Wallet) {
        val walletDetails = data.wallet
        val profileData = data.profileData
        val termsCondition = data.termsConditions
        val isReferralDataAvailable = isReferralDataAvailable
        if (TextUtils.isEmpty(profileData.firstName) && TextUtils.isEmpty(profileData.mobile) && !isReferralDataAvailable) activityWalletBinding!!.cvName.visibility =
            View.GONE else activityWalletBinding!!.cvName.visibility = View.VISIBLE
        if (TextUtils.isEmpty(walletDetails.totalPointsEarned) && TextUtils.isEmpty(walletDetails.totalReferralIncome)
            && TextUtils.isEmpty(walletDetails.totalWithdrawal) && TextUtils.isEmpty(walletDetails.availableBalance)
        ) activityWalletBinding!!.cvEarnings.visibility =
            View.GONE else activityWalletBinding!!.cvEarnings.visibility = View.VISIBLE
        if (TextUtils.isEmpty(termsCondition.description)) activityWalletBinding!!.cvTnc.visibility =
            View.GONE else activityWalletBinding!!.cvTnc.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.withdraw, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_withdraw) {
            startActivityForResult(
                Intent(this, WithdrawalActivity::class.java),
                Constants.REQUEST_WITHDRAW
            )
            true
        } else super.onOptionsItemSelected(item)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.REQUEST_WITHDRAW -> if (resultCode == RESULT_OK) getWalletDetails()
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}