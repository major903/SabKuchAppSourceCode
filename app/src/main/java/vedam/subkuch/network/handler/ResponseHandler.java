package vedam.subkuch.network.handler;

public interface ResponseHandler<T> extends BaseHandler {
     void onSuccess(T response);
}
