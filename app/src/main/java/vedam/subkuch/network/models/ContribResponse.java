package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

public class ContribResponse {

    @SerializedName("ReturnCode")
    private int returnCode;

    @SerializedName("ReturnMessage")
    private String returnMessage;

    @SerializedName("ReturnData")
    private ContribItem returnData;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public ContribItem getReturnData() {
        return returnData;
    }

    public void setReturnData(ContribItem returnData) {
        this.returnData = returnData;
    }
}
