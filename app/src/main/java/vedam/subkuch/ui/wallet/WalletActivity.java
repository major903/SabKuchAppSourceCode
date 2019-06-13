package vedam.subkuch.ui.wallet;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityWalletBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.wallet.ProfileData;
import vedam.subkuch.network.models.wallet.TermsCondition;
import vedam.subkuch.network.models.wallet.Wallet;
import vedam.subkuch.network.models.wallet.WalletDetails;
import vedam.subkuch.network.models.wallet.WalletResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class WalletActivity extends BaseActivity {

    private ActivityWalletBinding activityWalletBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWalletBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        setTitle(R.string.my_wallet);
        setToolbarBackButton();
        getWalletDetails();
    }

    private void getWalletDetails() {

        UiUtil.showProgressDialog(this, getString(R.string.loading));
        DataFetcher.getWalletDetails(this, onWalletSuccessListener, WalletResponse.class, onErrorListener);
    }

    private Response.Listener<WalletResponse> onWalletSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            if (response.getReturnData() != null) {
                bindData(response.getReturnData());
                hideViews(response.getReturnData());
            } else {
                UiUtil.showToast(this, getString(R.string.no_data));
                activityWalletBinding.llContainer.setVisibility(View.INVISIBLE);
            }
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
            activityWalletBinding.llContainer.setVisibility(View.INVISIBLE);
        }
    };

    private void bindData(Wallet data) {

        WalletDetails walletDetails = data.getWallet();
        ProfileData profileData = data.getProfileData();
        TermsCondition termsCondition = data.getTermsConditions();
        activityWalletBinding.llContainer.setVisibility(View.VISIBLE);
        UiUtil.setTextViewWithBoldPrefix(this, "Member Name : ",
                AppUtil.getFullName(profileData.getFirstName(), profileData.getLastName()), activityWalletBinding.tvName);
        UiUtil.setTextViewWithBoldPrefix(this, "Phone No. : ", profileData.getMobile(), activityWalletBinding.tvMobile);
        UiUtil.setTextViewWithBoldPrefix(this, "Total Earnings : ", walletDetails.getTotalReferralIncome(), activityWalletBinding.tvTotalEarnings);
        UiUtil.setTextViewWithBoldPrefix(this, "Points Earned : ", walletDetails.getTotalPointsEarned(), activityWalletBinding.tvPointsEarned);
        UiUtil.setTextViewWithBoldPrefix(this, "Withdrawal : ", walletDetails.getTotalWithdrawal(), activityWalletBinding.tvWithdrawal);
        UiUtil.setTextViewWithBoldPrefix(this, "Available Amount : ", walletDetails.getAvailableBalance(), activityWalletBinding.tvAvailableAmount);
        UiUtil.setTextView(activityWalletBinding.tvTncTitle, termsCondition.getTitle());
        setTnc(termsCondition.getDescription());

    }

    private void setTnc(String description) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            activityWalletBinding.tvTnc.setText(Html.fromHtml(AppUtil.deNull(description), Html.FROM_HTML_MODE_LEGACY));
        else
            activityWalletBinding.tvTnc.setText(Html.fromHtml(AppUtil.deNull(description)));

        activityWalletBinding.tvTnc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void hideViews(Wallet data) {
        WalletDetails walletDetails = data.getWallet();
        ProfileData profileData = data.getProfileData();
        TermsCondition termsCondition = data.getTermsConditions();
        if (TextUtils.isEmpty(profileData.getFirstName()) && TextUtils.isEmpty(profileData.getMobile()))
            activityWalletBinding.cvName.setVisibility(View.GONE);
        else
            activityWalletBinding.cvName.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(walletDetails.getTotalPointsEarned()) && TextUtils.isEmpty(walletDetails.getTotalReferralIncome())
                && TextUtils.isEmpty(walletDetails.getTotalWithdrawal()) && TextUtils.isEmpty(walletDetails.getAvailableBalance()))
            activityWalletBinding.cvEarnings.setVisibility(View.GONE);
        else
            activityWalletBinding.cvEarnings.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(termsCondition.getDescription()))
            activityWalletBinding.cvTnc.setVisibility(View.GONE);
        else
            activityWalletBinding.cvTnc.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.withdraw, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_withdraw) {
            startActivityForResult(new Intent(this, WithdrawalActivity.class), Constants.REQUEST_WITHDRAW);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Constants.REQUEST_WITHDRAW:
                if (resultCode == Activity.RESULT_OK)
                    getWalletDetails();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
