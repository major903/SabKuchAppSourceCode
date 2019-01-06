package vedam.subkuch.base;


import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnFragmentInteractionListener;
import vedam.subkuch.interfaces.ScreenChangeListener;
import vedam.subkuch.network.models.Image;
import vedam.subkuch.uicomponent.SlideShowDialogFragment;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;


/**
 * Created by nansari on 6/17/2016.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Context context;
    protected OnFragmentInteractionListener mListener;
    private ScreenChangeListener screenChangeListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    protected Response.ErrorListener onErrorListener = error -> {

        LogUtils.LOGD("ERROR", error.getMessage());
        onErrorReceived(error);

    };

    protected void onErrorReceived(VolleyError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(context, getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(context, getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(context, getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError) {
            UiUtil.showToast(context, getString(R.string.err_unauthorized));
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void parseAndShowError(VolleyError error) {

        UiUtil.showToast(context, getString(R.string.err_occurred));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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

    protected void addFragment(final int containerId, Fragment fragment, String tag, boolean addToBackStack,
                               @AnimRes int enterAnim, @AnimRes int exitAnim,
                               @AnimRes int enterAnimPop, @AnimRes int exitAnimPop) {

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).addFragment(containerId, fragment, tag, addToBackStack,
                    enterAnim, exitAnim, enterAnimPop, exitAnimPop);
        }
    }

    protected void addFragmentWithAnimation(final int containerId, Fragment fragment, String tag, boolean addToBackStack) {

        addFragment(containerId, fragment, tag, addToBackStack,
                R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
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

    //START_GALLERY
    protected void setGallery(ArrayList<Image> alImage, int selectedPosition, boolean isUrls) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_IMAGE_ITEMS, alImage);
        bundle.putInt(Constants.EXTRA_POSITION, selectedPosition);
        bundle.putBoolean(Constants.EXTRA_IS_IMAGE_URLS, isUrls);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        SlideShowDialogFragment newFragment = SlideShowDialogFragment.newInstance(bundle);
        newFragment.show(ft, "slideshow");
    }
}
