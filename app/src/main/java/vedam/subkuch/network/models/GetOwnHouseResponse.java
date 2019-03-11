package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetOwnHouseResponse {

    private String ReturnCode;

    private ArrayList<OwnHouse> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<OwnHouse> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<OwnHouse> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
