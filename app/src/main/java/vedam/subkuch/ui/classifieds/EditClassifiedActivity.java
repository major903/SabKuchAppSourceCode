package vedam.subkuch.ui.classifieds;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class EditClassifiedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setToolbarBackButton();
        setTitle(R.string.edit_classified);

        if (savedInstanceState == null)
            addFragment(R.id.content_frame, EditClassifiedFragment.newInstance(getIntent().getExtras()));
    }
}
