package vedam.subkuch.uicomponent;

import android.content.Context;
import android.util.AttributeSet;

import vedam.subkuch.utils.UiUtil;

public class RequiredTextView extends android.support.v7.widget.AppCompatTextView {

    public RequiredTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        UiUtil.setRequiredTextView(getText().toString(), this);

    }
}
