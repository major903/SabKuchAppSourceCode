package vedam.subkuch.network;

/** App-owned network error shared by Retrofit callbacks and UI error handling. */
public class ApiError extends Exception {
    public NetworkResponse networkResponse;

    public ApiError() {
        super();
    }

    public ApiError(String message) {
        super(message);
    }

    public ApiError(Throwable cause) {
        super(cause);
    }

    public ApiError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiError(NetworkResponse response) {
        super("HTTP " + response.statusCode);
        this.networkResponse = response;
    }
}
