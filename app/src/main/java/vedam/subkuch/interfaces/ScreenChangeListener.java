package vedam.subkuch.interfaces;

import android.content.Intent;
import android.os.Bundle;

import vedam.subkuch.utils.TargetScreen;


/**
 * Created by nansari on 6/12/2016.
 * For project RelayServices
 */
public interface ScreenChangeListener {

    void onScreenChange(TargetScreen targetScreen, Bundle bundle, boolean finishCurrentActivity, int flags);

    void handleActivityIntent(Intent intent);

    void handleServiceIntent(Intent intent);

    void handleActivityForResultIntent(Intent intent, int requestCode);
}
