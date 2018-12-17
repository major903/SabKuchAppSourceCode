package vedam.subkuch.network.models;

public class Feature {

    private String Name;

    private String FeatureId;

    private String Description;

    private String CityId;

    private String CityName;

    private String StateName;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFeatureId() {
        return FeatureId;
    }

    public void setFeatureId(String FeatureId) {
        this.FeatureId = FeatureId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    @Override
    public String toString() {
        return "Feature [Name = " + Name + ", FeatureId = " + FeatureId + ", Description = " + Description + ", CityId = " + CityId + ", CityName = " + CityName + ", StateName = " + StateName + "]";
    }
}
