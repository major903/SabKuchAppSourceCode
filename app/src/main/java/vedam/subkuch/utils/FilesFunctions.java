package vedam.subkuch.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import vedam.subkuch.helpers.Constants;

public class FilesFunctions {
    public static File createImageFile() {
        String imageFileName = Constants.APP_NAME + "_" + System.currentTimeMillis();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public static File createFileFromBitMap(Bitmap bitmap) {
        String videoFileName = Constants.APP_NAME + "-" + System.currentTimeMillis() + ".jpg";
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "SubKuch");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        File file = new File(myDirectory, videoFileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static String getPathFromData(Context context, Intent data) {
        Uri selectedImageUri = data.getData();
        String[] filePath = new String[]{MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(selectedImageUri, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        return c.getString(columnIndex);
    }

    public static Bitmap changeImageOrientation(String photoPath, Bitmap bitmap) {
        ExifInterface  ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
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
