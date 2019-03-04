package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.deleteImage.DeleteImageResponse;

public interface DeleteImageHandler extends BaseHandler {
    void onSuccess(DeleteImageResponse response);
}
