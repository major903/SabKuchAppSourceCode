package vedam.subkuch.ui.directory.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    private String BusinessReview;
    private String Rating;
    private String UserName;

    protected Review(Parcel in) {
        BusinessReview = in.readString();
        Rating = in.readString();
        UserName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BusinessReview);
        dest.writeString(Rating);
        dest.writeString(UserName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getBusinessReview() {
        return BusinessReview;
    }

    public void setBusinessReview(String BusinessReview) {
        this.BusinessReview = BusinessReview;
    }

    public String getRating() {
        return Rating;
    }

    public String getUserName() {
        return UserName;
    }

    @Override
    public String toString() {
        return "ClassPojo [BusinessReview = " + BusinessReview + "]";
    }
}
