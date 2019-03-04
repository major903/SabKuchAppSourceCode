package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetOccupationBean;

public interface GetOccupationHandler extends BaseHandler {
    void onSuccess(GetOccupationBean response);
}
