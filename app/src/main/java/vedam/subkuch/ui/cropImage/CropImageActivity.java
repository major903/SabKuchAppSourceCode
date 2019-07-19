package vedam.subkuch.ui.cropImage;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityCropImageBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.ImageUtil;

public class CropImageActivity extends BaseActivity {

    private ActivityCropImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop_image);
        binding.setCropImageActivity(this);
        setData();
    }

    private void setData() {
        String imageUri = getIntent().getStringExtra(Constants.EXTRA_IMAGE_URI);
        // Bitmap newBitMap = FilesFunctions.changeImageOrientation(imageUri, singleBitmapDetail.getBitMap());
        binding.imageViewCrop.setImageFilePath(imageUri);
        binding.imageViewCrop.setAspectRatio(6, 6);
    }


    public void buttonCropClick(View view) {
        if (!binding.imageViewCrop.isChangingScale()) {
            Bitmap b = binding.imageViewCrop.getCroppedImage();
            if (b != null) {
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Calendar.getInstance().getTime());
                ImageUtil.saveToInternalStorage(this, b, fileName);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Constants.EXTRA_FILE_NAME, fileName);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(CropImageActivity.this, R.string.fail_to_crop, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void buttonDiscardClick(View view) {
        finish();
    }
}
