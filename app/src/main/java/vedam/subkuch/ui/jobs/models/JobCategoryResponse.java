package vedam.subkuch.ui.jobs.models;

import java.util.ArrayList;

public class JobCategoryResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<JobCategory> ReturnData;

    public String getReturnCode() {
        return ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public ArrayList<JobCategory> getReturnData() {
        return ReturnData;
    }

    @Override
    public String toString() {
        return "JobCategoryResponse [ReturnCode = " + ReturnCode + ", ReturnMessage = "
                + ReturnMessage + ", ReturnData = " + ReturnData + "]";
    }
}
