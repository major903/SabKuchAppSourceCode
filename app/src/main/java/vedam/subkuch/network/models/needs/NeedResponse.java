package vedam.subkuch.network.models.needs;

import java.util.ArrayList;

public class NeedResponse {

    private String ReturnCode;

    private ArrayList<Need> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<Need> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Need> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
