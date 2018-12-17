package vedam.subkuch.ui.directory;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;

public class DirectoryDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        String subCatName = getIntent().getStringExtra(Constants.EXTRA_SUB_CATEGORY_NAME);

        setToolbarBackButton();
        setTitle(subCatName);

        Bundle extras = getIntent().getExtras();

        addFragment(R.id.content_frame, DirectoryDetailsFragment.newInstance(extras));
    }
}
