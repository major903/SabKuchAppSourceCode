package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetCityResponse;

public interface GetAllCityHandler extends BaseHandler {
    void onSuccess(GetCityResponse response);
}
