package vedam.subkuch.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import vedam.subkuch.R
import vedam.subkuch.ui.shopping.ShareUtilsListener
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
            packageName: String?,
            listener: ShareUtilsListener
    ) {
        listener.onShareStarted()
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        try {
                            val cachePath = File(context.getExternalFilesDir("pics"), "images")
                            cachePath.mkdirs() // don't forget to make the directory
                            val stream =
                                    FileOutputStream("$cachePath/image.png") // overwrites this image every time
                            bitmap.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    90,
                                    stream
                            )
                            stream.flush()
                            stream.close()
                            val imagePath = File(context.getExternalFilesDir("pics"), "images")
                            val newFile = File(imagePath, "image.png")
                            val contentUri = FileProvider.getUriForFile(
                                    context,
                                    context.packageName + ".provider",
                                    newFile
                            )
                            if (contentUri != null) {
                                val shareIntent = Intent()
                                shareIntent.action = Intent.ACTION_SEND
                                shareIntent.putExtra(Intent.EXTRA_TEXT, "$url\n\n$shareMessage")
//                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
//                                shareIntent.setDataAndType(
//                                        contentUri,
//                                        context.contentResolver.getType(contentUri)
//                                )
//                                shareIntent.type = "image/*"
//                                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                                shareIntent.type = "text/plain"
                                packageName?.let {
                                    shareIntent.setPackage(packageName)
                                }
                                context.startActivity(shareIntent)
                                listener.onShared()
                            }
                        } catch (e: ActivityNotFoundException) {
                            listener.onTargetAppNotInstalledError()
                            LogUtils.LOGD("TAG", e.message)
//                    Timber.e("Whatsapp not installed: %s", e.message)
                        } catch (e: Exception) {
                            listener.onShareError(e)
                            LogUtils.LOGD("TAG", e.message)
//                    Timber.e("Share message error: %s", e.message)
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
//        ImageDownloader(context, object : ImageDownloader.GlideImageLoaderListener {
//            override fun onProgress(progress: Int) {}
//            override fun onDownloaded(bitmap: Bitmap) {
//
//            }
//
//            override fun onError(e: GlideException?) {
//                listener.onShareError(e)
//                LogUtils.LOGD("TAG", e?.message)
//            }
//        }).load(url)
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    fun showSettingsDialog(context: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.dialog_permission_title))
        builder.setMessage(context.getString(R.string.dialog_permission_message))
        builder.setPositiveButton(
                context.getString(R.string.go_to_settings)
        ) { dialog: DialogInterface, which: Int ->
            dialog.cancel()
            openSettings(context)
        }
        builder.setNegativeButton(
                context.getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings(context: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivityForResult(intent, 101)
    }
}