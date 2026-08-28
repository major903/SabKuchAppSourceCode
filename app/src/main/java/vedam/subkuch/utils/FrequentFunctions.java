package vedam.subkuch.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FrequentFunctions {
    public static void hideKeyBoard(Context activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static String convertBitmapToBase64(Bitmap bitmap) {
        Bitmap bitmap1=Bitmap.createScaledBitmap(bitmap,500,500,true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static String getCurrentDateTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}
