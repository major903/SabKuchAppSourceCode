package vedam.subkuch.ui.movies;

import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class MoviesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.movies);

        addFragment(R.id.content_frame, MoviesFragment.newInstance());
    }
}
