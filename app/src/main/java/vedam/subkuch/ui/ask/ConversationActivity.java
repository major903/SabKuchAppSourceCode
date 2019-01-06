package vedam.subkuch.ui.ask;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;

public class ConversationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(getIntent().getStringExtra(Constants.EXTRA_CATEGORY_NAME));

        addFragment(R.id.content_frame, ConversationFragment.newInstance(getIntent().getExtras()));
    }
}
