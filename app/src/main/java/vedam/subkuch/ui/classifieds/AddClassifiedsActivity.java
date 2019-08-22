package vedam.subkuch.ui.classifieds;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class AddClassifiedsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setToolbarBackButton();
        setTitle(R.string.add_classified);

        addFragment(R.id.content_frame, AddClassifiedFragment.newInstance());
    }
}
