package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class RegistrationMasterResponse {
    @SerializedName(value = "ReturnMessage", alternate = {"returnMessage", "message", "Message"})
    private String ReturnMessage;
    @SerializedName(value = "ReturnData", alternate = {"returnData", "data", "Data"})
    private ArrayList<RegistrationMasterOption> ReturnData;

    public String getReturnMessage() { return ReturnMessage; }
    public ArrayList<RegistrationMasterOption> getReturnData() { return ReturnData; }
}
