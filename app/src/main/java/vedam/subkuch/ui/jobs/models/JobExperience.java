package vedam.subkuch.ui.jobs.models;

import android.support.annotation.NonNull;

public class JobExperience {

    private String JobExpId;
    private String JobExpName;

    public String getJobExpId() {
        return JobExpId;
    }

    public void setJobExpId(String JobExpId) {
        this.JobExpId = JobExpId;
    }

    public String getJobExpName() {
        return JobExpName;
    }

    public void setJobExpName(String JobExpName) {
        this.JobExpName = JobExpName;
    }


    @NonNull
    @Override
    public String toString() {
        return JobExpName;
    }
}
