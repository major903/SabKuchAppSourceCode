package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class TransferRequest {
    @SerializedName(value = "ReceiverMobile", alternate = {"ReceiverPhoneNumber"})
    private String ReceiverMobile;

    @SerializedName("Coins")
    private BigDecimal Coins;

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        ReceiverMobile = receiverPhoneNumber;
    }

    public void setCoins(BigDecimal coins) {
        Coins = coins;
    }
}
