package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

/** Response returned by POST /api/UserProfile/EditProfile. */
public class UpdateUserResponse {

    @SerializedName(value = "status", alternate = {"Status", "ReturnCode"})
    private int status;
    @SerializedName(value = "message", alternate = {"Message", "ReturnMessage"})
    private String message;
    private int userId;
    @SerializedName(value = "ProfileId", alternate = {"profileId"})
    private int profileId;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    /** Profile edit endpoints may return either a status response or updated profile data. */
    public boolean isSuccess() {
        return (status == 1 && "Success".equalsIgnoreCase(message)) || profileId > 0;
    }
}
