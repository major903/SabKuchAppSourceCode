package vedam.subkuch.network.models.wallet;

public class Wallet {

    private TermsCondition TermsConditions;
    private ProfileData ProfileData;
    private WalletDetails Wallet;

    public TermsCondition getTermsConditions() {
        return TermsConditions;
    }

    public void setTermsConditions(TermsCondition TermsConditions) {
        this.TermsConditions = TermsConditions;
    }

    public WalletDetails getWallet() {
        return Wallet;
    }

    public void setWallet(WalletDetails Wallet) {
        this.Wallet = Wallet;
    }

    public ProfileData getProfileData() {
        return ProfileData;
    }
}
