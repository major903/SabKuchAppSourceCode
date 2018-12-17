package vedam.subkuch.ui.directory.models;

public class DirectoryDetailResponse {

    private String message;

    private String status;

    private BusinessesResult BusinessesResult;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BusinessesResult getBusinessesResult() {
        return BusinessesResult;
    }

    public void setBusinessesResult(BusinessesResult BusinessesResult) {
        this.BusinessesResult = BusinessesResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", BusinessesResult = " + BusinessesResult + "]";
    }
}
