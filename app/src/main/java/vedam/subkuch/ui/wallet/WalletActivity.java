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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.Stack;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityWalletBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.referral.MyReferral;
import vedam.subkuch.network.models.referral.MyReferralResponse;
import vedam.subkuch.network.models.referral.ReferralDetails;
import vedam.subkuch.network.models.wallet.ProfileData;
import vedam.subkuch.network.models.wallet.TermsCondition;
import vedam.subkuch.network.models.wallet.Wallet;
import vedam.subkuch.network.models.wallet.WalletDetails;
import vedam.subkuch.network.models.wallet.WalletResponse;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

public class WalletActivity extends BaseActivity {

    private ActivityWalletBinding activityWalletBinding;
    private Stack<Object> requestStack = new Stack<>();
    private WalletResponse walletResponse;
    private MyReferralResponse myReferralResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWalletBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        setTitle(R.string.my_wallet);
        setToolbarBackButton();

        startAPICalls();
    }

    private void startAPICalls() {

        requestStack.add(new Object());
        requestStack.add(new Object());
        UiUtil.showProgressDialog(this, getString(R.string.loading));
        getWalletDetails();
        getMyReferrals();
    }

    private void getWalletDetails() {

        DataFetcher.getWalletDetails(this, onWalletSuccessListener, WalletResponse.class, onErrorListener);
    }

    private void getMyReferrals() {

        DataFetcher.getMyReferral(this, onMyReferralSuccessListener, MyReferralResponse.class, onErrorListener);
    }

    private Response.Listener<WalletResponse> onWalletSuccessListener = response -> {
        requestStack.pop();
        walletResponse = response;
        checkFlagAndLoadUI();
    };

    private Response.Listener<MyReferralResponse> onMyReferralSuccessListener = response -> {
        requestStack.pop();
        myReferralResponse = response;
        checkFlagAndLoadUI();
    };

    private void checkFlagAndLoadUI() {
        if (requestStack.isEmpty()) {
            UiUtil.cancelProgressDialog();
            loadUI();
        }
    }

    private void loadUI() {

        if (walletResponse != null && walletResponse.getReturnMessage().equals(Constants.SUCCESS)) {
            if (walletResponse.getReturnData() != null) {
                bindData(walletResponse.getReturnData());
                hideViews(walletResponse.getReturnData());
            } else {
                UiUtil.showToast(this, getString(R.string.no_data));
                activityWalletBinding.llContainer.setVisibility(View.INVISIBLE);
            }
        } else {
            UiUtil.showToast(this, getString(R.string.err_occurred));
            activityWalletBinding.llContainer.setVisibility(View.INVISIBLE);
        }
    }

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

        boolean isReferralDataAvailable = isReferralDataAvailable();

        if (isReferralDataAvailable) {
            activityWalletBinding.rlSubContainer.setVisibility(View.VISIBLE);
            activityWalletBinding.tvReferralHeading.setVisibility(View.VISIBLE);
            setVenueListener(activityWalletBinding.tvReferee, activityWalletBinding.ivTriangle, activityWalletBinding.rlSubContainer, myReferralResponse.getReturnData());
            setVenue(activityWalletBinding.tvReferee, activityWalletBinding.ivTriangle, myReferralResponse.getReturnData());
        } else {
            activityWalletBinding.rlSubContainer.setVisibility(View.GONE);
            activityWalletBinding.tvReferralHeading.setVisibility(View.GONE);
        }

    }

    private boolean isReferralDataAvailable() {
        return myReferralResponse != null && myReferralResponse.getReturnData() != null
                && myReferralResponse.getReturnData().getReferralDetails() != null
                && !myReferralResponse.getReturnData().getReferralDetails().isEmpty();
    }

    private void setTnc(String description) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            activityWalletBinding.tvTnc.setText(Html.fromHtml(AppUtil.deNull(description), Html.FROM_HTML_MODE_LEGACY));
        else
            activityWalletBinding.tvTnc.setText(Html.fromHtml(AppUtil.deNull(description)));

        activityWalletBinding.tvTnc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setVenueListener(TextView tvVenue, ImageView ivTriangle, RelativeLayout rlSubContainer, ReferralDetails referralDetails) {
        if (referralDetails.getReferralDetails().size() > 2) {
            ivTriangle.setVisibility(View.VISIBLE);
            rlSubContainer.setOnClickListener(v -> {
                if (referralDetails.isExpanded()) {
                    referralDetails.setExpanded(false);
                    setVenue(tvVenue, ivTriangle, referralDetails);
                } else {
                    referralDetails.setExpanded(true);
                    setVenue(tvVenue, ivTriangle, referralDetails);
                }
            });
        } else {
            ivTriangle.setVisibility(View.GONE);
            rlSubContainer.setOnClickListener(null);
        }
    }

    private void setVenue(TextView tvName, ImageView ivTriangle, ReferralDetails referralDetails) {

        if (referralDetails.getReferralDetails().size() > 2)
            if (referralDetails.isExpanded()) {
                tvName.setText(getFullNamesString(referralDetails.getReferralDetails()));
                ivTriangle.setImageResource(R.drawable.baseline_expand_less_black_24dp);
            } else {
                ivTriangle.setImageResource(R.drawable.baseline_expand_more_black_24dp);
                if (referralDetails.getReferralDetails().size() > 0) {
                    MyReferral myReferral = referralDetails.getReferralDetails().get(0);
                    tvName.setText(AppUtil.getFullName(myReferral.getFirstName(), myReferral.getLastName()));
                } else
                    tvName.setText("");
            }
        else {
            tvName.setText(getFullNamesString(referralDetails.getReferralDetails()));
        }
    }

    private String getFullNamesString(ArrayList<MyReferral> myReferrals) {
        StringBuilder fullNames = new StringBuilder();
        for (int i = 0; i < myReferrals.size(); i++) {
            MyReferral myReferral = myReferrals.get(i);
            if (i == myReferrals.size() - 1)
                fullNames.append(AppUtil.getFullName(myReferral.getFirstName(), myReferral.getLastName()));
            else
                fullNames.append(AppUtil.getFullName(myReferral.getFirstName(), myReferral.getLastName())).append("\n");
        }
        return fullNames.toString();
    }

    private void hideViews(Wallet data) {
        WalletDetails walletDetails = data.getWallet();
        ProfileData profileData = data.getProfileData();
        TermsCondition termsCondition = data.getTermsConditions();

        boolean isReferralDataAvailable = isReferralDataAvailable();

        if (TextUtils.isEmpty(profileData.getFirstName()) && TextUtils.isEmpty(profileData.getMobile()) && !isReferralDataAvailable)
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
