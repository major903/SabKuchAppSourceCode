package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.UserDetail.GetUserDetailResponse;

public interface GetUserDetailHandler extends BaseHandler {
    void onSuccess(GetUserDetailResponse response);
}
