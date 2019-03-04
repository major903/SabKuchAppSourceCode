package vedam.subkuch.network.handler;


import vedam.subkuch.network.models.updateMatrimonial.UpdateMatrimonialResponse;

public interface UpdateMaterimonialHandler extends BaseHandler {
    void onSuccess(UpdateMatrimonialResponse response);
}
