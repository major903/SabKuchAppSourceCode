package vedam.subkuch.network.models.wallet;

/** Balance fields returned by /api/Withdrawal/GetBalance. */
public class Balance {
    private String EarnedPoints;
    private String AlreadyWithdrawnAmount;
    private String TotalCoinBalance;
    private String RemainingWithdrawableAmount;
    private String PointsToAmount;

    public String getEarnedPoints() {
        return EarnedPoints;
    }

    public String getAlreadyWithdrawnAmount() {
        return AlreadyWithdrawnAmount;
    }

    public String getTotalCoinBalance() {
        return TotalCoinBalance;
    }

    public String getRemainingWithdrawableAmount() {
        return RemainingWithdrawableAmount;
    }

    public String getPointsToAmount() {
        return PointsToAmount;
    }
}
