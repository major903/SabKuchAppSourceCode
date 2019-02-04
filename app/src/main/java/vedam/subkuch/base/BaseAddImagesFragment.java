package vedam.subkuch.base;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.network.models.Image;
import vedam.subkuch.uicomponent.PickImageDialog;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.helpers.Constants.REQUEST_PICK_IMAGE_FROM_GALLERY;


public abstract class BaseAddImagesFragment extends BaseFragment {

    public int maxImagesAllowed = 5;
    private TextView tvAddPicture;
    private LinearLayout llAddPicture;
    private CardView cvAddPicture;
    private int imageCount;

    /**
     * array list to store image urls
     */
    private List<String> listImageUrls = new ArrayList<>();

    /**
     * The path of image taken by the camera.
     */
    private String imagePath;
    /**
     * hash map to store image ids and it's file path
     */
    private HashMap<Integer, String> hmImageItem = new LinkedHashMap<>();

    protected HashMap<Integer, String> getImageItemMap() {
        return hmImageItem;
    }

    private View.OnLongClickListener imageOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {

            UiUtil.showConfirmationDialog(context, "Are you sure you want to delete this image?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int tag = (int) v.getTag();
                    llAddPicture.removeView(v);
                    hmImageItem.remove(tag);
                    setAddPictureVisibility();
//                    setAddPictureText();
                }
            }, null, true);

            return true;
        }
    };

    private View.OnClickListener imageOnClickListener = view -> {
        int tag = (int) view.getTag();

        ArrayList<Image> alImageUris = new ArrayList<>();
        int selectedPosition = 0;
        for (Map.Entry<Integer, String> entry : hmImageItem.entrySet()) {
            //We assign position here as position is zero based and size returns +1
            if (entry.getKey() == tag)
                selectedPosition = alImageUris.size();
            alImageUris.add(0, new Image(entry.getValue()));
        }
        setGallery(alImageUris, alImageUris.size() - selectedPosition - 1, false);
    };

    protected Response.ErrorListener onErrorListener = error -> {

        LogUtils.LOGD("ERROR", error.getMessage());
        if (getActivity() != null)
            onErrorReceived(error);

    };

    protected void onErrorReceived(VolleyError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(context, getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(context, getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(context, getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError || (error.networkResponse != null &&
                error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED)) {
            logout();
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void parseAndShowError(VolleyError error) {

        UiUtil.showToast(context, getString(R.string.err_occurred));
    }

    protected void setImagesLayout(View view, int maxImagesAllowed) {
        this.maxImagesAllowed = maxImagesAllowed;
        tvAddPicture = view.findViewById(R.id.tv_add_picture);
        setAddPictureText();
        cvAddPicture = view.findViewById(R.id.cv_add_picture);
        llAddPicture = view.findViewById(R.id.ll_add_picture);
        bindCallbacks();
    }


    private void setAddPictureText() {

        if (maxImagesAllowed > 1)
            tvAddPicture.setText(R.string.add_photos);
        else
            tvAddPicture.setText(R.string.add_photo);

//        tvAddPicture.setText(String.format("Attach Photos %d/5", hmImageItem.size()));
    }

    private void bindCallbacks() {
        cvAddPicture.setOnClickListener(view -> dialogBuilderPickImage());
    }

    private void getImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_PICK_IMAGE_FROM_GALLERY);
    }

    protected PickImageDialog dialogBuilderPickImage() {
        PickImageDialog pickImageDialog = new PickImageDialog(context);
        pickImageDialog.setCancelable(true);
        Window window = pickImageDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.getAttributes().windowAnimations = R.style.DialogAnimation;
        pickImageDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                int selected_key = ((PickImageDialog) dialog).getSelected_key();
                switch (selected_key) {
                    case PickImageDialog.KEY_CAMERA:
                        List<String> permissions = new ArrayList<>();
                        permissions.add(Manifest.permission.CAMERA);
                        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (AppUtil.checkPermissions(context, permissions))
                            launchCamera();
                        else
                            getPermissionToAccessUserCamera();
                        break;
                    case PickImageDialog.KEY_GALLERY:
                        List<String> permissions2 = new ArrayList<>();
                        permissions2.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (AppUtil.checkPermissions(context, permissions2))
                            getImageFromGallery();
                        else
                            getPermissionToAccessExternalStorage();
                        break;
                }
            }
        });
        pickImageDialog.show();
        return pickImageDialog;
    }

    //region Helper methods for camera permission
    private void getPermissionToAccessUserCamera() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        int hasCameraAccessPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int hasExternalStorageAccessPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasCameraAccessPermission != PackageManager.PERMISSION_GRANTED || hasExternalStorageAccessPermission != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Tutorial Screen which image and text and button.
                UiUtil.showDialog(context, context.getString(R.string.camera_rationale), (dialog, which) -> requestPermissions(Constants.CAMERA_GALLERY_GROUP_PERMISSION,
                        Constants.PERMISSIONS_REQUEST_CAMERA), true);
                return;
            }
            requestPermissions(Constants.CAMERA_GALLERY_GROUP_PERMISSION,
                    Constants.PERMISSIONS_REQUEST_CAMERA);
        } else {
            //Permission is granted
            launchCamera();
        }

    }

    //region Helper methods for Gallery Permission
    private void getPermissionToAccessExternalStorage() {
        int hasExternalStorageAccessPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasExternalStorageAccessPermission != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Tutorial Screen which image and text and button.
                UiUtil.showDialog(context, context.getString(R.string.external_storage_rationale), (dialog, which) -> requestPermissions(Constants.READ_WRITE_EXTERNAL_GROUP_PERMISSION,
                        Constants.PERMISSIONS_REQUEST_STORAGE), true);
                return;
            }
            requestPermissions(Constants.READ_WRITE_EXTERNAL_GROUP_PERMISSION,
                    Constants.PERMISSIONS_REQUEST_STORAGE);
        } else {
            //Permission is granted
            getImageFromGallery();
        }
    }

    private void showSettingsDialog(String message) {

        UiUtil.showDialog(context, message, (dialogInterface, i) -> AppUtil.openAppSettings(context), true);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            startActivityForResult(takePictureIntent, Constants.REQUEST_PICK_IMAGE_FROM_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CAMERA:
                //Camera and external storage permission check
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted :)
                    launchCamera();
                } else
                    showSettingsDialog(getString(R.string.camera_denied));
                break;
            case Constants.PERMISSIONS_REQUEST_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted :)
                    getImageFromGallery();
                } else
                    showSettingsDialog(getString(R.string.external_storage_denied));
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    private void createImageView(final Bitmap bitmap, final String imageUri) {

        hmImageItem.put(imageCount, imageUri);
        setAddPictureVisibility();
        create(bitmap);
        imageCount++;
    }

    private void create(Bitmap bitmap) {


        if (llAddPicture != null) {
            RoundedImageView imageView = new RoundedImageView(context);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(cvAddPicture.getWidth(), cvAddPicture.getHeight());
            param.setMargins(AppUtil.dpToPx(context, 8), 0, 0, 0);
            param.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(param);
            imageView.setCornerRadius(AppUtil.dpToPx(context, 8));
            imageView.setImageBitmap(bitmap);
            imageView.setTag(imageCount);
            imageView.setOnClickListener(imageOnClickListener);
            imageView.setOnLongClickListener(imageOnLongClickListener);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            llAddPicture.addView(imageView, 1);
        }

    }

    /**
     * Sets the the visibility of AddPicture card according to the number of image items
     */
    private void setAddPictureVisibility() {
        if (hmImageItem.size() == maxImagesAllowed) {

            if (cvAddPicture != null)
                cvAddPicture.setVisibility(View.GONE);
        } else {
            if (cvAddPicture != null)
                cvAddPicture.setVisibility(View.VISIBLE);
        }
    }

    /*protected void uploadImage() {
        listImageUrls.clear();

        for (Map.Entry<Integer, String> pair : hmImageItem.entrySet()) {
            String imagePath = pair.getValue();

            startImageUpload(imagePath);
        }
    }


    private void startImageUpload(String imagePath) {

        Handler handler = new Handler(Looper.getMainLooper());

        ImageUploadResultReceiver mResultReceiver = new ImageUploadResultReceiver(handler, this);

        Intent intent = new Intent(context, ImageUploadIntentService.class);

        intent.putExtra(Constants.EXTRA_RECEIVER, mResultReceiver);
        intent.putExtra(Constants.EXTRA_IMAGE_PATH, imagePath);

        if (getScreenChangeListener() != null) {
            getScreenChangeListener().handleServiceIntent(intent);
        }

    }

    @Override
    public void onImageUploaded(String imageUrl) {
        if (imageUrl == null) {
            handleError();
            return;
        }
        listImageUrls.add(imageUrl);

        if (listImageUrls.size() == hmImageItem.size()) {
            String formattedUrls = TextUtils.join(",", listImageUrls);
            onAllImagesUploaded(formattedUrls);
        }
    }

    private void handleError() {
        listImageUrls.clear();
        UiUtil.cancelProgressDialog();
        UiUtil.showToast(context, getString(R.string.err_unknown));
    }*/
}
