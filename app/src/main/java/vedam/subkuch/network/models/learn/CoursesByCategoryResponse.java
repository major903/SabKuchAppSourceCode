package vedam.subkuch.network.models.learn;

public class CoursesByCategoryResponse {
    private String ReturnCode;
    private String ReturnMessage;
    private CoursesByCategoryData ReturnData;

    public String getReturnCode() { return ReturnCode; }
    public String getReturnMessage() { return ReturnMessage; }
    public CoursesByCategoryData getReturnData() { return ReturnData; }
}
