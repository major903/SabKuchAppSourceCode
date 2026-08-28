package vedam.subkuch.base;

import static vedam.subkuch.base.BaseActivity.TAG;
import static vedam.subkuch.helpers.Constants.REQUEST_PICK_IMAGE_FROM_GALLERY;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import vedam.subkuch.network.AuthFailureError;
import vedam.subkuch.network.NetworkError;
import vedam.subkuch.network.NetworkResponse;
import vedam.subkuch.network.ParseError;
import vedam.subkuch.network.Response;
import vedam.subkuch.network.TimeoutError;
import vedam.subkuch.network.ApiError;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

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
import vedam.subkuch.network.models.ErrorResponse;
import vedam.subkuch.network.models.Image;
import vedam.subkuch.uicomponent.PickImageDialog;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.LogUtils;
import vedam.subkuch.utils.UiUtil;


public abstract class BaseAddImagesFragment extends BaseFragment {

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleImageResult(result.getResultCode(), result.getData()));

    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    launchCamera();
                } else {
                    showSettingsDialog(getString(R.string.camera_denied));
                }
            });

    private int maxImagesAllowed = 5;
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

            UiUtil.showConfirmationDialog(mContext, "Are you sure you want to delete this image?", new DialogInterface.OnClickListener() {
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

    protected void onErrorReceived(ApiError error) {

        if (error instanceof NetworkError) {
            UiUtil.showToast(mContext, getString(R.string.connectionError));
        } else if (error instanceof TimeoutError) {
            UiUtil.showToast(mContext, getString(R.string.timeoutError));
        } else if (error instanceof ParseError) {
            UiUtil.showToast(mContext, getString(R.string.err_parsing));
        } else if (error instanceof AuthFailureError || (error.networkResponse != null &&
                error.networkResponse.statusCode == NetworkConstants.CODE_UNAUTHORIZED)) {
            logout();
        } else {
            parseAndShowError(error);
        }
        UiUtil.cancelProgressDialog();
    }

    protected void parseAndShowError(ApiError error) {

        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            String response = new String(networkResponse.data);
            LogUtils.LOGE(TAG, "response error:" + response);

            try {
                ErrorResponse errorResponse = new Gson().fromJson(response, ErrorResponse.class);

                if (!TextUtils.isEmpty(errorResponse.getReturnMessage()))
                    UiUtil.showToast(mContext, errorResponse.getReturnMessage());
                else if (!TextUtils.isEmpty(errorResponse.getMessage()))
                    UiUtil.showToast(mContext, errorResponse.getMessage());
                else
                    UiUtil.showToast(mContext, getString(R.string.err_occurred));
            } catch (Exception exception) {
                FirebaseCrashlytics.getInstance().recordException(exception);
                exception.printStackTrace();
                UiUtil.showToast(mContext, getString(R.string.err_occurred));
            }
        } else {
            UiUtil.showToast(mContext, getString(R.string.err_unknown));
        }
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
        imagePickerLauncher.launch(Intent.createChooser(intent, getString(R.string.select_picture)));
    }

    protected void dialogBuilderPickImage() {
        PickImageDialog pickImageDialog = new PickImageDialog(mContext);
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
                        if (AppUtil.checkPermissions(mContext, permissions))
                            launchCamera();
                        else
                            requestCameraPermission();
                        break;
                    case PickImageDialog.KEY_GALLERY:
                        getImageFromGallery();
                        break;
                }
            }
        });
        pickImageDialog.show();
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            UiUtil.showDialog(mContext, mContext.getString(R.string.camera_rationale),
                    (dialog, which) -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA), true);
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showSettingsDialog(String message) {

        UiUtil.showDialog(mContext, message, (dialogInterface, i) -> AppUtil.openAppSettings(mContext), true);
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            imagePickerLauncher.launch(takePictureIntent);
        }
    }


    private Uri setImageUri() {
        // Store image in dcim
        File file;
        try {
            file = File.createTempFile(String.format("image%s", new Date().getTime()), ".jpg", mContext.getExternalCacheDir());
            Uri imgUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            this.imagePath = file.getAbsolutePath();
            return imgUri;
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        return null;
    }

    private void handleImageResult(int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                //Getting the Bitmap from Gallery
                if (uri.getScheme().equals("content")) {
                    InputStream is = mContext.getContentResolver().openInputStream(uri);
                    File file = File.createTempFile(String.format("image%s", new Date().getTime()), ".jpg", mContext.getExternalCacheDir());
                    copyInputStreamToFile(is, file);

                    String filePath = file.getAbsolutePath();

                    Bitmap rotatedImage = UiUtil.rotateImageIfRequired(filePath);
                    Bitmap thumbnailBitmap = UiUtil.getThumbnail(mContext, rotatedImage);

                    //Setting the Bitmap to ImageView
                    createImageView(thumbnailBitmap, filePath);
                } else {
                    UiUtil.showToast(mContext, getString(R.string.cannot_get_image));
                    noImageAdded();
                }
            } catch (IOException e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
                noImageAdded();
            }
        } else if (resultCode == Activity.RESULT_OK) {
            try {
                Bitmap rotatedImage = UiUtil.rotateImageIfRequired(imagePath);
                Bitmap decodedBitmap = UiUtil.getThumbnail(mContext, rotatedImage);
                if (decodedBitmap != null) {
                    createImageView(decodedBitmap, imagePath);
                } else {
                    noImageAdded();
                }
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
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
            FirebaseCrashlytics.getInstance().recordException(e);
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
            ShapeableImageView imageView = new ShapeableImageView(mContext);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(cvAddPicture.getWidth(), cvAddPicture.getHeight());
            param.setMargins(AppUtil.dpToPx(mContext, 8), 0, 0, 0);
            param.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(param);
            imageView.setShapeAppearanceModel(ShapeAppearanceModel.builder()
                    .setAllCorners(CornerFamily.ROUNDED, AppUtil.dpToPx(mContext, 8))
                    .build());
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
