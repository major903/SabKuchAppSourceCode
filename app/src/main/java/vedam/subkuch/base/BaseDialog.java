package vedam.subkuch.base;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import vedam.subkuch.R;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

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

        UiUtil.showToast(getContext(), getContext().getString(R.string.err_occurred));
    }

    public BaseDialog(@NonNull Context context) {
        super(context);
    }
}
