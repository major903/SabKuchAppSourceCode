package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PhoneBookResponse {

    private String ReturnCode;

    private String ReturnMessage;

    @SerializedName("ReturnData")
    private ArrayList<PhoneBookCategory> phoneBookCategories;

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

    public ArrayList<PhoneBookCategory> getReturnData() {
        return phoneBookCategories;
    }

    public void setReturnData(ArrayList<PhoneBookCategory> phoneBookCategories) {
        this.phoneBookCategories = phoneBookCategories;
    }

    @Override
    public String toString() {
        return "PhoneBookResponse [ReturnCode = " + ReturnCode + ", ReturnMessage = " + ReturnMessage + ", ReturnData = " + phoneBookCategories + "]";
    }
}
