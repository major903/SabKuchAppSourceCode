package vedam.subkuch.ui.directory;

import android.os.Bundle;
import android.widget.TextView;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;

public class SubDirectoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();

        String categoryId = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_ID);
        String catName = getIntent().getStringExtra(Constants.EXTRA_CATEGORY_NAME);
        setTitle(catName);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        if (tvToolbarTitle != null) {
            tvToolbarTitle.setMaxEms(12);
        }

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_CATEGORY_ID, categoryId);
        bundle.putString(Constants.EXTRA_CATEGORY_NAME, catName);
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_NAME, catName);
        bundle.putString(Constants.EXTRA_SUB_CATEGORY_ID, "");

        addFragment(R.id.content_frame, DirectoryDetailsFragment.newInstance(bundle));
    }

}
