package vedam.subkuch.ui.jobs.models;

import java.util.ArrayList;

public class JobQualificationResponse {
    private String ReturnCode;

    private ArrayList<JobQualification> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<JobQualification> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<JobQualification> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

}
