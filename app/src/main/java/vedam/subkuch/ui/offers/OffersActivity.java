package vedam.subkuch.ui.offers;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class OffersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.offers);

        addFragment(R.id.content_frame, OffersFragment.newInstance());
        bindCallbacks();
    }

    private void bindCallbacks() {

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof OffersFragment)
                setTitle(R.string.offers);
        });
    }
}
