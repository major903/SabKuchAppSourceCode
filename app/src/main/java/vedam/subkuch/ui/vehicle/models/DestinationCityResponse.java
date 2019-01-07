package vedam.subkuch.ui.vehicle.models;

import java.util.ArrayList;

public class DestinationCityResponse {
    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<DestinationCity> ReturnData;

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

    public ArrayList<DestinationCity> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<DestinationCity> ReturnData) {
        this.ReturnData = ReturnData;
    }

    @Override
    public String toString() {
        return "ClassPojo [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + ReturnData + "]";
    }
}
