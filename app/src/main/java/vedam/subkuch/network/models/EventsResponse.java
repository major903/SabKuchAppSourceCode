package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EventsResponse {
    private String ReturnCode;

    private String ReturnMessage;

    @SerializedName("ReturnData")
    private ArrayList<Event> events;

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
        return events;
    }

    public void setReturnData(ArrayList<Event> ReturnData) {
        this.events = ReturnData;
    }

    @Override
    public String toString() {
        return "EventsResponse [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + events + "]";
    }
}
