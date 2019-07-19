package vedam.subkuch.base;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import vedam.subkuch.R;
import vedam.subkuch.interfaces.OnFragmentInteractionListener;
import vedam.subkuch.interfaces.ScreenChangeListener;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

public class BaseListFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
    public Context context;
    protected OnFragmentInteractionListener mListener;
    private ScreenChangeListener screenChangeListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected Response.ErrorListener onErrorListener = error -> {

        LogUtils.LOGD("ERROR", error.getMessage());
        if (getActivity() != null)
            onErrorReceived(error);

    };

    protected void onErrorReceived(VolleyError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(context, getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(context, getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(context, getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError || (error.networkResponse != null &&
                error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED)) {
            logout();
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void logout() {
        if (getGlobalFragmentInteractionListener() != null)
            getGlobalFragmentInteractionListener().logout();
    }

    protected void parseAndShowError(VolleyError error) {

        UiUtil.showToast(context, getString(R.string.err_occurred));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSwipeViewIfPresent(view);
    }

    /**
     * The id should always be swipe_refresh
     *
     * @param view View inflated
     */
    private void initSwipeViewIfPresent(View view) {
//        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
//        if (swipeRefreshLayout != null)
//            swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        if (context instanceof ScreenChangeListener)
            screenChangeListener = (ScreenChangeListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement ScreenChangeListener");

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        screenChangeListener = null;
        context = null;
    }

    protected void addFragmentWithAnimation(final int containerId, Fragment fragment, String tag, boolean addToBackStack) {

        addFragment(containerId, fragment, tag, addToBackStack,
                R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void addFragment(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                               @AnimRes int enterAnim, @AnimRes int exitAnim,
                               @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).addFragment(containerId, fragment, tag, addToBackStack,
                    enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }
    }

    protected void replaceFragment(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                                   @AnimRes int enterAnim, @AnimRes int exitAnim,
                                   @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).replaceFragment(containerId, fragment, tag, addToBackStack,
                    enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }
    }

    /**
     * Call this method to set title of toolbar in the parent activity from fragment
     *
     * @param title Title to be set
     */
    protected void setTitle(String title) {
        if (context instanceof BaseActivity) ((BaseActivity) context).setTitle(title);

    }

    /**
     * Call this method to set title of toolbar in the parent activity from fragment
     *
     * @param title Title to be set
     */
    protected void setTitle(int title) {
        if (context instanceof BaseActivity) ((BaseActivity) context).setTitle(title);

    }

    @Override
    public Context getContext() {
        return context;
    }

    public OnFragmentInteractionListener getGlobalFragmentInteractionListener() {
        return mListener;
    }

    public ScreenChangeListener getScreenChangeListener() {
        return screenChangeListener;
    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }

    public void onBackPressed() {
        if (context instanceof BaseActivity) ((BaseActivity) context).onBackPressed();
    }

    /**
     * requests parent activity to get {@link Address} corresponding to current location
     */
    protected void getAddress(boolean shouldForce) {
        if (mListener != null) {
            mListener.requestAddress(shouldForce);
        }
    }
}
