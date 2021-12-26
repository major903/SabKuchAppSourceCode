package vedam.subkuch.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.load.engine.GlideException
import java.io.File
import java.io.FileOutputStream

object ShareUtils {
    @JvmStatic
    fun shareMessage(context: Context, message: String, packageName: String?) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"

            if (!packageName.isNullOrBlank()) {
                setPackage(packageName)
            }
        }
        try {
            context.startActivity(Intent.createChooser(sendIntent, "Choose one"))
        } catch (e: Exception) {
            Toast.makeText(context, "Whatsapp Not Installed", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Sharing an image with pre-composed message
     * */
    fun shareImageWithMessage(
            context: Context,
            url: String?,
            shareMessage: String?,
            packageName: String?
    ) {
        ImageDownloader(context, object : ImageDownloader.GlideImageLoaderListener {
            override fun onProgress(progress: Int) {}
            override fun onDownloaded(bitmap: Bitmap) {
                try {
                    val cachePath = File(context.cacheDir, "images")
                    cachePath.mkdirs() // don't forget to make the directory
                    val stream =
                            FileOutputStream("$cachePath/image.png") // overwrites this image every time
                    bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            90,
                            stream
                    )
                    stream.close()
                    val imagePath = File(context.cacheDir, "images")
                    val newFile = File(imagePath, "image.png")
                    val contentUri = FileProvider.getUriForFile(
                            context,
                            context.packageName + ".fileprovider",
                            newFile
                    )
                    if (contentUri != null) {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                        shareIntent.setDataAndType(
                                contentUri,
                                context.contentResolver.getType(contentUri)
                        )
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                        packageName?.let {
                            shareIntent.setPackage(packageName)
                        }
                        context.startActivity(shareIntent)
//                        listener.onShared()
                    }
                } catch (e: ActivityNotFoundException) {
//                    Timber.e("Whatsapp not installed: %s", e.message)
                } catch (e: Exception) {
//                    Timber.e("Share message error: %s", e.message)
                }
            }

            override fun onError(e: GlideException?) {
            }
        }).load(url)
    }
}