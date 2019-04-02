package vedam.subkuch.network.models;

public class WalletResponse {
    private boolean success;
    private Wallet data;

    public boolean isSuccess() {
        return success;
    }

    public Wallet getData() {
        return data;
    }
}
