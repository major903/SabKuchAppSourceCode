package vedam.subkuch.network.models.wallet;

public class WalletDetails {
    private String TotalPointsEarned;

    private String AvailableBalance;

    private String TotalWithdrawal;

    private String TotalReferralIncome;

    public String getTotalPointsEarned() {
        return TotalPointsEarned;
    }

    public void setTotalPointsEarned(String TotalPointsEarned) {
        this.TotalPointsEarned = TotalPointsEarned;
    }

    public String getAvailableBalance() {
        return AvailableBalance;
    }

    public void setAvailableBalance(String AvailableBalance) {
        this.AvailableBalance = AvailableBalance;
    }

    public String getTotalWithdrawal() {
        return TotalWithdrawal;
    }

    public void setTotalWithdrawal(String TotalWithdrawal) {
        this.TotalWithdrawal = TotalWithdrawal;
    }

    public String getTotalReferralIncome() {
        return TotalReferralIncome;
    }

    public void setTotalReferralIncome(String TotalReferralIncome) {
        this.TotalReferralIncome = TotalReferralIncome;
    }


}
