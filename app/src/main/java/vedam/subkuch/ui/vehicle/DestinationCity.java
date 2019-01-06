package vedam.subkuch.ui.vehicle;

public class DestinationCity {
    private String Cityname;

    private String Vehicletocityid;

    public String getCityname() {
        return Cityname;
    }

    public void setCityname(String Cityname) {
        this.Cityname = Cityname;
    }

    public String getVehicletocityid() {
        return Vehicletocityid;
    }

    public void setVehicletocityid(String Vehicletocityid) {
        this.Vehicletocityid = Vehicletocityid;
    }

    @Override
    public String toString() {
        return Cityname;
    }
}
