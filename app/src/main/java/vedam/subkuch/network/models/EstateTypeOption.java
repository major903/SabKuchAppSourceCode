package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

/** A tolerant representation of an EstateType/GetEstateTypes record. */
public class EstateTypeOption {
    @SerializedName(value = "EstateTypeId", alternate = {"estateTypeId", "Id", "id"})
    private Integer id;
    @SerializedName(value = "Type", alternate = {"type", "Name", "name", "EstateTypeName"})
    private String name;

    public static EstateTypeOption placeholder(String label) {
        EstateTypeOption option = new EstateTypeOption();
        option.name = label;
        return option;
    }

    public int getId() { return id == null ? 0 : id; }

    public String getName() { return name; }

    @Override
    public String toString() { return name == null ? "" : name; }
}
