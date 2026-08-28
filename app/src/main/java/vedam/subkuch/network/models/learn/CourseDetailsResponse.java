package vedam.subkuch.network.models.learn;

/** Response returned by Learn/GetCourseDetails. */
public class CourseDetailsResponse {
    private String ReturnCode;
    private String ReturnMessage;
    private LearnCourseDetailsData ReturnData;

    public String getReturnCode() { return ReturnCode; }
    public String getReturnMessage() { return ReturnMessage; }
    public LearnCourseDetailsData getReturnData() { return ReturnData; }
}
