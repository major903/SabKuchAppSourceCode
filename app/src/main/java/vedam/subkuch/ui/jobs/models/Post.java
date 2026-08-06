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
        Gender = in.readInt();
        Call1 = in.readString();
        Time1 = in.readString();
        Time2 = in.readString();
        Call2 = in.readString();
        Time3 = in.readString();
        Time4 = in.readString();
        WhatsApp = in.readString();
        Email1 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(JobCategoryId);
        dest.writeString(JobpostId);
        dest.writeString(Requirement);
        dest.writeString(JobTitle);
        dest.writeByte((byte) (IsApply == null ? 0 : IsApply ? 1 : 2));
        dest.writeInt(Gender);
        dest.writeString(Call1);
        dest.writeString(Time1);
        dest.writeString(Time2);
        dest.writeString(Call2);
        dest.writeString(Time3);
        dest.writeString(Time4);
        dest.writeString(WhatsApp);
        dest.writeString(Email1);
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
    // 0 = any gender, 1 = male, 2 = female.
    private int Gender;
    private String Call1;
    private String Time1;
    private String Time2;
    private String Call2;
    private String Time3;
    private String Time4;
    private String WhatsApp;
    private String Email1;

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

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
    }

    public String getCall1() {
        return Call1;
    }

    public void setCall1(String call1) {
        Call1 = call1;
    }

    public String getTime1() {
        return Time1;
    }

    public void setTime1(String time1) {
        Time1 = time1;
    }

    public String getTime2() {
        return Time2;
    }

    public void setTime2(String time2) {
        Time2 = time2;
    }

    public String getCall2() {
        return Call2;
    }

    public void setCall2(String call2) {
        Call2 = call2;
    }

    public String getTime3() {
        return Time3;
    }

    public void setTime3(String time3) {
        Time3 = time3;
    }

    public String getTime4() {
        return Time4;
    }

    public void setTime4(String time4) {
        Time4 = time4;
    }

    public String getWhatsApp() {
        return WhatsApp;
    }

    public void setWhatsApp(String whatsApp) {
        WhatsApp = whatsApp;
    }

    public String getEmail1() {
        return Email1;
    }

    public void setEmail1(String email1) {
        Email1 = email1;
    }

    @Override
    public String toString() {
        return "ClassPojo [Requirement = " + Requirement + ", JobTitle = " + JobTitle + "]";
    }
}
