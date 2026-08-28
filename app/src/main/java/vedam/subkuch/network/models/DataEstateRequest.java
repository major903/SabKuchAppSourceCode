package vedam.subkuch.network.models;

/** Payload accepted by DataEstate/AddDataEstate. */
public class DataEstateRequest {
    private final int UserId;
    private final int StateId;
    private final int DistrictId;
    private final String Location;
    private final String Name;
    private final String Mobile;
    private final int EstateTypeId;
    private final String Details;

    public DataEstateRequest(int userId, int stateId, int districtId, String location,
                             String name, String mobile, int estateTypeId, String details) {
        UserId = userId;
        StateId = stateId;
        DistrictId = districtId;
        Location = location;
        Name = name;
        Mobile = mobile;
        EstateTypeId = estateTypeId;
        Details = details;
    }
}
