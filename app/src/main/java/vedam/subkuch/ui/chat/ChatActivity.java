package vedam.subkuch.ui.chat;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;

public class ChatActivity extends BaseActivity {

    private Disposable internetDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        setToolbarBackButton();

        addFragment(R.id.content_frame, ChatFragment.newInstance(getIntent().getExtras()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setConnection, Crashlytics::logException);
    }

    private void setConnection(Boolean isConnected) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ChatFragment)
            ((ChatFragment) fragment).setIsConnected(isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable subscription) {

        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}
