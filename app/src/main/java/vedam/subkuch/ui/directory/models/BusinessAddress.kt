package vedam.subkuch.ui.directory.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessAddress implements Parcelable {

    private String DealingIn;

    private String InfoLine1;

    private String ContactPerson;

    private String Email;

    private String Address;

    private String Mobile1;

    private String Mobile2;

    private String longitude;

    private String latitude;

    private String Zipcode;

    private String InfoLine2;

    private String PhoneNo;

    private String city;

    private String Distance;

    public BusinessAddress() {
    }

    protected BusinessAddress(Parcel in) {
        DealingIn = in.readString();
        InfoLine1 = in.readString();
        ContactPerson = in.readString();
        Email = in.readString();
        Address = in.readString();
        Mobile1 = in.readString();
        Mobile2 = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        Zipcode = in.readString();
        InfoLine2 = in.readString();
        PhoneNo = in.readString();
        city = in.readString();
        Distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(DealingIn);
        dest.writeString(InfoLine1);
        dest.writeString(ContactPerson);
        dest.writeString(Email);
        dest.writeString(Address);
        dest.writeString(Mobile1);
        dest.writeString(Mobile2);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(Zipcode);
        dest.writeString(InfoLine2);
        dest.writeString(PhoneNo);
        dest.writeString(city);
        dest.writeString(Distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BusinessAddress> CREATOR = new Creator<BusinessAddress>() {
        @Override
        public BusinessAddress createFromParcel(Parcel in) {
            return new BusinessAddress(in);
        }

        @Override
        public BusinessAddress[] newArray(int size) {
            return new BusinessAddress[size];
        }
    };

    public String getDealingIn() {
        return DealingIn;
    }

    public void setDealingIn(String dealingIn) {
        DealingIn = dealingIn;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getInfoLine1() {
        return InfoLine1;
    }

    public void setInfoLine1(String InfoLine1) {
        this.InfoLine1 = InfoLine1;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String Mobile1) {
        this.Mobile1 = Mobile1;
    }

    public String getMobile2() {
        return Mobile2;
    }

    public void setMobile2(String Mobile2) {
        this.Mobile2 = Mobile2;
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

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String Zipcode) {
        this.Zipcode = Zipcode;
    }

    public String getInfoLine2() {
        return InfoLine2;
    }

    public void setInfoLine2(String InfoLine2) {
        this.InfoLine2 = InfoLine2;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    @Override
    public String toString() {
        return "ClassPojo [InfoLine1 = " + InfoLine1 + ", ContactPerson = " + ContactPerson + ", Email = " + Email + ", Address = " + Address + ", Mobile1 = " + Mobile1 + ", Mobile2 = " + Mobile2 + ", longitude = " + longitude + ", latitude = " + latitude + ", Zipcode = " + Zipcode + ", InfoLine2 = " + InfoLine2 + ", PhoneNo = " + PhoneNo + "]";
    }
}
