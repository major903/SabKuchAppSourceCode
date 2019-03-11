package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetHeightResponse {
    private String ReturnCode;

    private ArrayList<Height> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<Height> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Height> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
