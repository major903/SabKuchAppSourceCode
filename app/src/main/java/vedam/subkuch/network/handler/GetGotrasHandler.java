package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetGotrasBean;

public interface GetGotrasHandler extends BaseHandler {
    void onSuccess(GetGotrasBean response);
}
