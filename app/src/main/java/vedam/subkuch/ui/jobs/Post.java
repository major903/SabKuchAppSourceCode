package vedam.subkuch.ui.jobs;

public class Post {

    private String JobCategoryId;

    private String Requirement;

    private String JobTitle;

    public String getRequirement() {
        return Requirement;
    }

    public void setRequirement(String Requirement) {
        this.Requirement = Requirement;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String JobTitle) {
        this.JobTitle = JobTitle;
    }

    public String getJobCategoryId() {
        return JobCategoryId;
    }

    public void setJobCategoryId(String jobCategoryId) {
        JobCategoryId = jobCategoryId;
    }

    @Override
    public String toString() {
        return "ClassPojo [Requirement = " + Requirement + ", JobTitle = " + JobTitle + "]";
    }
}
