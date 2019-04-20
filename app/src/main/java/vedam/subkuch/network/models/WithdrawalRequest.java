package vedam.subkuch.network.models;

public class WithdrawalRequest {
    private String VendorCode;
    private String Amount;

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setVendorCode(String vendorCode) {
        VendorCode = vendorCode;
    }
}
