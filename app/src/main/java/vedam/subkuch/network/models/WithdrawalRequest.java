package vedam.subkuch.network.models;

public class WithdrawalRequest {
    private String fromUser;
    private String vendorCode;
    private String amount;

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
