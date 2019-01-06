package vedam.subkuch.ui.vehicle;

public class Vehicle {
    private String vehicle;

    private String Vehiclebycityid;
    private String vehicletype;

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehiclebycityid() {
        return Vehiclebycityid;
    }

    public void setVehiclebycityid(String Vehiclebycityid) {
        this.Vehiclebycityid = Vehiclebycityid;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    @Override
    public String toString() {
        return vehicle;
    }
}
