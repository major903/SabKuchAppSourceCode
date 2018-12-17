package vedam.subkuch.network.models;

public class Country {

    private String Name;

    private String Countrycode;

    private String Countryid;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCountrycode() {
        return Countrycode;
    }

    public void setCountrycode(String Countrycode) {
        this.Countrycode = Countrycode;
    }

    public String getCountryid() {
        return Countryid;
    }

    public void setCountryid(String Countryid) {
        this.Countryid = Countryid;
    }

    @Override
    public String toString() {
        return Name + " (" + Countrycode + ")";
    }
}
