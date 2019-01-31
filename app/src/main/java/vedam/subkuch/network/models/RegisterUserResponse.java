package vedam.subkuch.network.models;

import java.util.ArrayList;

public class RegisterUserResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Profile> ReturnData;

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

    public ArrayList<Profile> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Profile> ReturnData) {
        this.ReturnData = ReturnData;
    }
}
