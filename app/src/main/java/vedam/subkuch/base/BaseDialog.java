package vedam.subkuch.base;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.network.models.ErrorResponse;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.base.BaseActivity.TAG;

public class BaseDialog extends Dialog {

    protected Response.ErrorListener onErrorListener = error -> {

        LogUtils.LOGD("ERROR", error.getMessage());
        onErrorReceived(error);

    };

    protected void onErrorReceived(VolleyError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(getContext(), getContext().getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(getContext(), getContext().getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(getContext(), getContext().getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError) {
            UiUtil.showToast(getContext(), getContext().getString(R.string.err_unauthorized));
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void parseAndShowError(VolleyError error) {

        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String response = new String(networkResponse.data);
            LogUtils.LOGE(TAG, "response error:" + response);

            try {
                ErrorResponse errorResponse = new Gson().fromJson(response, ErrorResponse.class);

                if (!TextUtils.isEmpty(errorResponse.getReturnMessage()))
                    UiUtil.showToast(getContext(), errorResponse.getReturnMessage());
                else if (!TextUtils.isEmpty(errorResponse.getMessage()))
                    UiUtil.showToast(getContext(), errorResponse.getMessage());
                else
                    UiUtil.showToast(getContext(), getContext().getString(R.string.err_occurred));
            } catch (Exception exception) {
                Crashlytics.logException(exception);
                exception.printStackTrace();
                UiUtil.showToast(getContext(), getContext().getString(R.string.err_occurred));
            }
        } else {
            UiUtil.showToast(getContext(), getContext().getString(R.string.err_unknown));
        }
    }

    public BaseDialog(@NonNull Context context) {
        super(context);
    }
}
