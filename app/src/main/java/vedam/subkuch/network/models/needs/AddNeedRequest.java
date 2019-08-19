package vedam.subkuch.network.models.needs;

public class AddNeedRequest {

    private String NeedProviderId;

    private String WorkLocation;

    private String WorkDetails;

    private String UserId;

    private String Latitude;

    private String Longitude;

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
