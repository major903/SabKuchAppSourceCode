package vedam.subkuch.ui.jobs.models;

public class JobCategoryResponse {

    private String message;

    private String status;

    private JobCategoriesResult JobCategoriesResult;

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

    public JobCategoriesResult getJobCategoriesResult() {
        return JobCategoriesResult;
    }

    public void setJobCategoriesResult(JobCategoriesResult JobCategoriesResult) {
        this.JobCategoriesResult = JobCategoriesResult;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", JobCategoriesResult = " + JobCategoriesResult + "]";
    }
}
