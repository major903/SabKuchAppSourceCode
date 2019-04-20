package vedam.subkuch.network.models.wallet;

public class WalletResponse {
    private String ReturnCode;

    private Wallet ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public Wallet getReturnData() {
        return ReturnData;
    }

    public void setReturnData(Wallet ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

}
