package vedam.subkuch.ui.jobs.models;

import java.util.ArrayList;

public class JobExperienceResponse {

    private String ReturnCode;

    private ArrayList<JobExperience> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<JobExperience> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<JobExperience> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
