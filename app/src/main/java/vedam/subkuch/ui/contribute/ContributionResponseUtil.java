package vedam.subkuch.ui.contribute;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

import retrofit2.Response;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.ui.jobs.models.AddResponse;

/**
 * Tolerant AddResponse interpretation shared by the contribution submission forms,
 * mirroring the checks used by the company data-entry form.
 */
final class ContributionResponseUtil {

    private ContributionResponseUtil() {
    }

    static boolean isSaved(AddResponse response) {
        if (response == null) return false;
        if (response.isSuccess() || response.isStatus()
                || response.getReturnCode() == 1 || response.getReturnCode() == 200) return true;
        return isPositiveSaveMessage(response.getReturnMessage())
                || isPositiveSaveMessage(response.getMessage());
    }

    private static boolean isPositiveSaveMessage(String message) {
        if (message == null) return false;
        String normalized = message.trim().toLowerCase(Locale.ROOT);
        return Constants.SUCCESS.equalsIgnoreCase(normalized)
                || normalized.contains("successfully")
                || normalized.contains("data saved")
                || normalized.contains("data added")
                || normalized.contains("data submitted");
    }

    static String getResponseMessage(AddResponse response, String fallback) {
        if (response != null && response.getReturnMessage() != null
                && !response.getReturnMessage().trim().isEmpty()) return response.getReturnMessage();
        if (response != null && response.getMessage() != null
                && !response.getMessage().trim().isEmpty()) return response.getMessage();
        return fallback;
    }

    static String getErrorMessage(Response<?> response, String fallback) {
        if (response != null && response.errorBody() != null) {
            try {
                String body = response.errorBody().string().trim();
                JsonObject errorBody = JsonParser.parseString(body).getAsJsonObject();
                String[] messageFields = {"ReturnMessage", "message", "Message", "title", "detail"};
                for (String field : messageFields) {
                    JsonElement value = errorBody.get(field);
                    if (value != null && value.isJsonPrimitive()
                            && !value.getAsString().trim().isEmpty()) return value.getAsString();
                }
            } catch (Exception ignored) {
                // Use the standard message when the server response is not JSON.
            }
        }
        return fallback;
    }
}
