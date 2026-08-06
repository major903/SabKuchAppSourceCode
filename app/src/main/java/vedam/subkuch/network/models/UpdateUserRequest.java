package vedam.subkuch.network.models;

/** Request body for POST /api/Users/update. */
public class UpdateUserRequest {

    private final int UserId;
    private final String FirstName;
    private final String LastName;
    private final String DOB;
    private final String Mobile;
    private final int OccupationId;
    private final String OccupationOther;
    private final int UserTypeId;
    private final String DeviceId;
    private final double Latitude;
    private final double Longitude;
    private final int DistrictId;
    private final int countryid;

    public UpdateUserRequest(int userId, String firstName, String lastName, String dob,
                             String mobile, int occupationId, String occupationOther,
                             int userTypeId, String deviceId, double latitude,
                             double longitude, int districtId, int countryId) {
        UserId = userId;
        FirstName = firstName;
        LastName = lastName;
        DOB = dob;
        Mobile = mobile;
        OccupationId = occupationId;
        OccupationOther = occupationOther;
        UserTypeId = userTypeId;
        DeviceId = deviceId;
        Latitude = latitude;
        Longitude = longitude;
        DistrictId = districtId;
        countryid = countryId;
    }
}
