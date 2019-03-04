package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetBodyTypeBean;

public interface GetBodyTypeHandler  extends BaseHandler {
    void onSuccess(GetBodyTypeBean response);
}