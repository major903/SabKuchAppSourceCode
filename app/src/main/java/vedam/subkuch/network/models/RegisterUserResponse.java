package vedam.subkuch.network.models;

public class RegisterUserResponse {

    private String status;
    private String userId;
    private String AuthenticationResult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthenticationResult() {
        return AuthenticationResult;
    }
}
