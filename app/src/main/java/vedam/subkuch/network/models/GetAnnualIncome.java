package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetAnnualIncome {
    private String ReturnCode;

    private ArrayList<AnnualIncome> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<AnnualIncome> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<AnnualIncome> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
