package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.getLiving.GetLivingResponse;

public interface GetLivingHandler extends BaseHandler {
    void onSuccess(GetLivingResponse response);
}
