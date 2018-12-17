package vedam.subkuch.uicomponent;

/**
 * Created by nansari on 10/6/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import vedam.subkuch.R;


public class PickImageDialog extends Dialog implements View.OnClickListener {

    public static final int KEY_CAMERA = 1;
    public static final int KEY_GALLERY = 2;
    private int selected_key = 0;
    private CloseButtonClickListener closeButtonClickListener;

    public interface CloseButtonClickListener {
        void onCloseClicked(View v);
    }

    protected PickImageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initUI();
    }


    public PickImageDialog(Context context) {
        super(context);
        initUI();
    }

    public PickImageDialog(Context context, int theme) {
        super(context, theme);
        initUI();
    }

    public void setCloseButtonClickListener(CloseButtonClickListener closeButtonClickListener) {
        this.closeButtonClickListener = closeButtonClickListener;
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pick_image);

        findViewById(R.id.ib_cancel).setOnClickListener(this);
        findViewById(R.id.cv_take_picture).setOnClickListener(this);
        findViewById(R.id.cv_gallery).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_cancel:
                this.dismiss();
                if (closeButtonClickListener != null) {
                    closeButtonClickListener.onCloseClicked(v);
                }
                break;
            case R.id.cv_take_picture:
                selected_key = KEY_CAMERA;
                this.dismiss();
                break;
            case R.id.cv_gallery:
                selected_key = KEY_GALLERY;
                this.dismiss();
                break;
        }
    }

    public int getSelected_key() {
        return selected_key;
    }
}

