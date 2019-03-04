package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetMaritalStatusResponse;

public interface GetMaritalStatusHandler extends BaseHandler{
    void onSuccess(GetMaritalStatusResponse response);
}
