package vedam.subkuch.network.models;

public class BaseResponse<T> {
    private String ReturnCode;

    private T ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public T getReturnData() {
        return ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }
}
