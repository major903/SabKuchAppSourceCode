package vedam.subkuch.ui.needs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class NeedsActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle(R.string.needs);
        setToolbarBackButton();

        bindCallbacks();
        addFragment(R.id.content_frame, NeedsFragment.newInstance());
    }

    private void bindCallbacks() {
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof NeedsFragment)
            setTitle(R.string.needs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }
}
