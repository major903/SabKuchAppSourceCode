package vedam.subkuch.ui.jobs;

import android.location.Location;
import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;

public class JobCategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.jobs);
        requestLocation(false);
        addFragment(R.id.content_frame, JobCategoryFragment.newInstance());
//        handleIntent(getIntent());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;
        DataFetcher.updateLocation(this, null, AddEventResponse.class, null, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude()));
    }
//    @Override
//    protected void onNewIntent(Intent intent) {
//        handleIntent(intent);
//    }

    /*private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof JobsFragment && !TextUtils.isEmpty(query)) {
//                ((JobsFragment) fragment).getJobs(query);
            }

            //use the query to search your data somehow
        } else {
            addFragment(R.id.content_frame, JobCategoryFragment.newInstance());
        }
    }*/
}
