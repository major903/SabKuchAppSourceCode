package vedam.subkuch.ui.vehicle.models;

import java.util.ArrayList;

public class VehicleTimingResponse {
    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<VehicleTiming> ReturnData;

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

    public ArrayList<VehicleTiming> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<VehicleTiming> ReturnData) {
        this.ReturnData = ReturnData;
    }
}
