package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetDoshamBean;

public interface GetDoshamHandler extends BaseHandler {
    void onSuccess(GetDoshamBean response);
}
