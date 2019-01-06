package vedam.subkuch.network.models;

import java.util.ArrayList;

public class EventsResponse {
    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Event> ReturnData;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public ArrayList<Event> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Event> ReturnData) {
        this.ReturnData = ReturnData;
    }
}
