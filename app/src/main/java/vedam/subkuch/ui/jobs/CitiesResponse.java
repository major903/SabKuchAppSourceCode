package vedam.subkuch.ui.jobs;

import java.util.ArrayList;

public class CitiesResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<City> ReturnData;

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

    public ArrayList<City> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<City> ReturnData) {
        this.ReturnData = ReturnData;
    }

    @Override
    public String toString() {
        return "ClassPojo [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + ReturnData + "]";
    }
}
