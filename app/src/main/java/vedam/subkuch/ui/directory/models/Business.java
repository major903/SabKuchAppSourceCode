package vedam.subkuch.ui.directory.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Business implements Parcelable {

    private String BusinessImage;

    private String AvegrageOfRating;

    private String Phone;

    private Address[] Addresses;

    private String Email;

    private String ContactPerson;

    private String Website;

    private Review[] Reviews;

    private String Mobile;

    private String BusinessID;

    private String BusinessName;

    private String Distance;

    protected Business(Parcel in) {
        BusinessImage = in.readString();
        AvegrageOfRating = in.readString();
        Phone = in.readString();
        Addresses = in.createTypedArray(Address.CREATOR);
        Email = in.readString();
        ContactPerson = in.readString();
        Website = in.readString();
        Reviews = in.createTypedArray(Review.CREATOR);
        Mobile = in.readString();
        BusinessID = in.readString();
        BusinessName = in.readString();
        Distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BusinessImage);
        dest.writeString(AvegrageOfRating);
        dest.writeString(Phone);
        dest.writeTypedArray(Addresses, flags);
        dest.writeString(Email);
        dest.writeString(ContactPerson);
        dest.writeString(Website);
        dest.writeTypedArray(Reviews, flags);
        dest.writeString(Mobile);
        dest.writeString(BusinessID);
        dest.writeString(BusinessName);
        dest.writeString(Distance);
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

    public String getDistance() {
        return Distance;
    }

    public String getBusinessImage() {
        return BusinessImage;
    }

    public void setBusinessImage(String BusinessImage) {
        this.BusinessImage = BusinessImage;
    }

    public String getAvegrageOfRating() {
        return AvegrageOfRating;
    }

    public void setAvegrageOfRating(String AvegrageOfRating) {
        this.AvegrageOfRating = AvegrageOfRating;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public Address[] getAddresses() {
        return Addresses;
    }

    public void setAddresses(Address[] Addresses) {
        this.Addresses = Addresses;
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

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public Review[] getReviews() {
        return Reviews;
    }

    public void setReviews(Review[] Reviews) {
        this.Reviews = Reviews;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
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

    @Override
    public String toString() {
        return "ClassPojo [BusinessImage = " + BusinessImage + ", AvegrageOfRating = " + AvegrageOfRating + ", Phone = " + Phone + ", Address = " + Addresses + ", Email = " + Email + ", ContactPerson = " + ContactPerson + ", Website = " + Website + ", Reviews = " + Reviews + ", Mobile = " + Mobile + ", BusinessID = " + BusinessID + ", BusinessName = " + BusinessName + "]";
    }
}
