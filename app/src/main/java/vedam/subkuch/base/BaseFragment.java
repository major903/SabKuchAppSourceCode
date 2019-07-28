package vedam.subkuch.base;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnFragmentInteractionListener;
import vedam.subkuch.interfaces.ScreenChangeListener;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.ErrorResponse;
import vedam.subkuch.network.models.Image;
import vedam.subkuch.ui.matrimonial.preference.ItemAdapter;
import vedam.subkuch.uicomponent.SlideShowDialogFragment;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.base.BaseActivity.TAG;


/**
 * Created by nansari on 6/17/2016.
 */
public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public Context context;
    private OnFragmentInteractionListener mListener;
    private ScreenChangeListener screenChangeListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected PopupWindow mPopupWindow;

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

    void logout() {
        if (getGlobalFragmentInteractionListener() != null)
            getGlobalFragmentInteractionListener().logout();
    }
    protected void parseAndShowError(VolleyError error) {

        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String response = new String(networkResponse.data);
            LogUtils.LOGE(TAG, "response error:" + response);

            try {
                ErrorResponse errorResponse = new Gson().fromJson(response, ErrorResponse.class);

                if (!TextUtils.isEmpty(errorResponse.getReturnMessage()))
                    UiUtil.showToast(context, errorResponse.getReturnMessage());
                else if (!TextUtils.isEmpty(errorResponse.getMessage()))
                    UiUtil.showToast(context, errorResponse.getMessage());
                else
                    UiUtil.showToast(context, getString(R.string.err_occurred));
            } catch (Exception exception) {
                Crashlytics.logException(exception);
                exception.printStackTrace();
                UiUtil.showToast(context, getString(R.string.err_occurred));
            }
        } else {
            UiUtil.showToast(context, getString(R.string.err_unknown));
        }
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

    public void baseshowFeedbackMessage(View view, String message) {
        try {
            Snackbar snakbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            TextView tv = snakbar.getView().findViewById(R.id.snackbar_text);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            snakbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
            if (snakbar.isShown()) {
                snakbar.dismiss();
            }
            snakbar.show();
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
    }

    public void showPopWindow(View view, ItemAdapter adapter) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.view_pop_window, null);
        RecyclerView recylcerView = customView.findViewById(R.id.recyclerView);
        recylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recylcerView.setAdapter(adapter);
        mPopupWindow = new PopupWindow(customView, view.getWidth(), WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAsDropDown(view, 0, 10);
    }

    public Integer checkPermission(String[] permission) {
        int permissionNeeded = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permission.length; i++) {
                int result = ContextCompat.checkSelfPermission(getActivity(), permission[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionNeeded++;
                }
            }
        }
        return permissionNeeded;
    }

    protected void requestLocation() {
        if (getGlobalFragmentInteractionListener() != null)
            getGlobalFragmentInteractionListener().requestLocation(false);
    }
}
