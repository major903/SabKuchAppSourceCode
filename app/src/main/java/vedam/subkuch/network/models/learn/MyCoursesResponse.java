package vedam.subkuch.network.models.learn;

import java.util.ArrayList;

public class MyCoursesResponse {
    private String ReturnCode;
    private String ReturnMessage;
    private ArrayList<LearnCourse> ReturnData;

    public String getReturnCode() { return ReturnCode; }
    public String getReturnMessage() { return ReturnMessage; }
    public ArrayList<LearnCourse> getReturnData() { return ReturnData; }
}
