package vedam.subkuch.network.models;

public class PhoneBookCategory {

    private String Name;

    private String Phonebookcategoryid;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhonebookcategoryid() {
        return Phonebookcategoryid;
    }

    public void setPhonebookcategoryid(String Phonebookcategoryid) {
        this.Phonebookcategoryid = Phonebookcategoryid;
    }

    @Override
    public String toString() {
        return "PhoneBookCategory [Name = " + Name + ", Phonebookcategoryid = " + Phonebookcategoryid + "]";
    }
}
