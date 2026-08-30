package vedam.subkuch.network.models;

import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

public class UpdateUserResponseTest {

    @Test
    public void treatsEditProfileMainProfileResponseAsSuccess() {
        UpdateUserResponse response = new Gson().fromJson(
                "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":[{}]}",
                UpdateUserResponse.class);

        assertTrue(response.isSuccess());
    }
}
