package vedam.subkuch.ui.jobs;

import java.util.ArrayList;

public class JobsResult {

    private ArrayList<Job> Jobs;

    public ArrayList<Job> getJobs() {
        return Jobs;
    }

    public void setJobs(ArrayList<Job> Jobs) {
        this.Jobs = Jobs;
    }

    @Override
    public String toString() {
        return "ClassPojo [Jobs = " + Jobs + "]";
    }
}
