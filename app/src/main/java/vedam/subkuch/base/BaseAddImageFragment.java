package vedam.subkuch.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.uicomponent.PickImageDialog;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.UiUtil;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static vedam.subkuch.helpers.Constants.PERMISSIONS_REQUEST_CAMERA;
import static vedam.subkuch.helpers.Constants.PERMISSIONS_REQUEST_STORAGE;
import static vedam.subkuch.helpers.Constants.REQUEST_PICK_IMAGE_FROM_GALLERY;

public abstract class BaseAddImageFragment extends BaseFragment {

    protected RoundedImageView ivPicture;
    private LinearLayout llEditPicture;
//      The path of image taken by the camera.

    private String imagePath;
    private String imageUri;

    protected void setImagesLayout(View view) {

        ivPicture = view.findViewById(R.id.iv_picture);
        llEditPicture = view.findViewById(R.id.ll_edit_picture);
        bindCallbacks();
    }

    private void bindCallbacks() {
        llEditPicture.setOnClickListener(view -> dialogBuilderPickImage());
    }

    private void getImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_PICK_IMAGE_FROM_GALLERY);
    }

    protected void dialogBuilderPickImage() {
        PickImageDialog pickImageDialog = new PickImageDialog(context);
        pickImageDialog.setCancelable(true);
        Window window = pickImageDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        pickImageDialog.setOnDismissListener(dialog -> {
            int selected_key = ((PickImageDialog) dialog).getSelected_key();
            switch (selected_key) {
                case PickImageDialog.KEY_CAMERA:
                    List<String> permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CAMERA);
                    permissions.add(WRITE_EXTERNAL_STORAGE);
                    if (AppUtil.checkPermissions(context, permissions))
                        launchCamera();
                    else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_CAMERA);

                    }
//                            showSettingsDialog("Your device does not give permission to use camera features. Please enable permission and try again.");
                    break;
                case PickImageDialog.KEY_GALLERY:
                    List<String> permissions2 = new ArrayList<>();
                    permissions2.add(WRITE_EXTERNAL_STORAGE);
                    if (AppUtil.checkPermissions(context, permissions2))
                        getImageFromGallery();
                    else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSIONS_REQUEST_STORAGE);
                    }
//                            showSettingsDialog("Your device does not give permission to use device media. Please enable permission and try again.");
                    break;
            }
        });
        pickImageDialog.show();
    }

    private void showSettingsDialog(String message) {

        UiUtil.showDialog(context, message, (dialogInterface, i) -> AppUtil.openAppSettings(context), false);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            startActivityForResult(takePictureIntent, Constants.REQUEST_PICK_IMAGE_FROM_CAMERA);
        }
    }

    private Uri setImageUri() {
        // Store image in dcim
        File file;
        try {
            file = File.createTempFile(String.format("image%s", new Date().getTime()), ".jpg", context.getExternalCacheDir());
            Uri imgUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            this.imagePath = file.getAbsolutePath();
            return imgUri;
        } catch (IOException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                //Getting the Bitmap from Gallery
                if (uri.getScheme().equals("content")) {
                    InputStream is = context.getContentResolver().openInputStream(uri);
                    File file = File.createTempFile(String.format("image%s", new Date().getTime()), ".jpg", context.getExternalCacheDir());
                    copyInputStreamToFile(is, file);

                    String filePath = file.getAbsolutePath();

                    Bitmap rotatedImage = UiUtil.rotateImageIfRequired(filePath);
                    Bitmap thumbnailBitmap = UiUtil.getThumbnail(context, rotatedImage);

                    //Setting the Bitmap to ImageView
                    createImageView(thumbnailBitmap, filePath);
                } else {
                    UiUtil.showToast(context, getString(R.string.cannot_get_image));
                    noImageAdded();
                }
            } catch (IOException e) {
                Crashlytics.logException(e);
                e.printStackTrace();
                noImageAdded();
            }
        } else if (requestCode == Constants.REQUEST_PICK_IMAGE_FROM_CAMERA) {
            try {
                Bitmap rotatedImage = UiUtil.rotateImageIfRequired(imagePath);
                Bitmap decodedBitmap = UiUtil.getThumbnail(context, rotatedImage);
                if (decodedBitmap != null) {
                    createImageView(decodedBitmap, imagePath);
                } else {
                    noImageAdded();
                }
            } catch (Exception e) {
                Crashlytics.logException(e);
                e.printStackTrace();
                noImageAdded();
            }
        } else {
            noImageAdded();
        }
    }

    protected void noImageAdded() {

    }

    private void createImageView(final Bitmap bitmap, final String imageUri) {

        this.imageUri = imageUri;
        ivPicture.setImageBitmap(bitmap);

    }

    private void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else
                    showSettingsDialog("Your device does not give permission to use device media. Please enable permission and try again.");
                break;
            case PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                } else {
                    showSettingsDialog("Your device does not give permission to use device media. Please enable permission and try again.");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

