package vedam.subkuch.network.models;

import java.util.ArrayList;

public class BaseGetMasterModel<T> {

    private String ReturnCode;

    private ArrayList<T> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<T> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<T> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
