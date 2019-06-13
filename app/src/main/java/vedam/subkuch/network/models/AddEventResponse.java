package vedam.subkuch.network.models;

public class AddEventResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private Event ReturnData;

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

    public Event getReturnData() {
        return ReturnData;
    }

    public void setReturnData(Event ReturnData) {
        this.ReturnData = ReturnData;
    }
}
