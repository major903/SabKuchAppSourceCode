package vedam.subkuch.network.models;

public class PhoneBookDetail {

    private String Name;

    private String Number;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    @Override
    public String toString() {
        return "ClassPojo [Name = " + Name + ", Number = " + Number + "]";
    }
}
