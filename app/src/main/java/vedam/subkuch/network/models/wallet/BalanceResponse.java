package vedam.subkuch.network.models.wallet;

/** Response returned by /api/Withdrawal/GetBalance. */
public class BalanceResponse {
    private int ReturnCode;
    private String ReturnMessage;
    private Balance ReturnData;

    public int getReturnCode() {
        return ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public Balance getReturnData() {
        return ReturnData;
    }
}
