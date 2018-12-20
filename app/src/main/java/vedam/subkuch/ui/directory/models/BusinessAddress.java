package vedam.subkuch.ui.directory.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessAddress implements Parcelable {

    private String InfoLine1;

    private String ContactPerson;

    private String Website;

    private String Mobile2;

    private String Zipcode;

    private String PhoneNo;

    private String city;

    private String country;

    private String DealingIn;

    private String State;

    private String Email;

    private String Mobile1;

    private String Address;

    private String longitude;

    private String latitude;

    private String InfoLine2;

    private String Distance;

    public BusinessAddress() {
    }

    protected BusinessAddress(Parcel in) {
        InfoLine1 = in.readString();
        ContactPerson = in.readString();
        Website = in.readString();
        Mobile2 = in.readString();
        Zipcode = in.readString();
        PhoneNo = in.readString();
        city = in.readString();
        country = in.readString();
        DealingIn = in.readString();
        State = in.readString();
        Email = in.readString();
        Mobile1 = in.readString();
        Address = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        InfoLine2 = in.readString();
        Distance = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(InfoLine1);
        dest.writeString(ContactPerson);
        dest.writeString(Website);
        dest.writeString(Mobile2);
        dest.writeString(Zipcode);
        dest.writeString(PhoneNo);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(DealingIn);
        dest.writeString(State);
        dest.writeString(Email);
        dest.writeString(Mobile1);
        dest.writeString(Address);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(InfoLine2);
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

    public String getDistance() {
        return Distance;
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

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getMobile2() {
        return Mobile2;
    }

    public void setMobile2(String Mobile2) {
        this.Mobile2 = Mobile2;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String Zipcode) {
        this.Zipcode = Zipcode;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDealingIn() {
        return DealingIn;
    }

    public void setDealingIn(String DealingIn) {
        this.DealingIn = DealingIn;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String Mobile1) {
        this.Mobile1 = Mobile1;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
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

    public String getInfoLine2() {
        return InfoLine2;
    }

    public void setInfoLine2(String InfoLine2) {
        this.InfoLine2 = InfoLine2;
    }

    @Override
    public String toString() {
        return "ClassPojo [InfoLine1 = " + InfoLine1 + ", ContactPerson = " + ContactPerson + ", Website = " + Website + ", Mobile2 = " + Mobile2 + ", Zipcode = " + Zipcode + ", PhoneNo = " + PhoneNo + ", city = " + city + ", country = " + country + ", DealingIn = " + DealingIn + ", State = " + State + ", Email = " + Email + ", Mobile1 = " + Mobile1 + ", BusinessAddress = " + Address + ", longitude = " + longitude + ", latitude = " + latitude + ", InfoLine2 = " + InfoLine2 + "]";
    }
}
