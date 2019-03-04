package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetComplexionBean;

public interface GetComplexionHandler extends BaseHandler {
    void onSuccess(GetComplexionBean response);
}
