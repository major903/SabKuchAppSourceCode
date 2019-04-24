package vedam.subkuch.network.models;

public class MessageRequest {
    private String StaffId;

    private String Latitude;

    private String PostedMessage;

    private String Longitude;

    private String Location;

    public String getStaffId() {
        return StaffId;
    }

    public void setStaffId(String StaffId) {
        this.StaffId = StaffId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getPostedMessage() {
        return PostedMessage;
    }

    public void setPostedMessage(String PostedMessage) {
        this.PostedMessage = PostedMessage;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
}
