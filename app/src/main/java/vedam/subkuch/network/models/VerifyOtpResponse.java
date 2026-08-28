package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

public class VerifyOtpResponse {
    @SerializedName("IsVerified")
    private Boolean isVerified;

    @SerializedName("IsNewUser")
    private Boolean isNewUser;

    @SerializedName(value = "userId", alternate = {"UserId", "ProfileId"})
    private String userId;

    @SerializedName(value = "firstName", alternate = {"FirstName"})
    private String firstName;

    @SerializedName(value = "lastName", alternate = {"LastName"})
    private String lastName;

    @SerializedName("AuthenticationResult")
    private String authenticationResult;

    public boolean isVerified() {
        return Boolean.TRUE.equals(isVerified);
    }

    public boolean isExistingUser() {
        return Boolean.FALSE.equals(isNewUser) ||
                (authenticationResult != null && !authenticationResult.trim().isEmpty());
    }

    public Boolean getIsNewUser() {
        return isNewUser;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAuthenticationResult() {
        return authenticationResult;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public void setIsNewUser(Boolean newUser) {
        isNewUser = newUser;
    }
}
