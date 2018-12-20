package vedam.subkuch.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.webkit.URLUtil;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import vedam.subkuch.R;
import vedam.subkuch.ui.directory.models.BusinessAddress;

public class AppUtil {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String appVersion;

    public static boolean validateString(String data) {
        return data != null && !(data.trim().equalsIgnoreCase("null") || data.trim().equalsIgnoreCase(""));
    }

    public static boolean validateEmail(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    public static boolean validateDob(final String dob) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        try {
            simpleDateFormat.parse(dob);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isOnlyNumber(String value) {
        boolean ret = false;
        if (!TextUtils.isEmpty(value)) {
            ret = value.matches("^[0-9]+$");
        }
        return ret;
    }

    public static String getAppVersion() {
        return appVersion;
    }

    public static void setAppVersion(Context context) {
        PackageInfo packageInfo;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Crashlytics.logException(e);

            e.printStackTrace();
        }

    }

    @SuppressLint("NewApi")
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the
                // range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    public static boolean validateURL(String url) {
        return URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url);
    }

    public static Date getDate(String dateString, TimeZone timeZone) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.parse(dateString);
    }

    /*
     * Helper function that determines if a string (check) is in a given set of
     * strings.
     */
    public static boolean in(String check, String[] set) {
        for (String thisString : set) {
            if (check.equals(thisString)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Turn bitmap into base 64
     *
     * @param bitmap bitmap
     * @return base 64 string
     */
    public static String getBase64FromBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /**
     * @param context     Context
     * @param permissions the list of permissions to be checked
     * @return true if all the list of permissions are granted
     */
    public static boolean checkPermissions(Context context, List<String> permissions) {

        for (String permission : permissions) {
            int hasPermission = ContextCompat.checkSelfPermission(context, permission);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean checkPermissions(Context context, String[] permissions) {

        for (String permission : permissions) {
            int hasPermission = ContextCompat.checkSelfPermission(context, permission);
            if (hasPermission != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     * @param str   The singular form of the string
     * @param count the count to which this string is related
     * @return the singular or plural form of the string based on the count
     */
    public static String getSingularOrPluralString(String str, int count) {
        if (str == null)
            return "";
        if (count <= 1)
            return str;
        else
            return String.format("%ss", str);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                LogUtils.LOGI(activity.getString(R.string.app_name), "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Returns sum pf string as a floating point value. The string must be parsable to float otherwise an exception
     * will be thrown
     *
     * @param values all the string values to be added
     * @return sum of strings as float
     */
    public static float getFloatSumOfStrings(String... values) {
        float totalValue = 0;
        for (String value : values) {
            totalValue += Float.valueOf(value);
        }

        return totalValue;
    }

    public static void startEmailIntent(Context context, String mailTo) {
        startEmailIntent(context, mailTo, null);
    }

    public static void startEmailIntent(Context context, String mailTo, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mailTo, null));
        if (!TextUtils.isEmpty(subject))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    /**
     * Reads json string from json file
     *
     * @param context  Context
     * @param fileName Filename including the extension which needs to be parsed
     * @return a formatted string contained in the file
     */
    public static String getTextFromAsset(Context context, String fileName) {
        String file;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            file = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Crashlytics.logException(ex);
            ex.printStackTrace();
            return null;
        }
        return file;
    }

    public static void openAppSettings(Context context) {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void openUrl(Context context, String url) {

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            UiUtil.showToast(context, context.getString(R.string.err_occurred));
            Crashlytics.logException(e);
        }
    }

    public static String getFormattedPrice(String price) {
        if (TextUtils.isEmpty(price))
            return null;
        return String.format("$%.2f", Float.valueOf(price));
    }

    public static String getFormattedPrice(double price) {
        if (price <= 0)
            return null;
        return String.format("$%.2f", price);
    }

    public static int dpToPx(Context context, int dps) {
        return Math.round(context.getResources().getDisplayMetrics().density * dps);
    }

    public static String getFormattedCurrency(String amount) {

        NumberFormat currencyFormatter =
                NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(Double.parseDouble(amount));
    }

    public static String replaceUnderscoreAndCapitalize(String string) {

        if (TextUtils.isEmpty(string))
            return "";

        String[] words = string.trim().split("_");

        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            sb.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static int spToPx(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static String getProjectIdFromURL(String data) {

        StringBuilder stringBuilder = new StringBuilder(data);
        String keyword = "my-service-request/";
        int start = stringBuilder.indexOf(keyword);
        if (start == -1)
            return null;
        start += keyword.length();

        return stringBuilder.substring(start);
    }

    /**
     * Makes the first letter caps and the rest lowercase.
     *
     * @param data deCapitalize this
     * @return String
     */
    public static String deCapitalize(String data) {

        if (TextUtils.isEmpty(data))
            return null;

        String firstLetter = data.substring(0, 1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public static String deNull(CharSequence text) {
        if (TextUtils.isEmpty(text))
            return "";
        else
            return text.toString();
    }

    public static String getFormattedAddress(BusinessAddress businessAddress) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!TextUtils.isEmpty(businessAddress.getAddress()))
            stringBuilder.append(businessAddress.getAddress());
        if (!TextUtils.isEmpty(businessAddress.getCity()))
            stringBuilder.append(", ").append(businessAddress.getCity());
        if (!TextUtils.isEmpty(businessAddress.getZipcode()))
            stringBuilder.append(" ").append(businessAddress.getZipcode());

        return stringBuilder.toString();
    }

    public static Bitmap getSingleBitmap(Context context, HashMap<Integer, String> hashMap) {
        Bitmap scaledBitmap = null;
        for (Map.Entry<Integer, String> pair : hashMap.entrySet()) {
            String imagePath = pair.getValue();
            Bitmap bitmap = UiUtil.rotateImageIfRequired(imagePath);
            if (bitmap != null)
                scaledBitmap = UiUtil.getResizedBitmap(bitmap, dpToPx(context, 320));
        }
        return scaledBitmap;
    }
}
