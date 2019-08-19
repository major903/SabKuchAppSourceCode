package vedam.subkuch.ui.home;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.Response;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityHomeBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;
import vedam.subkuch.network.models.Feature;
import vedam.subkuch.network.models.ReferralRequest;
import vedam.subkuch.network.models.ShareResponse;
import vedam.subkuch.ui.ask.AskCategoryActivity;
import vedam.subkuch.ui.directory.DirectoryActivity;
import vedam.subkuch.ui.events.EventActivity;
import vedam.subkuch.ui.inbox.InboxActivity;
import vedam.subkuch.ui.jobs.JobCategoryActivity;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.ui.movies.MoviesActivity;
import vedam.subkuch.ui.needs.NeedsActivity;
import vedam.subkuch.ui.offers.OffersActivity;
import vedam.subkuch.ui.phonebook.PhoneBookActivity;
import vedam.subkuch.ui.pin.PinActivity;
import vedam.subkuch.ui.profile.EditProfileActivity;
import vedam.subkuch.ui.public_utility.PublicUtilityActivity;
import vedam.subkuch.ui.transport.TransportActivity;
import vedam.subkuch.ui.vehicle.VehicleActivity;
import vedam.subkuch.ui.wallet.WalletActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, InstallReferrerStateListener {

    private ActivityHomeBinding activityHomeBinding;
    private InstallReferrerClient referrerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomeBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_home);
        requestLocation(false);
        getFeatures();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityHomeBinding.drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityHomeBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        activityHomeBinding.navView.setNavigationItemSelectedListener(this);
        TextView tvName = activityHomeBinding.navView.getHeaderView(0).findViewById(R.id.tv_name);
        tvName.setText(AppPrefs.getPrefsUserName(this));

        handleReferral();
    }

    private void handleReferral() {

        String isReferralDone = AppPrefs.getPrefsIsReferralDone(this);
        if (TextUtils.isEmpty(isReferralDone))
            logout();
        else if (Constants.FALSE.equalsIgnoreCase(isReferralDone)) {
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        DataFetcher.updateLocation(this, onUpdateLocationSuccessListener, AddEventResponse.class, null, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude()));
    }

    private Response.Listener<AddEventResponse> onUpdateLocationSuccessListener =
            response -> LogUtils.LOGD("Update Location", response.toString());

    private void getFeatures() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        Type type = new TypeToken<ArrayList<Feature>>() {
        }.getType();
        DataFetcher.getFeatures(this, onFeaturesSuccessListener, type, onErrorListener);
    }

    private Response.Listener<ArrayList<Feature>> onFeaturesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null) {
            enableFeatures(response);
            activityHomeBinding.getRoot().findViewById(R.id.ll_container).setVisibility(View.VISIBLE);
        } else
            UiUtil.showToast(HomeActivity.this, getString(R.string.err_occurred));
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_share)
            getShareMessage();

        return super.onOptionsItemSelected(item);
    }

    private void getShareMessage() {
        UiUtil.showProgressDialog(this, R.string.loading);
        DataFetcher.getShareContent(this, onShareSuccessListener, ShareResponse.class, onErrorListener);

    }

    private Response.Listener<ShareResponse> onShareSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS) && response.getReturnData() != null) {
            handleAppShare(response.getReturnData());
        } else
            UiUtil.showToast(HomeActivity.this, getString(R.string.err_occurred));
    };

    private void handleAppShare(String data) {

        if (TextUtils.isEmpty(data)) {
            UiUtil.showToast(this, getString(R.string.no_share_content));
            return;
        }
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            i.putExtra(Intent.EXTRA_TEXT, data);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    private void enableFeatures(ArrayList<Feature> response) {

        for (Feature feature : response) {
            switch (feature.getName()) {
                case Constants.Directory:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_directory).setVisibility(View.VISIBLE);
                    break;
                case Constants.Events:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_events).setVisibility(View.VISIBLE);
                    break;
                case Constants.Jobs:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_jobs).setVisibility(View.VISIBLE);
                    break;
                case Constants.Movies:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_movies).setVisibility(View.VISIBLE);
                    break;
                case Constants.Public_Transport_Timings:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_bus).setVisibility(View.VISIBLE);
                    break;
                case Constants.Phone_book:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_phone_book).setVisibility(View.VISIBLE);
                    break;
                case Constants.Dating:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_dating).setVisibility(View.VISIBLE);
                    break;
                case Constants.Matrimonial:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_matrimonial).setVisibility(View.VISIBLE);
                    break;
                case Constants.Goods_Transport:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_transport).setVisibility(View.VISIBLE);
                    break;
                case Constants.Ask_Me:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_ask_me).setVisibility(View.VISIBLE);
                    break;
                case Constants.Gift_A_Life:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_gift).setVisibility(View.VISIBLE);
                    break;
                case Constants.Offers:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_offer).setVisibility(View.VISIBLE);
                    break;
                case Constants.Needs:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_needs).setVisibility(View.VISIBLE);
                    break;
                case Constants.Public_Utility:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_public_utility).setVisibility(View.VISIBLE);
                    break;
                case Constants.Classifieds:
                    activityHomeBinding.getRoot().findViewById(R.id.iv_classifieds).setVisibility(View.VISIBLE);
                    break;

            }
        }
    }

    /*public void newsClick(View v)
    {
        startActivity(new Intent(this,NewsActivity.class));
    }*/

    public void directoryClick(View v) {
        startActivity(new Intent(this, DirectoryActivity.class));
    }

    /*public void datingClick(View v) {
        startActivity(new Intent(this, DatingActivity.class));
    }*/

    /*public void classifiedsClick(View v) {
        startActivity(new Intent(this, ClassifiedsActivity.class));
    }
*/
    /*public void PropertiesClick(View v)
    {
        startActivity(new Intent(this,Properties.class));
    }*/

    public void specialOfferClick(View v) {

        startActivity(new Intent(this, OffersActivity.class));
    }

    /*public void eCommerceClick(View v)
    {
        startActivity(new Intent(this,Ecommerce.class));
    }*/

    public void busClick(View v) {
        startActivity(new Intent(this, VehicleActivity.class));
    }

    public void phoneBookClick(View v) {
        startActivity(new Intent(this, PhoneBookActivity.class));
    }

    public void datingClick(View v) {
        startActivity(new Intent(this, PinActivity.class).putExtra(Constants.EXTRA_IS_DATING, true));
    }

    public void matrimonialClick(View v) {
        startActivity(new Intent(this, PinActivity.class).putExtra(Constants.EXTRA_IS_DATING, false));
    }

    public void askClick(View v) {
        startActivity(new Intent(this, AskCategoryActivity.class));
    }

    public void eventsClick(View v) {
        startActivity(new Intent(this, EventActivity.class));
    }

    public void jobsClick(View v) {
        startActivity(new Intent(this, JobCategoryActivity.class));
    }

    public void moviesClick(View v) {
        startActivity(new Intent(this, MoviesActivity.class));
    }

    public void transportClick(View v) {
        startActivity(new Intent(this, TransportActivity.class));
    }

    public void giftALifeClick(View v) {
//        startActivity(new Intent(this, JobCategoryActivity.class));
    }

    public void needsClick(View v) {
        startActivity(new Intent(this, NeedsActivity.class));
    }

    public void publicUtilityClick(View v)
    {
        startActivity(new Intent(this, PublicUtilityActivity.class));
    }

    public void classifiedsClick(View v)
    {
//        startActivity(new Intent(this,Wheretoshop.class));
    }

    /*public void InfoClick(View v)
    {
        startActivity(new Intent(this,Info.class));
    }*/

    @Override
    public void onBackPressed() {

        if (activityHomeBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            startActivity(new Intent(this, EditProfileActivity.class));
        } else if (id == R.id.nav_wallet) {
            startActivity(new Intent(this, WalletActivity.class));
        } else if (id == R.id.nav_inbox) {
            startActivity(new Intent(this, InboxActivity.class));
        } else if (id == R.id.nav_privacy) {
            AppUtil.openUrl(this, Constants.PRIVACY_POLICY_URL);
        }

        activityHomeBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        switch (responseCode) {
            case InstallReferrerResponse.OK:
                ReferrerDetails response;
                try {
                    response = referrerClient.getInstallReferrer();
                    addReferralCode(response.getInstallReferrer());
                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
                // Connection established
                break;
            case InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                // API not available on the current Play Store app
                break;
            case InstallReferrerResponse.SERVICE_UNAVAILABLE:
                // Connection could not be established
                break;
            case InstallReferrerResponse.DEVELOPER_ERROR:
                break;
            case InstallReferrerResponse.SERVICE_DISCONNECTED:
                break;
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {

        if (referrerClient != null && !referrerClient.isReady())
            referrerClient.startConnection(this);
    }

    private void addReferralCode(String referrerCode) {

        if (!TextUtils.isEmpty(referrerCode) && !referrerCode.startsWith("utm")) {
            ReferralRequest referralRequest = new ReferralRequest();
            referralRequest.setProfileId(AppPrefs.getPrefsUserId(this));
            referralRequest.setReferredBy(referrerCode);
            DataFetcher.addReferral(this, new Gson().toJson(referralRequest), onAddReferralSuccessListener, AddResponse.class, onErrorListener);
        }
    }

    private Response.Listener<AddResponse> onAddReferralSuccessListener = response -> {

        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            AppPrefs.setPrefsIsReferralDone(this, Constants.TRUE);
            endConnection();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endConnection();
    }

    private void endConnection() {
        if (referrerClient != null && referrerClient.isReady())
            referrerClient.endConnection();
    }
}
