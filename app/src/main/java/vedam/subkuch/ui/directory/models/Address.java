package vedam.subkuch.ui.directory.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private String State;

    private String Address2;

    private String Address1;

    private String longitude;

    private String latitude;

    private String Zipcode;

    private String country;

    private String city;

    protected Address(Parcel in) {
        State = in.readString();
        Address2 = in.readString();
        Address1 = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        Zipcode = in.readString();
        country = in.readString();
        city = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(State);
        dest.writeString(Address2);
        dest.writeString(Address1);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(Zipcode);
        dest.writeString(country);
        dest.writeString(city);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ClassPojo [State = " + State + ", Address2 = " + Address2 + ", Address1 = " + Address1 + ", longitude = " + longitude + ", latitude = " + latitude + ", Zipcode = " + Zipcode + ", country = " + country + ", city = " + city + "]";
    }
}
