package vedam.subkuch.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.IOException;

import vedam.subkuch.R;
import vedam.subkuch.uicomponent.CustomSpannable;
import vedam.subkuch.uicomponent.CustomTypefaceSpan;


public class UiUtil {
    private static final String TAG = "UiUtil";

    private static ProgressDialog pDialog;
    //private static TwoButtonDialog chatDialog_Tab, chatDialog_No, chatDialog_other;

    public static void showNoNetwork(Context context, View view, View.OnClickListener listener) {

        Snackbar snackbar = Snackbar
                .make(view, context.getString(R.string.err_no_network_avail), Snackbar.LENGTH_LONG)
                .setAction(context.getString(R.string.retry), listener);

        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    public static void showMessage(View view, String msg) {
//        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
//        snackbar.show();

        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setAllCaps(true);
        textView.setTextColor(Color.RED);
        snackbar.show();


    }

    public static void showProgressDialog(Context context, @NonNull String msg) {
        showProgressDialog(context, null, msg, false);
    }

    public static void showProgressDialog(Context context, @NonNull int msg) {
        if (context == null)
            return;
        showProgressDialog(context, null, context.getString(msg), false);
    }

    public static void showProgressDialog(Context context, @NonNull String msg, boolean cancelable) {
        showProgressDialog(context, null, msg, cancelable);
    }


    public static void showProgressDialog(Context context, @NonNull String title, @NonNull String msg) {
        showProgressDialog(context, title, msg, false);
    }

    public static void showProgressDialog(Context context, String title, @NonNull String msg, boolean cancelable) {
        if (context == null)
            return;

        if ((pDialog != null && !pDialog.isShowing()) || pDialog == null)
            pDialog = initProgressDialog(context);

        if (title != null)
            pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setCanceledOnTouchOutside(cancelable);
        pDialog.setCancelable(cancelable);
        pDialog.show();
    }

    private static ProgressDialog initProgressDialog(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return new ProgressDialog(context);
        else
            return new ProgressDialog(context, R.style.AlertDialogTheme);
    }

    public static ProgressDialog getProgressDialog() {
        return pDialog;
    }


    public static void cancelProgressDialog() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog = null;
            }
        } catch (Exception e) {
            Crashlytics.logException(e);
            LogUtils.LOGE(TAG, e.getMessage(), e);
        } finally {
            pDialog = null;
        }
    }

    public static void showDialog(Context context, CharSequence message, DialogInterface.OnClickListener okListener, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(cancelable)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .show();

        Typeface font = getTypeface(context, context.getString(R.string.typeface_regular));
        TextView textView = dialog.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setTypeface(font);
        }
    }

    public static void showDialog(Context context, String title, String message, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();

        Typeface font1 = getTypeface(context, context.getString(R.string.typeface_bold));
        Typeface font2 = getTypeface(context, context.getString(R.string.typeface_regular));
        TextView tvTitle = dialog.findViewById(android.R.id.title);
        TextView tvMessage = dialog.findViewById(android.R.id.message);
        setTypeface(tvTitle, font1);
        setTypeface(tvMessage, font2);
    }

    public static void showDialog(Context context, String message, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(cancelable)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();


        Typeface font2 = getTypeface(context, context.getString(R.string.typeface_regular));
        TextView tvMessage = dialog.findViewById(android.R.id.message);
        setTypeface(tvMessage, font2);
    }

    private static void setTypeface(TextView tv, Typeface font) {
        if (tv != null)
            tv.setTypeface(font);
    }

    public static void showConfirmationDialog(Context context, String message, DialogInterface.OnClickListener yesListener,
                                              DialogInterface.OnClickListener noListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.yes), yesListener);

        if (noListener == null)
            builder.setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        else
            builder.setNegativeButton(context.getString(R.string.no), noListener);

        builder.create().show();
    }

    public static void showToast(Context context, @NonNull String msg) {

        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, @NonNull String msg, int length) {
        if (context == null)
            return;
        Toast toast = Toast.makeText(context, msg, length);
        toast.show();

    }

    public static void showHEADERToast(Context context, String msg) {
        if (context == null)
            return;
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.show();

    }

    public static void hideKeyBoard(Context context, View editText) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void setImageView(ImageSetter imageSetter) {
        if (imageSetter == null) {
            return;
        }

        Picasso picasso = Picasso.get();

        RequestCreator requestCreator;
        if (!TextUtils.isEmpty(imageSetter.getImageLink()))
            requestCreator = picasso.load(imageSetter.getImageLink());
        else if (imageSetter.getImageResource() != 0)
            requestCreator = picasso.load(imageSetter.getImageResource());
        else if (!TextUtils.isEmpty(imageSetter.getFilePath()))
            requestCreator = picasso.load(new File(imageSetter.getFilePath()));
        else
            return;

        if (imageSetter.isDefaultsSet()) {
            try {
                requestCreator.placeholder(R.drawable.grey);
                requestCreator.error(R.drawable.grey);
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
        } else {
            if (imageSetter.getPlaceholderResource() != 0)
                requestCreator.placeholder(imageSetter.getPlaceholderResource());
            if (imageSetter.getErrorResource() != 0)
                requestCreator.error(imageSetter.getErrorResource());
        }

        int maxSize = imageSetter.getMaxSize();
        if (maxSize != 0)
            requestCreator.resize(maxSize, maxSize);
        if (imageSetter.isFit())
            requestCreator.fit();
        if (imageSetter.isCenterCrop())
            requestCreator.centerCrop();
        if (imageSetter.isCenterInside())
            requestCreator.centerInside();

        setImageViewPeripherals(imageSetter.getContext(), requestCreator, imageSetter);

    }

    private static void setImageViewPeripherals(Context context, RequestCreator requestCreator, ImageSetter imageSetter) {
        if (requestCreator == null)
            return;
        if (imageSetter.getCallback() == null)
            requestCreator.tag(context).into(imageSetter.getIvTarget());
        else
            requestCreator.tag(context).into(imageSetter.getIvTarget(), imageSetter.getCallback());
    }

    public static SpannableString getBulletedString(String text) {
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new BulletSpan(16), 0, text.length(), 0);
        return spannable;
    }

    public static Bitmap rotateImageIfRequired(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;

        o.inSampleSize = scale;
        o.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeFile(filePath, o);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        if (exif == null) {
            // rotation failed.
            return img;
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            rotationAngle = 90;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            rotationAngle = 180;
        }
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            rotationAngle = 270;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle);
        if (img == null)
            return null;
        Bitmap rotatedBitmap = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        System.gc();
        return rotatedBitmap;
    }

    public static Bitmap getThumbnail(Context context, Bitmap bitmap) {

        float actualWidth = bitmap.getWidth();
        float actualHeight = bitmap.getHeight();

        float max = AppUtil.dpToPx(context, 120);

        float convertedHeight = actualHeight / actualWidth * max;

        return Bitmap.createScaledBitmap(bitmap, (int) max, (int) convertedHeight, true);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width < maxSize && height < maxSize)
            return image;

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public static void setTextViewResizable(final Context context, final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }

        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(context, tv.getText().toString(), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(context, tv.getText().toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
                //text is less than maxLine so don't do anything
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Context context, final String str, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {

        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        if (str.contains(spanableText)) {

            ssb.setSpan(new CustomSpannable(false, context) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        setTextViewResizable(context, tv, 0, "view less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        setTextViewResizable(context, tv, 3, "...view more", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    /**
     * concatenates prefix and suffix and makes the prefix to be bold, then sets the text to textview
     *
     * @param prefix prefix to bold
     * @param suffix suffix to be concatenated with prefix
     * @param tv     TextView to be set
     */
    public static void setTextViewWithBoldPrefix(Context context, String prefix, String suffix, TextView tv) {

        if (TextUtils.isEmpty(suffix))
            tv.setVisibility(View.GONE);
        else {

//            Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.typeface_heavy));
//            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(typeface);
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s", prefix, suffix));
            str.setSpan(boldSpan, 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(str);
        }
    }

    /**
     * concatenates prefix and suffix and makes the suffix to be bold, then sets the text to textview
     *
     * @param prefix prefix to concatenated with suffix
     * @param suffix suffix to be bold
     * @param tv     TextView to be set
     */
    public static void setTextViewWithBoldSuffix(Context context, String prefix, String suffix, TextView tv) {

        if (TextUtils.isEmpty(suffix))
            tv.setVisibility(View.GONE);
        else {

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.typeface_heavy));
            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(typeface);

            final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s", prefix, suffix));
            str.setSpan(boldSpan, prefix.length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(str);
        }
    }

    /**
     * concatenates prefix and suffix and makes the suffix to be bold, then sets the text to textview
     *
     * @param prefix prefix to concatenated with mid string
     * @param value  value to be bold
     * @param suffix suffix to be concatenated with mid string
     * @param tv     TextView to be set
     */
    public static void setTextViewWithBoldMid(Context context, String prefix, String value, String suffix, TextView tv) {

        if (TextUtils.isEmpty(value))
            tv.setVisibility(View.GONE);
        else {

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.typeface_heavy));
            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(typeface);

            final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s %s", prefix, value, suffix));
            str.setSpan(boldSpan, prefix.length() + 1, prefix.length() + 1 + value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(str);
        }
    }

    public static void setTextViewWithBoldMidNoSpace(Context context, String prefix, String value, String suffix, TextView tv) {

        if (TextUtils.isEmpty(value))
            tv.setVisibility(View.GONE);
        else {

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.typeface_heavy));
            MetricAffectingSpan boldSpan = new CustomTypefaceSpan(typeface);

            final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s%s", prefix, value, suffix));
            str.setSpan(boldSpan, prefix.length() + 1, prefix.length() + 1 + value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(str);
        }
    }

    /*public static void setRequiredTextView(Context context, String value, TextView tv) {

        String fullString = String.format("%s*", value);

        final SpannableStringBuilder str = new SpannableStringBuilder(fullString);
        str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.accent)),
                fullString.length() - 1, fullString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(str);
    }*/

    /**
     * Sets the text view and handles the visibility of it
     *
     * @param tv    The text view to be set
     * @param value the value to be set on text view
     */
    public static void setTextView(TextView tv, CharSequence value) {

        if (TextUtils.isEmpty(value))
            tv.setVisibility(View.GONE);
        else
            tv.setText(value);
    }

    public static void setTextView(String prefix, String suffix, TextView tv) {
        if (!TextUtils.isEmpty(suffix) && !TextUtils.isEmpty(prefix)) {
            tv.setText(String.format("%s %s", prefix, suffix));
            tv.setVisibility(View.VISIBLE);
        } else
            tv.setVisibility(View.GONE);

    }

    public static void setTextView(TextView tv, Spannable value) {

        if (TextUtils.isEmpty(value))
            tv.setVisibility(View.GONE);
        else
            tv.setText(value);
    }

    /**
     * Sets the value in edit text. It will not hide the widget
     *
     * @param et    the edit text widget to be set
     * @param value the value to be set in edit text
     */
    public static void setEditText(EditText et, String value) {
        if (!TextUtils.isEmpty(value))
            et.setText(value);
    }

    /**
     * Sets the text view and handles the visibility of it. It also handles the visibility of the top heading text view
     *
     * @param tv        The text view to be set
     * @param tvHeading The heading text view
     * @param value     the value to be set on text view
     */
    public static void setTextView(TextView tv, TextView tvHeading, String value) {

        if (TextUtils.isEmpty(value)) {
            tv.setVisibility(View.GONE);
            tvHeading.setVisibility(View.GONE);
        } else
            tv.setText(value);
    }

    public static void setTextView(TextView tv, TextView tvHeading, ImageView ivIcon, String value) {

        if (TextUtils.isEmpty(value)) {
            tv.setVisibility(View.GONE);
            tvHeading.setVisibility(View.GONE);
            ivIcon.setVisibility(View.GONE);
        } else
            tv.setText(value);
    }

    public static Typeface getTypeface(Context context, String fontName) {
        return Typeface.createFromAsset(context.getAssets(), fontName);
    }

    public static void setRatingText(TextView textView, float ratingText) {
        textView.setText(String.format("%.1f", ratingText));
    }

    public static InputFilter getCharInputFilter() {

        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Only keep characters that are alphanumeric
                StringBuilder builder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (Character.isLetter(c) || c == ' ') {
                        builder.append(c);
                    }
                }

                // If all characters are valid, return null, otherwise only return the filtered characters
                boolean allCharactersValid = (builder.length() == end - start);
                return allCharactersValid ? null : builder.toString();
            }
        };
    }

    public static void setWebView(Context context, @NonNull String description, WebView webView, boolean isShortDescription, int backgroundColor, String textColor) {

        description = description.replaceAll("&nbsp;", " ");
        description = description.replaceAll("&rdquo;", "\"");

        if (textColor == null)
            textColor = "#000000";

        String SERVICE_DESCRIPTION_HEADER;
        if (isShortDescription)
            SERVICE_DESCRIPTION_HEADER = "service_description_header_2.html";
        else
            SERVICE_DESCRIPTION_HEADER = "service_description_header.html";

        String SERVICE_DESCRIPTION_FOOTER = "service_description_footer.html";

        String header = AppUtil.getTextFromAsset(context, SERVICE_DESCRIPTION_HEADER);
        String footer = AppUtil.getTextFromAsset(context, SERVICE_DESCRIPTION_FOOTER);
        String fullDescription = String.format("%s<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/avenirregular.ttf\")}body {font-family: MyFont; padding: 0; margin:0}</style><div style=\"color:%s\"; class=\"more\">%s</div>%s", header, textColor, description, footer);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultFontSize(17);

        if (backgroundColor != 0)
            webView.setBackgroundColor(backgroundColor);
        else
            webView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        webView.loadDataWithBaseURL(null, fullDescription, "text/html", "utf-8", null);

        webView.setVisibility(View.VISIBLE);
    }

    public static void setWebView(Context context, String description, WebView webView, int backgroundColor, String textColor) {

        if (TextUtils.isEmpty(description)) {
            webView.setVisibility(View.VISIBLE);
            return;
        }

        if (backgroundColor != 0)
            webView.setBackgroundColor(backgroundColor);
        else
            webView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

        webView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

        webView.setVisibility(View.VISIBLE);
    }

    public static void setTextViewWithSpan(String prefix, String suffix, int color, TextView tv) {

        final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s", prefix, suffix));
        str.setSpan(new ForegroundColorSpan(color), prefix.length() + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(str);
    }

    public static void setTextViewWithMidSpan(String prefix, String value, String suffix, int color, TextView tv) {

        if (TextUtils.isEmpty(value))
            tv.setVisibility(View.GONE);
        else {

            final SpannableStringBuilder str = new SpannableStringBuilder(String.format("%s %s %s", prefix, value, suffix));
            str.setSpan(new ForegroundColorSpan(color), prefix.length() + 1, prefix.length() + 1 + value.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv.setText(str);
        }
    }

    public static void setEditTextLength(EditText editText, int length) {

        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(inputFilters);
    }
}
