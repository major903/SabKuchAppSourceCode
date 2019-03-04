package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.getPreferencesResponse.GetPreferenceResponse;

public interface GetPreferenceHandler extends BaseHandler {
    void onSuccess(GetPreferenceResponse response);
}
