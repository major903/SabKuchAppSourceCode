package vedam.subkuch.network;

import java.util.Map;

/** HTTP response details exposed to the app's shared error UI. */
public final class NetworkResponse {
    public final int statusCode;
    public final byte[] data;
    public final Map<String, String> headers;
    public final boolean notModified;

    public NetworkResponse(int statusCode, byte[] data) {
        this(statusCode, data, null, false);
    }

    public NetworkResponse(int statusCode, byte[] data, Map<String, String> headers,
                           boolean notModified) {
        this.statusCode = statusCode;
        this.data = data;
        this.headers = headers;
        this.notModified = notModified;
    }
}
