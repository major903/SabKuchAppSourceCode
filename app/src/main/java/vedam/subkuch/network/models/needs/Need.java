package vedam.subkuch.network.models.needs;

public class Need {

    private int Status;

    private String NeedProviderId;

    private String WorkLocation;

    private String WorkDetails;

    private String UserId;

    private String Latitude;

    private String Longitude;
    private String NeedId;
    private String CurrentStatus;
    private String FirstName;
    private String Distance;

    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public String getDistance() {
        return Distance;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getNeedId() {
        return NeedId;
    }

    public String getCurrentStatus() {
        return CurrentStatus;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getNeedProviderId() {
        return NeedProviderId;
    }

    public void setNeedProviderId(String needProviderId) {
        NeedProviderId = needProviderId;
    }

    public String getWorkLocation() {
        return WorkLocation;
    }

    public void setWorkLocation(String workLocation) {
        WorkLocation = workLocation;
    }

    public String getWorkDetails() {
        return WorkDetails;
    }

    public void setWorkDetails(String workDetails) {
        WorkDetails = workDetails;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
