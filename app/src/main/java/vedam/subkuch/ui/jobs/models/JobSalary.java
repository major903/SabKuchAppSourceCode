package vedam.subkuch.ui.jobs.models;

import androidx.annotation.NonNull;

public class JobSalary {

    private String Salary;

    private String JobSalaryId;

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String Salary) {
        this.Salary = Salary;
    }

    public String getJobSalaryId() {
        return JobSalaryId;
    }

    public void setJobSalaryId(String JobSalaryId) {
        this.JobSalaryId = JobSalaryId;
    }

    @NonNull
    @Override
    public String toString() {
        return Salary;
    }
}
