package vedam.subkuch.ui.directory.models;

public class CategoryResponse {

    private String message;

    private CategoryResult CategoryResult;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CategoryResult getCategoryResult() {
        return CategoryResult;
    }

    public void setCategoryResult(CategoryResult CategoryResult) {
        this.CategoryResult = CategoryResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", CategoryResult = " + CategoryResult + ", status = " + status + "]";
    }
}
