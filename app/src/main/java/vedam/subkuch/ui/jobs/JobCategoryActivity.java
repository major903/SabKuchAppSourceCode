package vedam.subkuch.ui.jobs;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class JobCategoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.jobs);
        addFragment(R.id.content_frame, JobCategoryFragment.newInstance());
//        handleIntent(getIntent());
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
