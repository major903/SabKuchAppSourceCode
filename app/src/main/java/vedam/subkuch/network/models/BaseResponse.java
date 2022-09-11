package vedam.subkuch.network.models;

public class BaseResponse<T> {
    private int ReturnCode;

    private T ReturnData;

    private String ReturnMessage;

    public int getReturnCode() {
        return ReturnCode;
    }

    public T getReturnData() {
        return ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }
}
