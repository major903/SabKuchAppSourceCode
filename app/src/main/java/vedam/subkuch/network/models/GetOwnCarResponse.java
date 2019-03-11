package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetOwnCarResponse {

    private String ReturnCode;

    private ArrayList<OwnCar> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<OwnCar> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<OwnCar> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
