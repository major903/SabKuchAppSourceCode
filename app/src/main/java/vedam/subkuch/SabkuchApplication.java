package vedam.subkuch;

import android.app.Application;

import vedam.subkuch.network.Response;

import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.RegistrationMasterCache;
import vedam.subkuch.network.models.RegistrationMasterResponse;

/**
 * Created by naddy on 27/12/15.
 */
public class SabkuchApplication extends Application {

    public static final String TAG = SabkuchApplication.class
            .getSimpleName();

    private static SabkuchApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        warmContributionMasterData();
    }

    private void warmContributionMasterData() {
        if (!DataFetcher.isRegistrationApiConfigured()) return;

        if (!RegistrationMasterCache.areStatesFresh(this)) {
            DataFetcher.getRegistrationStates(this,
                    new Response.Listener<RegistrationMasterResponse>() {
                @Override
                public void onResponse(RegistrationMasterResponse response) {
                    if (hasMasterData(response)) {
                        RegistrationMasterCache.putStates(SabkuchApplication.this,
                                response.getReturnData());
                    }
                }
            }, RegistrationMasterResponse.class, error -> { });
        }
        if (!RegistrationMasterCache.areDistrictsFresh(this)) {
            DataFetcher.getRegistrationDistricts(this,
                    new Response.Listener<RegistrationMasterResponse>() {
                @Override
                public void onResponse(RegistrationMasterResponse response) {
                    if (hasMasterData(response)) {
                        RegistrationMasterCache.putDistricts(SabkuchApplication.this,
                                response.getReturnData());
                    }
                }
            }, RegistrationMasterResponse.class, error -> { });
        }
    }

    private boolean hasMasterData(RegistrationMasterResponse response) {
        return response != null && response.getReturnData() != null
                && !response.getReturnData().isEmpty();
    }

    public static synchronized SabkuchApplication getInstance() {
        return mInstance;
    }

}
