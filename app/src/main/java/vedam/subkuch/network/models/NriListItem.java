package vedam.subkuch.network.models;

/** One saved NRI record returned by DataNRI/GetDataNRIs. */
public class NriListItem {
    private String Name;
    private String Native;
    private String MobileNumber;

    public String getName() { return Name; }
    public String getNativePlace() { return Native; }
    public String getMobileNumber() { return MobileNumber; }
}
