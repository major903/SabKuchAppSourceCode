package vedam.subkuch.ui.dating.models;

import java.util.ArrayList;

public class DatingProfileResponse {

    private String ReturnCode;

    private ArrayList<DatingProfile> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<DatingProfile> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<DatingProfile> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
