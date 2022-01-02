package vedam.subkuch.ui.needs;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;

public class NeedsActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        setTitle(R.string.needs);
        setToolbarBackButton();
        requestLocation(false);
        bindCallbacks();
        addFragment(R.id.content_frame, NeedsFragment.newInstance());
    }

    @Override
    public void onLocationChanged(Location location) {
        DataFetcher.updateLocation(this, null, AddEventResponse.class, null, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude()));
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
