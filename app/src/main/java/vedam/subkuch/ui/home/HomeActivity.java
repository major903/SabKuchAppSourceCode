package vedam.subkuch.ui.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityHomeBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.Feature;
import vedam.subkuch.ui.ask.AskActivity;
import vedam.subkuch.ui.directory.DirectoryActivity;
import vedam.subkuch.ui.events.EventActivity;
import vedam.subkuch.ui.jobs.JobCategoryActivity;
import vedam.subkuch.ui.movies.MoviesActivity;
import vedam.subkuch.ui.offers.OffersActivity;
import vedam.subkuch.ui.phonebook.PhoneBookActivity;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomeBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_home);
        getFeatures();
    }

    private void getFeatures() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        Type type = new TypeToken<ArrayList<Feature>>() {
        }.getType();
        DataFetcher.getFeatures(this, onFeaturesSuccessListener, type, onErrorListener,
                AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, null));
    }

    private Response.Listener<ArrayList<Feature>> onFeaturesSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null) {
            enableFeatures(response);
            activityHomeBinding.contentLinearLayout.setVisibility(View.VISIBLE);
        } else
            UiUtil.showToast(HomeActivity.this, getString(R.string.err_occurred));
    };

    private void enableFeatures(ArrayList<Feature> response) {

        for (Feature feature : response) {
            switch (feature.getName()) {
                case Constants.Directory:
                    activityHomeBinding.ivDirectory.setVisibility(View.VISIBLE);
                    break;
                case Constants.Events:
                    activityHomeBinding.ivEvents.setVisibility(View.VISIBLE);
                    break;
                case Constants.Jobs:
                    activityHomeBinding.ivJobs.setVisibility(View.VISIBLE);
                    break;
                case Constants.Movies:
                    activityHomeBinding.ivMovies.setVisibility(View.VISIBLE);
                    break;
                case Constants.Transport:
                    activityHomeBinding.ivBus.setVisibility(View.VISIBLE);
                    break;
                case Constants.Phone_book:
                    activityHomeBinding.ivPhoneBook.setVisibility(View.VISIBLE);
                    break;
                case Constants.Dating:
                    activityHomeBinding.ivDating.setVisibility(View.VISIBLE);
                    break;
                case Constants.Ask_Me:
                    activityHomeBinding.ivAskMe.setVisibility(View.VISIBLE);
                    break;
                case Constants.Gift_A_Life:
                    activityHomeBinding.ivGift.setVisibility(View.VISIBLE);
                    break;
                case Constants.Offers:
                    activityHomeBinding.ivOffer.setVisibility(View.VISIBLE);
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
//        startActivity(new Intent(this, BusActivity.class));
    }

    public void phoneBookClick(View v) {
        startActivity(new Intent(this, PhoneBookActivity.class));
    }

    public void datingClick(View v) {
//        startActivity(new Intent(this, PhoneBookActivity.class));
    }

    public void askClick(View v) {
        startActivity(new Intent(this, AskActivity.class));
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

    public void giftALifeClick(View v) {
//        startActivity(new Intent(this, JobCategoryActivity.class));
    }

    /*public void offlinemapClick(View v)
    {
        startActivity(new Intent(this,OfflineMap.class));
    }*/

    /*public void WheretoeatClick(View v)
    {
        startActivity(new Intent(this,Wheretoeat.class));
    }

    public void WheretoshopClick(View v)
    {
        startActivity(new Intent(this,Wheretoshop.class));
    }*/

    /*public void InfoClick(View v)
    {
        startActivity(new Intent(this,Info.class));
    }*/
}
