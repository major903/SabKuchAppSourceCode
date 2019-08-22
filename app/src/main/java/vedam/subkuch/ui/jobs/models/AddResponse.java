package vedam.subkuch.ui.jobs.models;

public class AddResponse {

    private boolean status;
    private boolean success;
    private String message;
    private String ReturnMessage;
    private int ReturnCode;

    public int getReturnCode() {
        return ReturnCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }
}
