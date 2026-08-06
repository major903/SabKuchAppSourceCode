package vedam.subkuch.network.models;

/** Payload accepted by DataEntry/AddDataEntry. */
public class DataEntryRequest {
    private final int UserId;
    private final int DistrictId;
    private final String CompanyName;
    private final String Location;
    private final String Mobile1;
    private final String Mobile2;

    public DataEntryRequest(int userId, int districtId, String companyName, String location,
                            String mobile1, String mobile2) {
        UserId = userId;
        DistrictId = districtId;
        CompanyName = companyName;
        Location = location;
        Mobile1 = mobile1;
        Mobile2 = mobile2;
    }
}
