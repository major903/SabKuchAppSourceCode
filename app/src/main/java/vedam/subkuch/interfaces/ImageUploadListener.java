package vedam.subkuch.interfaces;

/**
 * Created by nansari on 7/6/2016.
 * For project RelayServices
 */
public interface ImageUploadListener {

    /**
     * Called when image is uploaded. This method is called on UI thread.
     *
     * @param imageUrl the image url which you get after uploading image
     */
    void onImageUploaded(String imageUrl);
}
