package vedam.subkuch.network;

public final class AuthFailureError extends ApiError {
    public AuthFailureError() {
        super("Authentication failed");
    }
}
