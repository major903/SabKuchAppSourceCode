package vedam.subkuch.ui.jobs;

public class JobResponse {
    private JobsResult JobsResult;

    private String message;

    private String status;

    public JobsResult getJobsResult() {
        return JobsResult;
    }

    public void setJobsResult(JobsResult JobsResult) {
        this.JobsResult = JobsResult;
    }

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

    @Override
    public String toString() {
        return "ClassPojo [JobsResult = " + JobsResult + ", message = " + message + ", status = " + status + "]";
    }
}
