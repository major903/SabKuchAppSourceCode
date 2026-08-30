package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.gson.Gson;

import org.junit.Test;

public class RegistrationResponseTest {

    @Test
    public void parsesCurrentRegistrationApiResponse() {
        RegistrationResponse response = new Gson().fromJson(
                "{\"status\":1,\"alreadyRegistered\":false,"
                        + "\"AuthenticationResult\":\"token\",\"userId\":42}",
                RegistrationResponse.class);

        assertEquals(1, response.getStatus());
        assertFalse(response.isAlreadyRegistered());
        assertEquals("token", response.getAuthenticationResult());
        assertEquals("42", response.getUserId());
    }
}
