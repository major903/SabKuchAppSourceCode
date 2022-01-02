package vedam.subkuch.ui.movies;

import android.location.Location;
import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;

public class MoviesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();
        setTitle(R.string.movies);
        requestLocation(false);
        addFragment(R.id.content_frame, MoviesFragment.newInstance());
    }

    @Override
    public void onLocationChanged(Location location) {
        DataFetcher.updateLocation(this, null, AddEventResponse.class, null, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude()));
    }
}
