package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetNakshatrasBean;

public interface GetNakshatrasHandler extends BaseHandler {
    void onSuccess(GetNakshatrasBean response);
}