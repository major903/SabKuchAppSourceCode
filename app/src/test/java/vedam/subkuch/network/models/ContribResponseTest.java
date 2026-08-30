package vedam.subkuch.network.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.gson.Gson;

import org.junit.Test;

public class ContribResponseTest {

    @Test
    public void parsesSingleContribResponse() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":{"
                + "\"ContribId\":2,\"Type\":\"Submit Company Data\","
                + "\"Detail\":\"Enter any small, medium shop, office, restaurant or industry data who may have job vacancy. Any fake data will attract a penalty of 5000 Vedam Coins\","
                + "\"CreatedDate\":\"2026-08-16T19:48:31.03\"}}";

        ContribResponse response = new Gson().fromJson(json, ContribResponse.class);

        assertEquals(1, response.getReturnCode());
        assertEquals("success", response.getReturnMessage());
        assertNotNull(response.getReturnData());
        assertEquals(2, response.getReturnData().getContribId());
        assertEquals("Submit Company Data", response.getReturnData().getType());
        assertEquals("Enter any small, medium shop, office, restaurant or industry data who may have job vacancy. Any fake data will attract a penalty of 5000 Vedam Coins",
                response.getReturnData().getDetail());
    }

    @Test
    public void parsesBulkContribListResponse() {
        String json = "{\"ReturnCode\":1,\"ReturnMessage\":\"success\",\"ReturnData\":{"
                + "\"TotalCount\":3,\"PageIndex\":1,\"PageSize\":50,\"Contribs\":["
                + "{\"ContribId\":4,\"Type\":\"Submit NRI Data\",\"Detail\":\"Do you know any NRI?. Can we have his/her contact details?. Any fake data will attract a penalty of 5000 Vedam Coins\",\"CreatedDate\":\"2026-08-16T19:49:18.977\"},"
                + "{\"ContribId\":3,\"Type\":\"Submit Real Estate Lead\",\"Detail\":\"Do you know anyone planning to rent or sell a property? Let us know. Any fake data will attract a penalty of 5000 Vedam Coins\",\"CreatedDate\":\"2026-08-16T19:49:02.463\"},"
                + "{\"ContribId\":2,\"Type\":\"Submit Company Data\",\"Detail\":\"Enter any small, medium shop, office, restaurant or industry data who may have job vacancy. Any fake data will attract a penalty of 5000 Vedam Coins\",\"CreatedDate\":\"2026-08-16T19:48:31.03\"}"
                + "]}}";

        ContribListResponse response = new Gson().fromJson(json, ContribListResponse.class);

        assertEquals(1, response.getReturnCode());
        assertEquals("success", response.getReturnMessage());
        assertNotNull(response.getReturnData());
        assertEquals(3, response.getReturnData().getTotalCount());
        assertEquals(3, response.getReturnData().getContribs().size());

        ContribItem nri = response.getReturnData().getContribs().get(0);
        assertEquals(4, nri.getContribId());
        assertEquals("Submit NRI Data", nri.getType());

        ContribItem realEstate = response.getReturnData().getContribs().get(1);
        assertEquals(3, realEstate.getContribId());
        assertEquals("Submit Real Estate Lead", realEstate.getType());

        ContribItem company = response.getReturnData().getContribs().get(2);
        assertEquals(2, company.getContribId());
        assertEquals("Submit Company Data", company.getType());
    }
}
