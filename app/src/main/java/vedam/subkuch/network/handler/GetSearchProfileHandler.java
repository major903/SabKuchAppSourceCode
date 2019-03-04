package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.searchProfile.SearchProfile;

public interface GetSearchProfileHandler extends BaseHandler {
    void onSuccess(SearchProfile response);
}
