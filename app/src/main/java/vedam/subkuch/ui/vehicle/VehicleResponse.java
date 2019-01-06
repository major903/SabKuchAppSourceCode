package vedam.subkuch.ui.vehicle;

import java.util.ArrayList;

public class VehicleResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Vehicle> ReturnData;

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

    public ArrayList<Vehicle> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Vehicle> ReturnData) {
        this.ReturnData = ReturnData;
    }

    @Override
    public String toString() {
        return "ClassPojo [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + ReturnData + "]";
    }
}
