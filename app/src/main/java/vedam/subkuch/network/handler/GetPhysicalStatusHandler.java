package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetPhysicalStatusBean;

public interface GetPhysicalStatusHandler extends BaseHandler {
    void onSuccess(GetPhysicalStatusBean response);
}

