package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.getMasterCast.GetMasterCastResponse;

public interface GetMasterCastHandler extends BaseHandler {
     void onSuccess(GetMasterCastResponse response);
}
