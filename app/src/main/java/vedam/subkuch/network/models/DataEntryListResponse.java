package vedam.subkuch.network.models;

import com.google.gson.JsonElement;

/** Response returned by DataEntry/GetUniqueDataEntries. */
public class DataEntryListResponse {
    private String ReturnMessage;
    private JsonElement ReturnData;

    public String getReturnMessage() { return ReturnMessage; }
    public JsonElement getReturnData() { return ReturnData; }
}
