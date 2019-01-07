package vedam.subkuch.ui.jobs.models;

public class City {
    private String Name;

    private String Cityid;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCityid() {
        return Cityid;
    }

    public void setCityid(String Cityid) {
        this.Cityid = Cityid;
    }

    @Override
    public String toString() {
        return Name;
    }
}
