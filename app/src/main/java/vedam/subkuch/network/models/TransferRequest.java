package vedam.subkuch.network.models;

import java.math.BigDecimal;

public class TransferRequest {
    private String ReceiverPhoneNumber;
    private BigDecimal Coins;

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        ReceiverPhoneNumber = receiverPhoneNumber;
    }

    public void setCoins(BigDecimal coins) {
        Coins = coins;
    }
}
