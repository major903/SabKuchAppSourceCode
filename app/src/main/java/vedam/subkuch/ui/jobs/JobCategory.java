package vedam.subkuch.ui.jobs;

public class JobCategory {

    private String JobCategoryName;

    private String JobCategoryId;

    public String getJobCategoryName() {
        return JobCategoryName;
    }

    public void setJobCategoryName(String JobCategoryName) {
        this.JobCategoryName = JobCategoryName;
    }

    public String getJobCategoryId() {
        return JobCategoryId;
    }

    public void setJobCategoryId(String JobCategoryId) {
        this.JobCategoryId = JobCategoryId;
    }

    @Override
    public String toString() {
        return "JobCategory [JobCategoryName = " + JobCategoryName + ", JobCategoryId = " + JobCategoryId + "]";
    }
}
