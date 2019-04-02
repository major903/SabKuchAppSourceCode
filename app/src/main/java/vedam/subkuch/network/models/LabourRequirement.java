package vedam.subkuch.network.models;

public class LabourRequirement {
    private String TransportCoolieId;

    private String TransportCoolieName;

    public String getTransportCoolieId() {
        return TransportCoolieId;
    }

    public void setTransportCoolieId(String TransportCoolieId) {
        this.TransportCoolieId = TransportCoolieId;
    }

    public String getTransportCoolieName() {
        return TransportCoolieName;
    }

    public void setTransportCoolieName(String TransportCoolieName) {
        this.TransportCoolieName = TransportCoolieName;
    }

    @Override
    public String toString() {
        return TransportCoolieName;
    }
}
