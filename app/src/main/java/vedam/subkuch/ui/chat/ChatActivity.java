package vedam.subkuch.ui.chat;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class ChatActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();

        addFragment(R.id.content_frame, ChatFragment.newInstance(getIntent().getExtras()));
    }
}
