package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

/** A tolerant representation of the master-list records used during registration. */
public class RegistrationMasterOption {
    @SerializedName(value = "Name", alternate = {"CountryName", "StateName", "DistrictName", "LanguageName", "occupationname"})
    private String name;
    @SerializedName(value = "CountryId", alternate = {"Countryid", "countryid"})
    private Integer countryId;
    @SerializedName(value = "StateId", alternate = {"stateid", "stateId"})
    private Integer stateId;
    @SerializedName(value = "DistrictId", alternate = {"districtid", "districtId", "CityId", "Cityid", "cityId"})
    private Integer districtId;
    @SerializedName(value = "LanguageId", alternate = {"languageid"})
    private Integer languageId;
    @SerializedName(value = "OccupationId", alternate = {"occupationid"})
    private Integer occupationId;
    @SerializedName(value = "Countrycode", alternate = {"CountryCode", "countrycode"})
    private String countryCode;

    public static RegistrationMasterOption placeholder(String label) {
        RegistrationMasterOption option = new RegistrationMasterOption();
        option.name = label;
        return option;
    }

    public int getId() {
        if (districtId != null) return districtId;
        if (stateId != null) return stateId;
        if (languageId != null) return languageId;
        if (occupationId != null) return occupationId;
        return countryId != null ? countryId : 0;
    }

    public Integer getCountryId() { return countryId; }
    public Integer getStateId() { return stateId; }
    public String getCountryCode() { return countryCode; }
    public String getName() { return name; }

    @Override
    public String toString() { return name == null ? "" : name; }
}
