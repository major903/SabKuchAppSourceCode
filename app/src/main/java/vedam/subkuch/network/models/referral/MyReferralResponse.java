package vedam.subkuch.network.models.referral;

public class MyReferralResponse {

    private String ReturnCode;

    private ReferralDetails ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ReferralDetails getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ReferralDetails ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
