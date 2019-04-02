package vedam.subkuch.ui.wallet;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityWalletBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Wallet;
import vedam.subkuch.network.models.WalletResponse;
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
        if (response != null && response.isSuccess()) {
            if (response.getData() != null) {
                bindData(response.getData());
                hideViews(response.getData());
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

        activityWalletBinding.llContainer.setVisibility(View.VISIBLE);
        UiUtil.setTextViewWithBoldPrefix(this, "Member Name : ", data.getUserName(), activityWalletBinding.tvName);
        UiUtil.setTextViewWithBoldPrefix(this, "Profile Id : ", data.getUserId(), activityWalletBinding.tvId);
        UiUtil.setTextViewWithBoldPrefix(this, "Phone No. : ", data.getPhoneNumber(), activityWalletBinding.tvMobile);
        UiUtil.setTextViewWithBoldPrefix(this, "Total Earnings : ", data.getTotalEarnings(), activityWalletBinding.tvTotalEarnings);
        UiUtil.setTextViewWithBoldPrefix(this, "Points Earned : ", data.getPoints(), activityWalletBinding.tvPointsEarned);
        UiUtil.setTextViewWithBoldPrefix(this, "Withdrawal : ", data.getWithdrawls(), activityWalletBinding.tvWithdrawal);
        UiUtil.setTextViewWithBoldPrefix(this, "Available Amount : ", data.getBalance(), activityWalletBinding.tvAvailableAmount);

    }

    private void hideViews(Wallet data) {
        if (TextUtils.isEmpty(data.getUserName()))
            activityWalletBinding.cvName.setVisibility(View.GONE);
        else
            activityWalletBinding.cvName.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(data.getUserId()) && TextUtils.isEmpty(data.getPhoneNumber()))
            activityWalletBinding.cvId.setVisibility(View.GONE);
        else
            activityWalletBinding.cvId.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(data.getTotalEarnings()) && TextUtils.isEmpty(data.getPoints())
                && TextUtils.isEmpty(data.getWithdrawls()) && TextUtils.isEmpty(data.getBalance()))
            activityWalletBinding.cvEarnings.setVisibility(View.GONE);
        else
            activityWalletBinding.cvEarnings.setVisibility(View.VISIBLE);

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
