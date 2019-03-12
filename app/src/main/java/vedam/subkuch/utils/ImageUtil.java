package vedam.subkuch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static boolean saveToInternalStorage(Context context, Bitmap bitmapImage, String imageName) {
        boolean success = true;
        String path = context.getFilesDir() + File.separator + imageName;
        LogUtils.LOGD("path of saving", path);

        File file = new File(context.getFilesDir() + File.separator + imageName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    public static Bitmap getBitmapFromInternalStorage(Context context, String imageName) {
        // File rootDirectory = createRootDirectoryForServiceLive();
        // File file = new File(rootDirectory, imageName);
        File file = new File(context.getFilesDir() + File.separator + imageName);
        return convertFileToBitmap(file.getAbsoluteFile());

    }

    private static Bitmap convertFileToBitmap(File para_ImageFile) {
        if (para_ImageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(para_ImageFile.getAbsolutePath());
            if (myBitmap != null) return myBitmap;
            else return null;
        } else return null;
    }

}
