package vedam.subkuch.uicomponent;

import android.content.Context;
import android.util.AttributeSet;

import vedam.subkuch.utils.UiUtil;

public class OptionalTextView extends android.support.v7.widget.AppCompatTextView {

    public OptionalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        UiUtil.setOptionalTextView(getText().toString(), this);

    }
}
