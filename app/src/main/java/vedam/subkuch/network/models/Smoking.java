package vedam.subkuch.network.models;

public class Smoking {

    private int SmokingId;

    private String SmokingType;

    public Smoking(int smokingId, String smokingType) {
        SmokingId = smokingId;
        SmokingType = smokingType;
    }

    public int getSmokingId() {
        return SmokingId;
    }

    public void setSmokingId(int SmokingId) {
        this.SmokingId = SmokingId;
    }

    public String getSmokingType() {
        return SmokingType;
    }

    public void setSmokingType(String SmokingType) {
        this.SmokingType = SmokingType;
    }
}
