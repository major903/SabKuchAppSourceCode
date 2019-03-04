package vedam.subkuch.network.handler;


import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;

public interface UpdatePreferencesHandler extends BaseHandler {
    void onSuccess(UpdateMatrimonialResponse response);
}
