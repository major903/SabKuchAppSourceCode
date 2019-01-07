package vedam.subkuch.ui.ask.models;

import java.util.ArrayList;

public class AskCategoryResponse {
    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<AskCategory> ReturnData;

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

    public ArrayList<AskCategory> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<AskCategory> ReturnData) {
        this.ReturnData = ReturnData;
    }
}
