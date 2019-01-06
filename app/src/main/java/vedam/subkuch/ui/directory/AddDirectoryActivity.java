package vedam.subkuch.ui.directory;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class AddDirectoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setToolbarBackButton();
        setTitle(R.string.add_business);

        addFragment(R.id.content_frame, AddDirectoryFragment.newInstance());

    }
}

