package vedam.subkuch.ui.matrimonial.viewProfile;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;

public class ViewProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        String name = getIntent().getStringExtra(Constants.EXTRA_NAME);
        setTitle(name);
        setToolbarBackButton();
        addFragment(R.id.content_frame, ViewProfileFragment.newInstance(getIntent().getExtras()));
    }
}
