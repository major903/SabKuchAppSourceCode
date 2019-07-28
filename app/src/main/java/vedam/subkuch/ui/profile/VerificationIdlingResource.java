package vedam.subkuch.ui.profile;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import vedam.subkuch.network.models.OtpResponse;

public class VerificationIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;
    private OtpResponse response;
    // Idleness is controlled with this boolean.
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    void setIdleState(boolean isIdleNow, OtpResponse response) {
        mIsIdleNow.set(isIdleNow);
        this.response = response;
        if (isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }

    public OtpResponse getResponse() {
        return response;
    }
}
