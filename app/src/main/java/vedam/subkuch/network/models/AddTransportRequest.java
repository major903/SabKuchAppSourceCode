package vedam.subkuch.network.models;

public class AddTransportRequest {
    private String PickupLocation;

    private String LaborRequired;

    private String VehicleTypeId;

    private String UserId;

    private String ItemType;

    private String Latitude;

    private String Longitude;

    private String DropLocation;

    public String getPickupLocation() {
        return PickupLocation;
    }

    public void setPickupLocation(String PickupLocation) {
        this.PickupLocation = PickupLocation;
    }

    public String getLaborRequired() {
        return LaborRequired;
    }

    public void setLaborRequired(String LaborRequired) {
        this.LaborRequired = LaborRequired;
    }

    public String getVehicleTypeId() {
        return VehicleTypeId;
    }

    public void setVehicleTypeId(String VehicleTypeId) {
        this.VehicleTypeId = VehicleTypeId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String ItemType) {
        this.ItemType = ItemType;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getDropLocation() {
        return DropLocation;
    }

    public void setDropLocation(String DropLocation) {
        this.DropLocation = DropLocation;
    }
}
