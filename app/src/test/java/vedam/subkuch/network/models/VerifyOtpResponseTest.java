package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

public class VerifyOtpResponseTest {

    private final Gson gson = new Gson();

    @Test
    public void parsesExistingUserLoginResponse() {
        String json = "{\"IsVerified\":true,\"IsNewUser\":false,\"userId\":3," +
                "\"firstName\":\"Sangeetha\",\"lastName\":\"B\"," +
                "\"AuthenticationResult\":\"Bearer test-token\"}";

        VerifyOtpResponse response = gson.fromJson(json, VerifyOtpResponse.class);

        assertTrue(response.isVerified());
        assertTrue(response.isExistingUser());
        assertEquals(Boolean.FALSE, response.getIsNewUser());
        assertEquals("3", response.getUserId());
        assertEquals("Sangeetha", response.getFirstName());
        assertEquals("B", response.getLastName());
        assertEquals("Bearer test-token", response.getAuthenticationResult());
    }

    @Test
    public void parsesNewUserVerificationResponse() {
        VerifyOtpResponse response = gson.fromJson(
                "{\"IsVerified\":true,\"IsNewUser\":true}", VerifyOtpResponse.class);

        assertTrue(response.isVerified());
        assertFalse(response.isExistingUser());
        assertEquals(Boolean.TRUE, response.getIsNewUser());
    }

    @Test
    public void parsesRejectedVerificationResponse() {
        VerifyOtpResponse response = gson.fromJson(
                "{\"IsVerified\":false}", VerifyOtpResponse.class);

        assertFalse(response.isVerified());
        assertFalse(response.isExistingUser());
    }
}
