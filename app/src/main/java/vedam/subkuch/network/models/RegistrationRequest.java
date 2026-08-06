package vedam.subkuch.network.models;

/**
 * Payload required by UserProfile/AddProfile in the current registration API.
 * Keep this separate from {@link Profile}; Profile also contains legacy fields
 * that should not be sent while a new account is being created.
 */
public class RegistrationRequest {

    private final int UserId = 0;
    private final String FirstName;
    private final String LastName;
    private final String Gender;
    private final String DOB;
    private final String Mobile;
    private final String EMail;
    private final int OccupationId;
    private final String OccupationOther;
    private final String DeviceId;
    private final String Latitude;
    private final String Longitude;
    private final int DistrictId;
    private final int countryid;

    public RegistrationRequest(String firstName, String lastName, String gender, String dob,
                               String mobile, String email, int occupationId,
                               String occupationOther, String deviceId, String latitude,
                               String longitude, int districtId, int countryId) {
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
        DOB = dob;
        Mobile = mobile;
        EMail = email;
        OccupationId = occupationId;
        OccupationOther = occupationOther;
        DeviceId = deviceId;
        Latitude = latitude;
        Longitude = longitude;
        DistrictId = districtId;
        countryid = countryId;
    }
}
