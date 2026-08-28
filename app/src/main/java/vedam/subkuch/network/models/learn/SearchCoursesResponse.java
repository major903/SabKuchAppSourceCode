package vedam.subkuch.network.models.learn;

public class SearchCoursesResponse {
    private String ReturnCode;
    private String ReturnMessage;
    private SearchCoursesData ReturnData;

    public String getReturnCode() { return ReturnCode; }
    public String getReturnMessage() { return ReturnMessage; }
    public SearchCoursesData getReturnData() { return ReturnData; }
}
