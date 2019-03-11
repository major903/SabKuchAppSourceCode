package vedam.subkuch.network;

import java.util.ArrayList;

import vedam.subkuch.network.models.Smoking;

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
