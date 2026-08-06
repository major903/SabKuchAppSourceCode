package vedam.subkuch.network.models;

/** Response returned by POST /api/Users/update. */
public class UpdateUserResponse {

    private int status;
    private String message;
    private int userId;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }
}
