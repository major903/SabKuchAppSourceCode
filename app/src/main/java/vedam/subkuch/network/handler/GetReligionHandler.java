package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.getReligion.GetReligionResponse;

public interface GetReligionHandler extends BaseHandler {
    void onSuccess(GetReligionResponse response);
}
