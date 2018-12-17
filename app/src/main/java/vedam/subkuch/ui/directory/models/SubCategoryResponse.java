package vedam.subkuch.ui.directory.models;

public class SubCategoryResponse {
    private String message;

    private String status;

    private SubCategoryResult SubCategoryResult;

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

    public SubCategoryResult getSubCategoryResult() {
        return SubCategoryResult;
    }

    public void setSubCategoryResult(SubCategoryResult SubCategoryResult) {
        this.SubCategoryResult = SubCategoryResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", SubCategoryResult = " + SubCategoryResult + "]";
    }
}
