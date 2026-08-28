package vedam.subkuch.network.models;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

public class DeleteProfileModelsTest {

    private final Gson gson = new Gson();

    @Test
    public void fullDeletionRequest_usesUpdatedApiFieldNames() {
        String json = gson.toJson(new DeleteProfileRequest(true, true, true));

        assertTrue(json.contains("\"isDating\":true"));
        assertTrue(json.contains("\"isMatrimony\":true"));
        assertTrue(json.contains("\"isUser\":true"));
    }

    @Test
    public void legacySuccessResponse_isAccepted() {
        DeleteProfileResponse response = gson.fromJson(
                "{\"ReturnCode\":1,\"ReturnMessage\":\"success\"}",
                DeleteProfileResponse.class);

        assertTrue(response.indicatesSuccess());
    }

    @Test
    public void explicitFailureResponse_isRejected() {
        DeleteProfileResponse response = gson.fromJson(
                "{\"success\":false,\"message\":\"Unable to delete profile\"}",
                DeleteProfileResponse.class);

        assertFalse(response.indicatesSuccess());
    }
}
