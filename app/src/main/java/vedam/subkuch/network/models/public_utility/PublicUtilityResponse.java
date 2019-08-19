package vedam.subkuch.network.models.public_utility;

import java.util.ArrayList;

public class PublicUtilityResponse {

    private String ReturnCode;

    private ArrayList<PublicUtility> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<PublicUtility> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<PublicUtility> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
