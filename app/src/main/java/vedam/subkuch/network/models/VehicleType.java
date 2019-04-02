package vedam.subkuch.network.models;

public class VehicleType {

    private String TransportTypeName;

    private String TransportTypeId;

    public String getTransportTypeName() {
        return TransportTypeName;
    }

    public void setTransportTypeName(String TransportTypeName) {
        this.TransportTypeName = TransportTypeName;
    }

    public String getTransportTypeId() {
        return TransportTypeId;
    }

    public void setTransportTypeId(String TransportTypeId) {
        this.TransportTypeId = TransportTypeId;
    }

    @Override
    public String toString() {
        return TransportTypeName;
    }
}
