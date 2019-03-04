package vedam.subkuch.network.models.getLikeDislikeResponse;

public class GetLikeDislikeStatus {
    /**
     * ReturnMessage : success
     * ReturnCode : 1
     * ReturnData : null
     */

    private String ReturnMessage;
    private String ReturnCode;
    private Object ReturnData;

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public Object getReturnData() {
        return ReturnData;
    }

    public void setReturnData(Object ReturnData) {
        this.ReturnData = ReturnData;
    }
}
