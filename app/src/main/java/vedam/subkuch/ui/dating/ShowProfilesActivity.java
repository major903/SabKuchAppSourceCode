package vedam.subkuch.ui.dating;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.ui.dating.editProfile.EditProfileFragment;

public class ShowProfilesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle(R.string.profiles);
        setToolbarBackButton();
        addFragment(R.id.content_frame, ShowProfilesFragment.newInstance());

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (fragment instanceof ShowProfilesFragment)
                setTitle(R.string.profiles);
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof EditProfileFragment)
            ((EditProfileFragment) fragment).onLocationChanged(location);
    }
}
