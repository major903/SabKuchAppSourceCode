package vedam.subkuch.network.models;

import java.util.ArrayList;

public class PhoneBookDetailsResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<PhoneBookDetail> ReturnData;

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

    public ArrayList<PhoneBookDetail> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<PhoneBookDetail> ReturnData) {
        this.ReturnData = ReturnData;
    }

    @Override
    public String toString() {
        return "ClassPojo [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + ReturnData + "]";
    }
}
