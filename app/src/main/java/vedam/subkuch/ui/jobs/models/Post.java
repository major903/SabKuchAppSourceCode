package vedam.subkuch.ui.jobs.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private String JobCategoryId;

    protected Post(Parcel in) {
        JobCategoryId = in.readString();
        JobpostId = in.readString();
        Requirement = in.readString();
        JobTitle = in.readString();
        byte tmpIsApply = in.readByte();
        IsApply = tmpIsApply == 0 ? null : tmpIsApply == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JobCategoryId);
        dest.writeString(JobpostId);
        dest.writeString(Requirement);
        dest.writeString(JobTitle);
        dest.writeByte((byte) (IsApply == null ? 0 : IsApply ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getJobpostId() {
        return JobpostId;
    }

    public void setJobpostId(String jobpostId) {
        JobpostId = jobpostId;
    }

    private String JobpostId;
    private String Requirement;

    private String JobTitle;
    private Boolean IsApply;

    public Post() {

    }

    public Boolean getApply() {
        return IsApply;
    }

    public void setApply(Boolean apply) {
        IsApply = apply;
    }

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
