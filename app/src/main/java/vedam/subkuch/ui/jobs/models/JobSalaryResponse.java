package vedam.subkuch.ui.jobs.models;

import java.util.ArrayList;

public class JobSalaryResponse {

    private String ReturnCode;

    private ArrayList<JobSalary> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<JobSalary> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<JobSalary> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
