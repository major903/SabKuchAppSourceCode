package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.insertImage.InsertImageResponse;

public interface InsertImageHandler extends BaseHandler {
    void onSuccess(InsertImageResponse response);
}
