package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

public class UpdateUserRequestTest {

    @Test
    public void serializesUpdatedEmail() {
        UpdateUserRequest request = new UpdateUserRequest(
                1, "Existing", "User", 1, 4, "new@example.com");

        JsonObject payload = JsonParser.parseString(new com.google.gson.Gson().toJson(request))
                .getAsJsonObject();

        assertEquals("new@example.com", payload.get("EMail").getAsString());
        assertEquals("Existing", payload.get("firstName").getAsString());
        assertEquals("User", payload.get("lastName").getAsString());
        assertEquals(4, payload.get("DistrictId").getAsInt());
    }

    @Test
    public void serializesStateAndLanguage() {
        UpdateUserRequest request = new UpdateUserRequest(
                67, "Nitesh", "Kumar", 1, 34, 3, 1, "test06@gmail.com");

        JsonObject payload = JsonParser.parseString(new com.google.gson.Gson().toJson(request))
                .getAsJsonObject();

        assertEquals(3, payload.get("StateId").getAsInt());
        assertEquals(1, payload.get("LanguageId").getAsInt());
        assertEquals(34, payload.get("DistrictId").getAsInt());
    }
}
