package vedam.subkuch.network.models;

/** One saved data-entry record returned by GetUniqueDataEntries. */
public class DataEntryListItem {
    private String CompanyName;
    private String Location;
    private String Mobile1;
    private String Mobile2;

    public String getCompanyName() { return CompanyName; }
    public String getLocation() { return Location; }
    public String getMobile1() { return Mobile1; }
    public String getMobile2() { return Mobile2; }
}
