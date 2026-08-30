package vedam.subkuch.network.models;

/** Request body for POST /api/UserProfile/EditProfile. */
public class UpdateUserRequest {

    private final int ProfileId;
    private final String firstName;
    private final String lastName;
    private final int CountryId;
    private final int DistrictId;
    private final int StateId;
    private final int LanguageId;
    private final String EMail;

    public UpdateUserRequest(int profileId, String firstName, String lastName, int countryId,
                             int districtId, int stateId, int languageId, String email) {
        ProfileId = profileId;
        this.firstName = firstName;
        this.lastName = lastName;
        CountryId = countryId;
        DistrictId = districtId;
        StateId = stateId;
        LanguageId = languageId;
        EMail = email;
    }

    public UpdateUserRequest(int profileId, String firstName, String lastName, int countryId,
                             int districtId, String email) {
        this(profileId, firstName, lastName, countryId, districtId, 0, 0, email);
    }
}
