package vedam.subkuch.ui.jobs;

import java.util.ArrayList;

public class JobCategoriesResult {

    private ArrayList<JobCategory> JobCategories;

    public ArrayList<JobCategory> getJobCategories() {
        return JobCategories;
    }

    public void setJobCategories(ArrayList<JobCategory> JobCategories) {
        this.JobCategories = JobCategories;
    }

    @Override
    public String toString() {
        return "ClassPojo [JobCategories = " + JobCategories + "]";
    }
}
