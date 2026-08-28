package vedam.subkuch.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vedam.subkuch.helpers.Constants;

public class FilesFunctions {
    public static File createImageFile(Context context) {
        String imageFileName = Constants.APP_NAME + "_" + System.currentTimeMillis();
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir == null) {
            return null;
        }
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public static File createFileFromBitMap(Context context, Bitmap bitmap) {
        String videoFileName = Constants.APP_NAME + "-" + System.currentTimeMillis() + ".jpg";
        File picturesDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (picturesDirectory == null) {
            return null;
        }
        File myDirectory = new File(picturesDirectory, "SubKuch");
        if (!myDirectory.exists() && !myDirectory.mkdirs()) {
            return null;
        }
        File file = new File(myDirectory, videoFileName);

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bitmapData);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getUriStringFromData(Intent data) {
        Uri selectedImageUri = data == null ? null : data.getData();
        return selectedImageUri == null ? null : selectedImageUri.toString();
    }

    public static Bitmap changeImageOrientation(String photoPath, Bitmap bitmap) {
        ExifInterface  ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ei == null) {
            return bitmap;
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, Float.parseFloat("90"));
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, Float.parseFloat("180"));
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, Float.parseFloat("270"));
                break;

            case ExifInterface.ORIENTATION_NORMAL:
                rotatedBitmap = bitmap;
                break;
            default: {
                rotatedBitmap = bitmap;
            }

        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, Float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
