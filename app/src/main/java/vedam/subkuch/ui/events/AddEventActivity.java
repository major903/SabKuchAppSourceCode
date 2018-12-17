package vedam.subkuch.ui.events;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class AddEventActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setToolbarBackButton();
        setTitle(R.string.add_event);

        addFragment(R.id.content_frame, AddEventFragment.newInstance());
    }
}
