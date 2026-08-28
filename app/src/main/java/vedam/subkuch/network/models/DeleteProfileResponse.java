package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/** Handles the response shapes used by both the legacy and updated APIs. */
public final class DeleteProfileResponse {

    @SerializedName(value = "ReturnCode", alternate = {"returnCode"})
    private Integer returnCode;

    @SerializedName(value = "ReturnMessage", alternate = {"returnMessage"})
    private String returnMessage;

    @SerializedName(value = "success", alternate = {
            "Success", "status", "Status", "IsDeleted", "isDeleted"
    })
    private Boolean success;

    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    public String getMessage() {
        return isBlank(returnMessage) ? message : returnMessage;
    }

    /** A 2xx response is accepted unless its body contains an explicit failure indicator. */
    public boolean indicatesSuccess() {
        if (success != null) {
            return success;
        }
        if (returnCode != null) {
            return returnCode == 1;
        }

        String responseMessage = getMessage();
        if (isBlank(responseMessage)) {
            return true;
        }
        String normalized = responseMessage.trim().toLowerCase(Locale.US);
        if (normalized.contains("fail") || normalized.contains("error")
                || normalized.contains("unable") || normalized.contains("invalid")) {
            return false;
        }
        return normalized.contains("success") || normalized.contains("delet");
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
