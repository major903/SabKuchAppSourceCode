package vedam.subkuch.ui.public_utility;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class PublicUtilityActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.public_utility);

        addFragment(R.id.content_frame, SubDirectoryFragment.newInstance());
        bindCallbacks();
    }

    private void bindCallbacks() {
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof SubDirectoryFragment)
            setTitle(R.string.public_utility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }
}
