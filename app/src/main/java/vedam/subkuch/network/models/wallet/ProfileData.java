package vedam.subkuch.network.models.wallet;

public class ProfileData {

    private String FirstName;

    private String LastName;

    private String Mobile;
    private String RefferalCode;

    public String getRefferalCode() {
        return RefferalCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
}
