package vedam.subkuch.network.models;

import java.util.ArrayList;

public class RegistrationMasterResponse {
    private String ReturnMessage;
    private ArrayList<RegistrationMasterOption> ReturnData;

    public String getReturnMessage() { return ReturnMessage; }
    public ArrayList<RegistrationMasterOption> getReturnData() { return ReturnData; }
}
