package vedam.subkuch.uicomponent;

import android.content.Context;
import android.util.AttributeSet;

import vedam.subkuch.utils.UiUtil;

public class RequiredTextView extends androidx.appcompat.widget.AppCompatTextView {

    public RequiredTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        UiUtil.setRequiredTextView(context, getText().toString(), this);

    }
}
