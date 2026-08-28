package vedam.subkuch.network.models;

import java.util.ArrayList;

public class EstateTypeListResponse {
    private String ReturnMessage;
    private ArrayList<EstateTypeOption> ReturnData;

    public String getReturnMessage() { return ReturnMessage; }
    public ArrayList<EstateTypeOption> getReturnData() { return ReturnData; }
}
