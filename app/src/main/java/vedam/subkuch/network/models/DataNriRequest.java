package vedam.subkuch.network.models;

/** Payload accepted by DataNRI/AddDataNRI. */
public class DataNriRequest {
    private final int UserId;
    private final String Name;
    private final int CountryId;
    private final String Native;
    private final String MobileNumber;
    private final String Details;

    public DataNriRequest(int userId, String name, int countryId, String nativePlace,
                          String mobileNumber, String details) {
        UserId = userId;
        Name = name;
        CountryId = countryId;
        Native = nativePlace;
        MobileNumber = mobileNumber;
        Details = details;
    }
}
