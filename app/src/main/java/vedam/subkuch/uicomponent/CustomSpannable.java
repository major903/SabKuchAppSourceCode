package vedam.subkuch.uicomponent;

/**
 * Created by nansari on 9/30/2016.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import vedam.subkuch.R;


public class CustomSpannable extends ClickableSpan {

    private boolean isUnderline = true;
    private Context context;

    /**
     * Constructor
     */
    public CustomSpannable(boolean isUnderline, Context context) {
        this.isUnderline = isUnderline;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        ds.setUnderlineText(isUnderline);

    }

    @Override
    public void onClick(View widget) {

    }
}
