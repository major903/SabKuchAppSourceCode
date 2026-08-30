package vedam.subkuch.network.models;

/** Response returned by POST /api/users/Register. */
public class RegistrationResponse {

    private int status;
    private boolean alreadyRegistered;
    private String AuthenticationResult;
    private String userId;

    public int getStatus() {
        return status;
    }

    public boolean isAlreadyRegistered() {
        return alreadyRegistered;
    }

    public String getAuthenticationResult() {
        return AuthenticationResult;
    }

    public String getUserId() {
        return userId;
    }
}
