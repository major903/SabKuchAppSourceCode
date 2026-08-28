package vedam.subkuch.network.models.referral;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;

import org.junit.Test;

public class MyReferralTest {
    @Test
    public void readsNameFieldsRegardlessOfJsonKeyCasing() {
        String json = "{\"FIRSTNAME\":\"Aarav\",\"lastNAME\":\"Sharma\"}";

        MyReferral referral = new Gson().fromJson(json, MyReferral.class);

        assertEquals("Aarav", referral.getFirstName());
        assertEquals("Sharma", referral.getLastName());
    }
}
