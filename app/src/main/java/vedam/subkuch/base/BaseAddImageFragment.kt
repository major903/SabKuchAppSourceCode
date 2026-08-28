package vedam.subkuch.base

import android.Manifest.permission
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.android.material.imageview.ShapeableImageView
import vedam.subkuch.R
import vedam.subkuch.helpers.Constants
import vedam.subkuch.ui.cropImage.CropImageActivity
import vedam.subkuch.uicomponent.PickImageDialog
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.ImageUtil
import vedam.subkuch.utils.UiUtil
import java.io.*
import java.util.*

abstract class BaseAddImageFragment : BaseFragment() {
    @JvmField
    protected var ivPicture: ShapeableImageView? = null
    private var llEditPicture: LinearLayout? = null

    //      The path of image taken by the camera.
    private var imagePath: String? = null
    var imageUri: String? = null
        private set

    private val openChooseImage =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                try {
                    //Getting the Bitmap from Gallery
                    if ("content" == result?.scheme) {
                        val `is` = mContext?.contentResolver?.openInputStream(result)
                        val file = File.createTempFile(String.format("image%s", Date().time), ".jpg", mContext?.externalCacheDir)
                        copyInputStreamToFile(`is`, file)
                        val filePath = file.absolutePath

//                    Bitmap rotatedImage = UiUtil.rotateImageIfRequired(filePath);
//                    Bitmap thumbnailBitmap = UiUtil.getThumbnail(context, rotatedImage);

                        //Setting the Bitmap to ImageView
                        startCrop(filePath)
                    } else {
                        UiUtil.showToast(mContext, getString(R.string.cannot_get_image))
                        noImageAdded()
                    }
                } catch (e: IOException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                    e.printStackTrace()
                    noImageAdded()
                }
            }

    private val cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) launchCamera()
                else showSettingsDialog("Your device does not give permission to use the camera. Please enable permission and try again.")
            }

    private val takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
                if (saved) handleCameraResult() else noImageAdded()
            }

    private val cropImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val fileName = result.data?.getStringExtra(Constants.EXTRA_FILE_NAME)
                    if (fileName != null) {
                        val bitmap = ImageUtil.getBitmapFromInternalStorage(mContext, fileName)
                        setImageView(bitmap, getImageUriFromFileName(fileName))
                    } else {
                        noImageAdded()
                    }
                } else {
                    noImageAdded()
                }
            }

    protected fun setImagesLayout(view: View) {
        ivPicture = view.findViewById(R.id.iv_picture)
        llEditPicture = view.findViewById(R.id.ll_edit_picture)
        bindCallbacks()
    }

    private fun bindCallbacks() {
        ivPicture?.setOnClickListener { dialogBuilderPickImage() }
        llEditPicture?.setOnClickListener { dialogBuilderPickImage() }
    }

    private fun getImageFromGallery() {
        openChooseImage.launch("image/*")
    }

    protected fun dialogBuilderPickImage() {
        val pickImageDialog = PickImageDialog(mContext)
        pickImageDialog.setCancelable(true)
        val window = pickImageDialog.window
        window!!.setGravity(Gravity.BOTTOM)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window.attributes.windowAnimations = R.style.DialogAnimation
        pickImageDialog.setOnDismissListener { dialog: DialogInterface ->
            val selected_key = (dialog as PickImageDialog).selected_key
            when (selected_key) {
                PickImageDialog.KEY_CAMERA -> {
                    val permissions: MutableList<String> = ArrayList()
                    permissions.add(permission.CAMERA)
                    if (AppUtil.checkPermissions(mContext, permissions)) launchCamera() else {
                        cameraPermissionLauncher.launch(permission.CAMERA)
                    }
                }
                PickImageDialog.KEY_GALLERY -> {
                    getImageFromGallery()
                }
            }
        }
        pickImageDialog.show()
    }

    private fun showSettingsDialog(message: String) {
        UiUtil.showDialog(mContext, message, { _: DialogInterface?, i: Int -> AppUtil.openAppSettings(mContext) }, false)
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            setImageUri()?.let { takePictureLauncher.launch(it) }
        }
    }

    private fun setImageUri(): Uri? {
        // Store image in dcim
        val file: File
        try {
            file = File.createTempFile(String.format("image%s", Date().time), ".jpg", mContext?.externalCacheDir)
            val imgUri = FileProvider.getUriForFile(requireContext(), mContext?.applicationContext?.packageName + ".provider", file)
            imagePath = file.absolutePath
            return imgUri
        } catch (e: IOException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            e.printStackTrace()
        }
        return null
    }

    private fun handleCameraResult() {
        try {
            val rotatedImage = UiUtil.rotateImageIfRequired(imagePath)
            val decodedBitmap = UiUtil.getThumbnail(mContext, rotatedImage)
            if (decodedBitmap != null) {
                startCrop(imagePath)
            } else {
                noImageAdded()
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            e.printStackTrace()
            noImageAdded()
        }
    }

    private fun getImageUriFromFileName(fileName: String?): String {
        val file = File(mContext?.filesDir.toString() + File.separator + fileName)
        return file.absolutePath
    }

    protected fun noImageAdded() {}
    private fun setImageView(bitmap: Bitmap, imageUri: String) {
        this.imageUri = imageUri
        ivPicture!!.setImageBitmap(bitmap)
    }

    private fun startCrop(imageUri: String?) {
        if (imageUri == null) return
        val intent = Intent(mContext, CropImageActivity::class.java)
        intent.putExtra(Constants.EXTRA_IMAGE_URI, imageUri)
        cropImageLauncher.launch(intent)
    }

    private fun copyInputStreamToFile(`in`: InputStream?, file: File) {
        try {
            val out: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (`in`!!.read(buf).also { len = it } > 0) {
                out.write(buf, 0, len)
            }
            out.close()
            `in`.close()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            e.printStackTrace()
        }
    }

}
