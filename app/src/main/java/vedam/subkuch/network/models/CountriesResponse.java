package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountriesResponse {

    private String ReturnCode;

    private String ReturnMessage;

    @SerializedName("ReturnData")
    private ArrayList<Country> countries;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> ReturnData) {
        this.countries = ReturnData;
    }

    @Override
    public String toString() {
        return "CountriesResponse [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", countries = " + countries + "]";
    }
}
