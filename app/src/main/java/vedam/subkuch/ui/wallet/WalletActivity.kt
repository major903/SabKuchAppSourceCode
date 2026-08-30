package vedam.subkuch.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import vedam.subkuch.network.Response
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityWalletBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher.getMyReferral
import vedam.subkuch.network.DataFetcher.getWalletDetails
import vedam.subkuch.network.DataFetcher.getWalletTerms
import vedam.subkuch.network.models.referral.MyReferral
import vedam.subkuch.network.models.referral.MyReferralResponse
import vedam.subkuch.network.models.wallet.Balance
import vedam.subkuch.network.models.wallet.BalanceResponse
import vedam.subkuch.network.models.wallet.WalletResponse
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.UiUtil
import java.util.*

class WalletActivity : BaseActivity() {
    private var activityWalletBinding: ActivityWalletBinding? = null
    private val requestStack = Stack<Any>()
    private var balanceResponse: BalanceResponse? = null
    private var myReferralResponse: MyReferralResponse? = null
    private var walletTermsResponse: WalletResponse? = null
    private var areReferralsExpanded = false
    private val referralsAdapter = ReferralsAdapter()

    private val withdrawalLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) getWalletDetails()
        }
    private val transferLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) getWalletDetails()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWalletBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        setTitle(R.string.my_wallet)
        setToolbarBackButton()
        activityWalletBinding!!.btnTransfer.setOnClickListener { openTransfer() }
        activityWalletBinding!!.btnWithdraw.setOnClickListener { openWithdrawal() }
        activityWalletBinding!!.rvReferrals.apply {
            layoutManager = LinearLayoutManager(this@WalletActivity)
            adapter = referralsAdapter
            isNestedScrollingEnabled = false
            itemAnimator = null
        }
        startAPICalls()
    }

    private fun startAPICalls() {
        requestStack.add(Any())
        requestStack.add(Any())
        UiUtil.showProgressDialog(this, getString(R.string.loading))
        getWalletDetails()
        getMyReferrals()
        getWalletTerms()
    }

    private fun getWalletDetails() {
        getWalletDetails(
            this,
            onBalanceSuccessListener,
            BalanceResponse::class.java,
            onInitialLoadErrorListener
        )
    }

    private fun getMyReferrals() {
        getMyReferral(
            this,
            onMyReferralSuccessListener,
            MyReferralResponse::class.java,
            onInitialLoadErrorListener
        )
    }

    private fun getWalletTerms() {
        getWalletTerms(
            this,
            onWalletTermsSuccessListener,
            WalletResponse::class.java,
            Response.ErrorListener {
                walletTermsResponse = null
                bindTerms()
            }
        )
    }

    private val onBalanceSuccessListener = Response.Listener { response: BalanceResponse? ->
        if (!requestStack.isEmpty()) requestStack.pop()
        balanceResponse = response
        checkFlagAndLoadUI()
    }

    private val onMyReferralSuccessListener = Response.Listener { response: MyReferralResponse? ->
        if (!requestStack.isEmpty()) requestStack.pop()
        myReferralResponse = response
        // A fresh response must start on the first page. Otherwise a refresh after the
        // list has been expanded can leave the pagination control in the wrong state.
        areReferralsExpanded = false
        checkFlagAndLoadUI()
    }

    private val onWalletTermsSuccessListener = Response.Listener { response: WalletResponse? ->
        walletTermsResponse = response
        bindTerms()
    }

    private val onInitialLoadErrorListener = Response.ErrorListener { error ->
        if (!requestStack.isEmpty()) requestStack.pop()
        onErrorReceived(error)
        checkFlagAndLoadUI()
    }

    private fun checkFlagAndLoadUI() {
        if (requestStack.isEmpty()) {
            UiUtil.cancelProgressDialog()
            loadUI()
        }
    }

    private fun loadUI() {
        if (balanceResponse != null && balanceResponse!!.returnMessage == Constants.SUCCESS) {
            if (balanceResponse!!.returnData != null) {
                bindData(balanceResponse!!.returnData)
            } else {
                UiUtil.showToast(this, getString(R.string.no_data))
                activityWalletBinding!!.llContainer.visibility = View.INVISIBLE
            }
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred))
            activityWalletBinding!!.llContainer.visibility = View.INVISIBLE
        }
    }

    private fun bindData(data: Balance) {
        activityWalletBinding!!.llContainer.visibility = View.VISIBLE
        activityWalletBinding!!.tvTotalEarnings.text = formatCoinValue(data.totalCoinBalance)
        activityWalletBinding!!.tvPointsEarned.visibility = View.GONE
        activityWalletBinding!!.tvWithdrawal.text = formatCoinValue(data.alreadyWithdrawnAmount)
        activityWalletBinding!!.tvAvailableAmount.text =
            formatCoinValue(data.remainingWithdrawableAmount)
        bindReferrals()
        bindTerms()
        activityWalletBinding!!.cvEarnings.visibility = View.VISIBLE
    }

    private fun bindTerms() {
        val binding = activityWalletBinding ?: return
        val terms = walletTermsResponse?.returnData?.termsConditions
        val description = terms?.description?.takeIf { it.isNotBlank() }
        if (description == null) {
            binding.cvTnc.visibility = View.GONE
            return
        }

        binding.tvTncTitle.text = terms.title?.takeIf { it.isNotBlank() }
            ?: getString(R.string.terms_conditions)
        binding.tvTnc.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.cvTnc.visibility = View.VISIBLE
    }

    private fun formatCoinValue(value: CharSequence?): String {
        val rawAmount = AppUtil.deNull(value).trim()
        if (rawAmount.isEmpty()) return "0"
        return rawAmount.replaceFirst(
            Regex("^(Vedam Coins|Rs\\.?|NPR|INR|₹|\\$)\\s*", RegexOption.IGNORE_CASE),
            ""
        ).trim()
            // Coin amounts are whole numbers — print "0", not "0.0".
            .replaceFirst(Regex("\\.0+$"), "")
    }

    private fun bindReferrals() {
        val binding = activityWalletBinding ?: return
        val referrals = myReferralResponse
            ?.takeIf { it.returnMessage == Constants.SUCCESS }
            ?.returnData
            ?.referralDetails

        if (referrals == null) {
            referralsAdapter.submitList(emptyList())
            binding.cvName.visibility = View.GONE
            return
        }

        binding.cvName.visibility = View.VISIBLE
        binding.tvMemberNameLabel.visibility = View.GONE
        binding.tvName.visibility = View.GONE
        binding.tvReferralCodeLabel.visibility = View.GONE
        binding.tvMobile.visibility = View.GONE
        binding.referralDivider.visibility = View.GONE
        binding.tvReferralHeading.visibility = View.GONE
        binding.rlSubContainer.visibility = View.VISIBLE
        binding.rlSubContainer.setOnClickListener(null)
        binding.llReferralToggle.setOnClickListener(null)

        if (referrals.isEmpty()) {
            referralsAdapter.submitList(emptyList())
            binding.rvReferrals.visibility = View.GONE
            binding.tvNoReferrals.visibility = View.VISIBLE
            binding.llReferralToggle.visibility = View.GONE
            return
        }

        binding.tvNoReferrals.visibility = View.GONE
        binding.rvReferrals.visibility = View.VISIBLE
        renderReferralNames(referrals)
    }

    private fun renderReferralNames(referrals: List<MyReferral>) {
        val binding = activityWalletBinding ?: return
        val canExpand = referrals.size > MAX_COLLAPSED_REFERRALS
        val visibleReferrals =
            if (canExpand && !areReferralsExpanded) referrals.take(MAX_COLLAPSED_REFERRALS) else referrals
        referralsAdapter.submitList(visibleReferrals)

        if (!canExpand) {
            binding.llReferralToggle.visibility = View.GONE
            binding.llReferralToggle.setOnClickListener(null)
            return
        }

        binding.llReferralToggle.visibility = View.VISIBLE
        binding.ivTriangle.setImageResource(
            if (areReferralsExpanded) {
                R.drawable.baseline_expand_less_black_24dp
            } else {
                R.drawable.baseline_expand_more_black_24dp
            }
        )
        binding.ivTriangle.contentDescription = getString(
            if (areReferralsExpanded) R.string.collapse_referrals else R.string.expand_referrals
        )
        binding.tvReferralToggle.text = if (areReferralsExpanded) {
            getString(R.string.show_fewer_referrals)
        } else {
            getString(R.string.show_all_referrals, referrals.size)
        }
        // Keep pagination on its explicit control. RecyclerView consumes its own touch
        // events, so putting this listener on the surrounding container makes expanding
        // unreliable when the list is tapped.
        binding.llReferralToggle.setOnClickListener {
            areReferralsExpanded = !areReferralsExpanded
            renderReferralNames(referrals)
        }
    }

    private fun openWithdrawal() {
        val intent = Intent(this, WithdrawalActivity::class.java).apply {
            putExtra(
                WithdrawalActivity.EXTRA_AVAILABLE_BALANCE,
                activityWalletBinding!!.tvAvailableAmount.text.toString()
            )
        }
        withdrawalLauncher.launch(intent)
    }

    private fun openTransfer() {
        val intent = Intent(this, TransferActivity::class.java).apply {
            putExtra(
                TransferActivity.EXTRA_AVAILABLE_BALANCE,
                activityWalletBinding!!.tvAvailableAmount.text.toString()
            )
        }
        transferLauncher.launch(intent)
    }

    private companion object {
        const val MAX_COLLAPSED_REFERRALS = 4
    }
}
