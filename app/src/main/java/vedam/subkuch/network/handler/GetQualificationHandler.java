package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetQualificationBean;

public interface GetQualificationHandler extends BaseHandler {
    void onSuccess(GetQualificationBean response);
}
