package vedam.subkuch.ui.directory.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Business implements Parcelable {

    private String Image;

    private String AvegrageOfRating;

    private BusinessAddress[] Addresses;

    private Review[] Reviews;

    private String BusinessID;

    private String BusinessName;
    private String Website;
    private String Country;
    private String City;

    protected Business(Parcel in) {
        Image = in.readString();
        AvegrageOfRating = in.readString();
        Addresses = in.createTypedArray(BusinessAddress.CREATOR);
        Reviews = in.createTypedArray(Review.CREATOR);
        BusinessID = in.readString();
        BusinessName = in.readString();
        Website = in.readString();
        Country = in.readString();
        City = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Image);
        dest.writeString(AvegrageOfRating);
        dest.writeTypedArray(Addresses, flags);
        dest.writeTypedArray(Reviews, flags);
        dest.writeString(BusinessID);
        dest.writeString(BusinessName);
        dest.writeString(Website);
        dest.writeString(Country);
        dest.writeString(City);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Business> CREATOR = new Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel in) {
            return new Business(in);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };

    public String getWebsite() {
        return Website;
    }

    public String getCountry() {
        return Country;
    }

    public String getCity() {
        return City;
    }

    public String getBusinessImage() {
        return Image;
    }

    public void setBusinessImage(String Image) {
        this.Image = Image;
    }

    public String getAvegrageOfRating() {
        return AvegrageOfRating;
    }

    public void setAvegrageOfRating(String AvegrageOfRating) {
        this.AvegrageOfRating = AvegrageOfRating;
    }

    public BusinessAddress[] getBusinessAddresses() {
        return Addresses;
    }

    public void setBusinessAddresses(BusinessAddress[] businessAddresses) {
        this.Addresses = businessAddresses;
    }

    public Review[] getReviews() {
        return Reviews;
    }

    public void setReviews(Review[] Reviews) {
        this.Reviews = Reviews;
    }

    public String getBusinessID() {
        return BusinessID;
    }

    public void setBusinessID(String BusinessID) {
        this.BusinessID = BusinessID;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }
}
