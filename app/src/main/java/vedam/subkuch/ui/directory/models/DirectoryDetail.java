package vedam.subkuch.ui.directory.models;

import android.os.Parcel;
import android.os.Parcelable;

public class DirectoryDetail implements Parcelable {

    private String Id;
    private String Name;

    private String Phone;

    private String Email;

    private String ContactPerson;

    private String Rating;

    private String Website;

    private String Address1;

    private String Image;

    private String Mobile;

    private String Review;

    private String longitude;

    private String latitude;
    private String Distance;

    protected DirectoryDetail(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Phone = in.readString();
        Email = in.readString();
        ContactPerson = in.readString();
        Rating = in.readString();
        Website = in.readString();
        Address1 = in.readString();
        Image = in.readString();
        Mobile = in.readString();
        Review = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        Distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(Name);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeString(ContactPerson);
        dest.writeString(Rating);
        dest.writeString(Website);
        dest.writeString(Address1);
        dest.writeString(Image);
        dest.writeString(Mobile);
        dest.writeString(Review);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(Distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DirectoryDetail> CREATOR = new Creator<DirectoryDetail>() {
        @Override
        public DirectoryDetail createFromParcel(Parcel in) {
            return new DirectoryDetail(in);
        }

        @Override
        public DirectoryDetail[] newArray(int size) {
            return new DirectoryDetail[size];
        }
    };

    public String getId() {
        return Id;
    }

    public String getDistance() {
        return Distance;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String Review) {
        this.Review = Review;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "DirectoryDetail [Name = " + Name + ", Phone = " + Phone + ", Email = " + Email + ", ContactPerson = " + ContactPerson + ", Rating = " + Rating + ", Website = " + Website + ", Address1 = " + Address1 + ", Image = " + Image + ", Mobile = " + Mobile + ", Review = " + Review + ", longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}
