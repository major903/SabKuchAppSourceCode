package vedam.subkuch.ui.cropImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.naver.android.helloyako.imagecrop.view.ImageCropView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.handler.AllLocalHandler;

public class CropImageActivity extends BaseActivity implements AllLocalHandler {

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
        String imageUri = getIntent().getStringExtra("image");
        // Bitmap newBitMap = FilesFunctions.changeImageOrientation(imageUri, singleBitmapDetail.getBitMap());
        imageViewCrop.setImageFilePath(imageUri);
        imageViewCrop.setAspectRatio(6, 5);
    }


    @OnClick(R.id.buttonCrop)
    public void buttonCropClick(View view) {
        Bitmap bitMap = imageViewCrop.getCroppedImage();
        singleBitmapDetail.setBitMap(bitMap);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @OnClick(R.id.buttonDiscard)
    public void buttonDiscardClick() {
        finish();
    }
}
