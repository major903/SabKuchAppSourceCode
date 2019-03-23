package vedam.subkuch.ui.cropImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.utils.ImageUtil;

public class CropImageActivity extends BaseActivity {

    @BindView(R.id.imageViewCrop)
    ImageCropView imageViewCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.bind(this);
        setData();
    }

    private void setData() {
        String imageUri = getIntent().getStringExtra(Constants.EXTRA_IMAGE_URI);
        // Bitmap newBitMap = FilesFunctions.changeImageOrientation(imageUri, singleBitmapDetail.getBitMap());
        imageViewCrop.setImageFilePath(imageUri);
        imageViewCrop.setAspectRatio(6, 6);
    }


    @OnClick(R.id.buttonCrop)
    public void buttonCropClick(View view) {
        if (!imageViewCrop.isChangingScale()) {
            Bitmap b = imageViewCrop.getCroppedImage();
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

    @OnClick(R.id.buttonDiscard)
    public void buttonDiscardClick() {
        finish();
    }
}
