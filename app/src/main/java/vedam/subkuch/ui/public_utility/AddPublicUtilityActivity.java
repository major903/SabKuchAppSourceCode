package vedam.subkuch.ui.public_utility;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class AddPublicUtilityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setToolbarBackButton();
        setTitle(R.string.add_public_utility);

        addFragment(R.id.content_frame, AddPublicUtilityFragment.newInstance());

    }
}

