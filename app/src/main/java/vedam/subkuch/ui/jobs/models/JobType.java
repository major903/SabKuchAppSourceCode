package vedam.subkuch.ui.jobs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class JobType implements Parcelable {

    private String JobTypeId;

    private String JobTypeName;

    private boolean isChecked;

    protected JobType(Parcel in) {
        JobTypeId = in.readString();
        JobTypeName = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JobTypeId);
        dest.writeString(JobTypeName);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JobType> CREATOR = new Creator<JobType>() {
        @Override
        public JobType createFromParcel(Parcel in) {
            return new JobType(in);
        }

        @Override
        public JobType[] newArray(int size) {
            return new JobType[size];
        }
    };

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getJobTypeId() {
        return JobTypeId;
    }

    public void setJobTypeId(String JobTypeId) {
        this.JobTypeId = JobTypeId;
    }

    public String getJobTypeName() {
        return JobTypeName;
    }

    public void setJobTypeName(String JobTypeName) {
        this.JobTypeName = JobTypeName;
    }
}
