package vedam.subkuch;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import vedam.subkuch.network.WebServices;

/**
 * Created by naddy on 27/12/15.
 */
public class SabkuchApplication extends Application {

    public static final String TAG = SabkuchApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static SabkuchApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new WebServices(getApplicationContext());
    }

    public static synchronized SabkuchApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
