package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

public class RegistrationRequestTest {

    @Test
    public void registrationPayload_includesStateAndLanguageIds() {
        RegistrationRequest request = new RegistrationRequest(
                "First", "Last", "Male", "2000-01-01", "9000000000",
                "user@example.com", 0, "", "device", "1.0", "2.0",
                96, 12, 3, 1);

        JsonObject json = new Gson().toJsonTree(request).getAsJsonObject();

        assertEquals(96, json.get("DistrictId").getAsInt());
        assertEquals(12, json.get("StateId").getAsInt());
        assertEquals(3, json.get("LanguageId").getAsInt());
        assertEquals(1, json.get("countryid").getAsInt());
    }
}
