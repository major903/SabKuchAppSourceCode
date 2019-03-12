package vedam.subkuch.network.models;

public class OwnHouse {

    private String OwnHouseType;

    private int OwnHouseId;

    public OwnHouse(int ownHouseId, String ownHouseType) {
        OwnHouseType = ownHouseType;
        OwnHouseId = ownHouseId;
    }

    public String getOwnHouseType() {
        return OwnHouseType;
    }

    public void setOwnHouseType(String OwnHouseType) {
        this.OwnHouseType = OwnHouseType;
    }

    public int getOwnHouseId() {
        return OwnHouseId;
    }

    public void setOwnHouseId(int OwnHouseId) {
        this.OwnHouseId = OwnHouseId;
    }
}
