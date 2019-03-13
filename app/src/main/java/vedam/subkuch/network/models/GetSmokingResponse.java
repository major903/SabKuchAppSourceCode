package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetSmokingResponse {

    private String ReturnCode;

    private ArrayList<Smoking> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<Smoking> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Smoking> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
