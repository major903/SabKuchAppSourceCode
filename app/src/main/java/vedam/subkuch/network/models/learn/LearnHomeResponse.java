package vedam.subkuch.network.models.learn;

/** Response returned by Learn/GetLearnHome. */
public class LearnHomeResponse {
    private String ReturnCode;
    private String ReturnMessage;
    private LearnHomeData ReturnData;

    public String getReturnCode() { return ReturnCode; }
    public String getReturnMessage() { return ReturnMessage; }
    public LearnHomeData getReturnData() { return ReturnData; }
}
