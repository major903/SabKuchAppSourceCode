package vedam.subkuch.ui.jobs.jobmela;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class JobMelaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.job_mela);

        addFragment(R.id.content_frame, JobMelaFragment.newInstance());
    }
}
