package vedam.subkuch.network.models;

public class AddEventResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private String ReturnData;

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

    public String getReturnData() {
        return ReturnData;
    }

    public void setReturnData(String ReturnData) {
        this.ReturnData = ReturnData;
    }
}
