package vedam.subkuch.network.models.feature;

public class FeatureResponse {
    private int ReturnCode;

    private Feature ReturnData;

    private String ReturnMessage;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public Feature getReturnData() {
        return ReturnData;
    }

    public void setReturnData(Feature ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
