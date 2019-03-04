package vedam.subkuch.network.handler;

import vedam.subkuch.network.models.GetAllCountries;

public interface GetCountryHandler extends BaseHandler {
    void onSuccess(GetAllCountries response);
}
