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
    }
}
