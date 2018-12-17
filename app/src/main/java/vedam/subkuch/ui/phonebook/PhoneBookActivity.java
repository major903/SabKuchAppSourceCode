package vedam.subkuch.ui.phonebook;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class PhoneBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.phone_book);

        addFragment(R.id.content_frame, PhoneBookFragment.newInstance());

    }
}
