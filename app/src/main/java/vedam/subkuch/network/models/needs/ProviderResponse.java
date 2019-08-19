package vedam.subkuch.network.models.needs;

import java.util.ArrayList;

public class ProviderResponse {
    private String ReturnCode;

    private ArrayList<Provider> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<Provider> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Provider> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
