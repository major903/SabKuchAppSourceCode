package vedam.subkuch.network.models;

/**
 * Created by Nadeem Ansari on 7/2/21.
 */
public class BroadcastResponse {
    private int ReturnCode;

    private Broadcast ReturnData;

    private String ReturnMessage;

    public int getReturnCode() {
        return ReturnCode;
    }

    public Broadcast getReturnData() {
        return ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }
}

