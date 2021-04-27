package vedam.subkuch.network.models.feature;

public class Node {
    private String FeatureId;

    private String IconUrl;

    private String Description;

    private String CityId;

    private String StateName;

    private String CityName;

    private String Name;

    private int IsEnabled;

    private String Message;

    public String getFeatureId() {
        return FeatureId;
    }

    public void setFeatureId(String FeatureId) {
        this.FeatureId = FeatureId;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String IconUrl) {
        this.IconUrl = IconUrl;
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

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public boolean getIsEnabled() {
        return IsEnabled == 1;
    }

    public String getMessage() {
        return Message;
    }
}
