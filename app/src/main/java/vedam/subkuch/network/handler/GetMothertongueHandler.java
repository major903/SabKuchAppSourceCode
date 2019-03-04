package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetMothertongueBean;

public interface GetMothertongueHandler extends BaseHandler {
    void onSuccess(GetMothertongueBean response);
}
