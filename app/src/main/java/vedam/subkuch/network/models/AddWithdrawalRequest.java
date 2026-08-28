package vedam.subkuch.network.models;

/** Request body for the new wallet withdrawal API. */
public class AddWithdrawalRequest {
    private final int Points;
    private final String AccountName;
    private final String AccountNumber;
    private final String BankName;
    private final String IFSCCode;

    public AddWithdrawalRequest(int points) {
        this(points, null, null, null, null);
    }

    public AddWithdrawalRequest(int points, String accountName, String accountNumber,
                                String bankName, String ifscCode) {
        Points = points;
        AccountName = accountName;
        AccountNumber = accountNumber;
        BankName = bankName;
        IFSCCode = ifscCode;
    }
}
