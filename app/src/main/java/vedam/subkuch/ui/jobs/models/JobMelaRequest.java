package vedam.subkuch.ui.jobs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class JobMelaRequest implements Parcelable {

    private String JobExperienceDetails;

    public int[] getJobTypes() {
        return JobTypes;
    }

    public void setJobTypes(int[] jobTypes) {
        JobTypes = jobTypes;
    }

    private int[] JobTypes;

    private Boolean IsOwnTwoWheeler;

    private String UserId;

    private Boolean IsInterestedInJob;

    private String JobQualification;

    private String JobExperienceId;
    private String JobExpName;

    private String JobSalaryId;

    protected JobMelaRequest(Parcel in) {
        JobExperienceDetails = in.readString();
        JobTypes = in.createIntArray();
        byte tmpIsOwnTwoWheeler = in.readByte();
        IsOwnTwoWheeler = tmpIsOwnTwoWheeler == 0 ? null : tmpIsOwnTwoWheeler == 1;
        UserId = in.readString();
        byte tmpIsInterestedInJob = in.readByte();
        IsInterestedInJob = tmpIsInterestedInJob == 0 ? null : tmpIsInterestedInJob == 1;
        JobQualification = in.readString();
        JobExperienceId = in.readString();
        JobSalaryId = in.readString();
        JobpostId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JobExperienceDetails);
        dest.writeIntArray(JobTypes);
        dest.writeByte((byte) (IsOwnTwoWheeler == null ? 0 : IsOwnTwoWheeler ? 1 : 2));
        dest.writeString(UserId);
        dest.writeByte((byte) (IsInterestedInJob == null ? 0 : IsInterestedInJob ? 1 : 2));
        dest.writeString(JobQualification);
        dest.writeString(JobExperienceId);
        dest.writeString(JobSalaryId);
        dest.writeString(JobpostId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobMelaRequest> CREATOR = new Creator<JobMelaRequest>() {
        @Override
        public JobMelaRequest createFromParcel(Parcel in) {
            return new JobMelaRequest(in);
        }

        @Override
        public JobMelaRequest[] newArray(int size) {
            return new JobMelaRequest[size];
        }
    };

    public Boolean getOwnTwoWheeler() {
        return IsOwnTwoWheeler;
    }

    public void setOwnTwoWheeler(Boolean ownTwoWheeler) {
        IsOwnTwoWheeler = ownTwoWheeler;
    }

    public Boolean getInterestedInJob() {
        return IsInterestedInJob;
    }

    public void setInterestedInJob(Boolean interestedInJob) {
        IsInterestedInJob = interestedInJob;
    }

    public String getJobpostId() {
        return JobpostId;
    }

    public void setJobpostId(String jobpostId) {
        JobpostId = jobpostId;
    }

    private String JobpostId;

    public JobMelaRequest() {
    }


    public String getJobExperienceDetails() {
        return JobExperienceDetails;
    }

    public void setJobExperienceDetails(String JobExpereinceDetails) {
        this.JobExperienceDetails = JobExpereinceDetails;
    }

    public Boolean getIsOwnTwoWheeler() {
        return IsOwnTwoWheeler;
    }

    public void setIsOwnTwoWheeler(Boolean IsOwnTwoWheeler) {
        this.IsOwnTwoWheeler = IsOwnTwoWheeler;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public Boolean getIsInterestedInJob() {
        return IsInterestedInJob;
    }

    public void setIsInterestedInJob(Boolean IsInterestedInJob) {
        this.IsInterestedInJob = IsInterestedInJob;
    }

    public String getJobQualification() {
        return JobQualification;
    }

    public void setJobQualification(String JobQualification) {
        this.JobQualification = JobQualification;
    }

    public String getJobExperienceId() {
        return JobExperienceId;
    }

    public String getJobExpName() {
        return JobExpName;
    }

    public void setJobExperienceId(String JobExpereince) {
        this.JobExperienceId = JobExpereince;
    }

    public String getJobSalaryId() {
        return JobSalaryId;
    }

    public void setJobSalaryId(String JobSalaryId) {
        this.JobSalaryId = JobSalaryId;
    }
}
