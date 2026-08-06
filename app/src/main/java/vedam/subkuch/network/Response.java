package vedam.subkuch.network;

/** Lightweight callback types used by the Retrofit network layer. */
public final class Response<T> {
    private Response() {
    }

    public interface Listener<T> {
        void onResponse(T response);
    }

    public interface ErrorListener {
        void onErrorResponse(ApiError error);
    }
}
