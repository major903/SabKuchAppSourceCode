package vedam.subkuch.network.models;

/** One saved real estate lead returned by DataEstate/GetDataEstates. */
public class EstateListItem {
    private String Name;
    private String Location;
    private String Mobile;
    private Integer EstateTypeId;
    private String Details;

    public String getName() { return Name; }
    public String getLocation() { return Location; }
    public String getMobile() { return Mobile; }
    public Integer getEstateTypeId() { return EstateTypeId; }
    public String getDetails() { return Details; }
}
