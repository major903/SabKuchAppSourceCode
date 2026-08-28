package vedam.subkuch.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable {

    private String OwnCar;

    private String UpdatedDate;

    private String DeviceId;

    private String DrinkingStatusid;

    private String Latitude;

    private String Gender;

    private String OccupationOther;

    private String Doshamid;

    private String AccessPin;

    @SerializedName(value = "CityId", alternate = {"DistrictId"})
    private String CityId;

    private String DOB;

    @SerializedName(value = "Occupationid", alternate = {"OccupationId"})
    private String Occupationid;

    private String Matrimonial;

    private String TokenId;

    private String IsSmoking;

    private String Dating;

    private String height;

    private String BodyTypeid;

    private String Complexionid;

    private String FoodHabitsid;

    private String AnualIncomeid;

    @SerializedName(value = "FirstName", alternate = {"firstName"})
    private String FirstName;

    private String LivingWithId;

    private String AboutMe;

    private String Gotraid;

    private String Nakshakraid;

    private String Qualificationid;

    private String EMail;

    private String Mobile;

    private String Longitude;

    private String countryid;

    private String Weight;

    private int UserTypeId = 1;

    private String MotherTougeid;

    private String CasteId;

    private String CreatedDate;

    private String OwnHouse;

    private String MatrialStatusid;

    private String PhysicalStatusid;

    @SerializedName(value = "LastName", alternate = {"lastName"})
    private String LastName;

    private String ReligionId;

    private String age;

    @SerializedName(value = "ProfileId", alternate = {"UserId"})
    private String ProfileId;
    
    private String AuthToken;

    private boolean IsReferralDone;

    public Profile() {
    }

    protected Profile(Parcel in) {
        OwnCar = in.readString();
        UpdatedDate = in.readString();
        DeviceId = in.readString();
        DrinkingStatusid = in.readString();
        Latitude = in.readString();
        Gender = in.readString();
        OccupationOther = in.readString();
        Doshamid = in.readString();
        AccessPin = in.readString();
        CityId = in.readString();
        DOB = in.readString();
        Occupationid = in.readString();
        Matrimonial = in.readString();
        TokenId = in.readString();
        IsSmoking = in.readString();
        Dating = in.readString();
        height = in.readString();
        BodyTypeid = in.readString();
        Complexionid = in.readString();
        FoodHabitsid = in.readString();
        AnualIncomeid = in.readString();
        FirstName = in.readString();
        LivingWithId = in.readString();
        AboutMe = in.readString();
        Gotraid = in.readString();
        Nakshakraid = in.readString();
        Qualificationid = in.readString();
        EMail = in.readString();
        Mobile = in.readString();
        Longitude = in.readString();
        countryid = in.readString();
        Weight = in.readString();
        UserTypeId = in.readInt();
        MotherTougeid = in.readString();
        CasteId = in.readString();
        CreatedDate = in.readString();
        OwnHouse = in.readString();
        MatrialStatusid = in.readString();
        PhysicalStatusid = in.readString();
        LastName = in.readString();
        ReligionId = in.readString();
        age = in.readString();
        ProfileId = in.readString();
        AuthToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OwnCar);
        dest.writeString(UpdatedDate);
        dest.writeString(DeviceId);
        dest.writeString(DrinkingStatusid);
        dest.writeString(Latitude);
        dest.writeString(Gender);
        dest.writeString(OccupationOther);
        dest.writeString(Doshamid);
        dest.writeString(AccessPin);
        dest.writeString(CityId);
        dest.writeString(DOB);
        dest.writeString(Occupationid);
        dest.writeString(Matrimonial);
        dest.writeString(TokenId);
        dest.writeString(IsSmoking);
        dest.writeString(Dating);
        dest.writeString(height);
        dest.writeString(BodyTypeid);
        dest.writeString(Complexionid);
        dest.writeString(FoodHabitsid);
        dest.writeString(AnualIncomeid);
        dest.writeString(FirstName);
        dest.writeString(LivingWithId);
        dest.writeString(AboutMe);
        dest.writeString(Gotraid);
        dest.writeString(Nakshakraid);
        dest.writeString(Qualificationid);
        dest.writeString(EMail);
        dest.writeString(Mobile);
        dest.writeString(Longitude);
        dest.writeString(countryid);
        dest.writeString(Weight);
        dest.writeInt(UserTypeId);
        dest.writeString(MotherTougeid);
        dest.writeString(CasteId);
        dest.writeString(CreatedDate);
        dest.writeString(OwnHouse);
        dest.writeString(MatrialStatusid);
        dest.writeString(PhysicalStatusid);
        dest.writeString(LastName);
        dest.writeString(ReligionId);
        dest.writeString(age);
        dest.writeString(ProfileId);
        dest.writeString(AuthToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public boolean getIsReferralDone() {
        return IsReferralDone;
    }

    public String getOwnCar() {
        return OwnCar;
    }

    public void setOwnCar(String OwnCar) {
        this.OwnCar = OwnCar;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public String getDrinkingStatusid() {
        return DrinkingStatusid;
    }

    public void setDrinkingStatusid(String DrinkingStatusid) {
        this.DrinkingStatusid = DrinkingStatusid;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getOccupationOther() {
        return OccupationOther;
    }

    public void setOccupationOther(String OccupationOther) {
        this.OccupationOther = OccupationOther;
    }

    public String getDoshamid() {
        return Doshamid;
    }

    public void setDoshamid(String Doshamid) {
        this.Doshamid = Doshamid;
    }

    public String getAccessPin() {
        return AccessPin;
    }

    public void setAccessPin(String AccessPin) {
        this.AccessPin = AccessPin;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getOccupationid() {
        return Occupationid;
    }

    public void setOccupationid(String Occupationid) {
        this.Occupationid = Occupationid;
    }

    public String getMatrimonial() {
        return Matrimonial;
    }

    public void setMatrimonial(String Matrimonial) {
        this.Matrimonial = Matrimonial;
    }

    public String getTokenId() {
        return TokenId;
    }

    public void setTokenId(String TokenId) {
        this.TokenId = TokenId;
    }

    public String getIsSmoking() {
        return IsSmoking;
    }

    public void setIsSmoking(String IsSmoking) {
        this.IsSmoking = IsSmoking;
    }

    public String getDating() {
        return Dating;
    }

    public void setDating(String Dating) {
        this.Dating = Dating;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBodyTypeid() {
        return BodyTypeid;
    }

    public void setBodyTypeid(String BodyTypeid) {
        this.BodyTypeid = BodyTypeid;
    }

    public String getComplexionid() {
        return Complexionid;
    }

    public void setComplexionid(String Complexionid) {
        this.Complexionid = Complexionid;
    }

    public String getFoodHabitsid() {
        return FoodHabitsid;
    }

    public void setFoodHabitsid(String FoodHabitsid) {
        this.FoodHabitsid = FoodHabitsid;
    }

    public String getAnualIncomeid() {
        return AnualIncomeid;
    }

    public void setAnualIncomeid(String AnualIncomeid) {
        this.AnualIncomeid = AnualIncomeid;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLivingWithId() {
        return LivingWithId;
    }

    public void setLivingWithId(String LivingWithId) {
        this.LivingWithId = LivingWithId;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String AboutMe) {
        this.AboutMe = AboutMe;
    }

    public String getGotraid() {
        return Gotraid;
    }

    public void setGotraid(String Gotraid) {
        this.Gotraid = Gotraid;
    }

    public String getNakshakraid() {
        return Nakshakraid;
    }

    public void setNakshakraid(String Nakshakraid) {
        this.Nakshakraid = Nakshakraid;
    }

    public String getQualificationid() {
        return Qualificationid;
    }

    public void setQualificationid(String Qualificationid) {
        this.Qualificationid = Qualificationid;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public int getUserTypeId() {
        return UserTypeId;
    }

    public void setUserTypeId(int UserTypeId) {
        this.UserTypeId = UserTypeId;
    }

    public String getMotherTougeid() {
        return MotherTougeid;
    }

    public void setMotherTougeid(String MotherTougeid) {
        this.MotherTougeid = MotherTougeid;
    }

    public String getCasteId() {
        return CasteId;
    }

    public void setCasteId(String CasteId) {
        this.CasteId = CasteId;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getOwnHouse() {
        return OwnHouse;
    }

    public void setOwnHouse(String OwnHouse) {
        this.OwnHouse = OwnHouse;
    }

    public String getMatrialStatusid() {
        return MatrialStatusid;
    }

    public void setMatrialStatusid(String MatrialStatusid) {
        this.MatrialStatusid = MatrialStatusid;
    }

    public String getPhysicalStatusid() {
        return PhysicalStatusid;
    }

    public void setPhysicalStatusid(String PhysicalStatusid) {
        this.PhysicalStatusid = PhysicalStatusid;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getReligionId() {
        return ReligionId;
    }

    public void setReligionId(String ReligionId) {
        this.ReligionId = ReligionId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setProfileId(String profileId) {
        ProfileId = profileId;
    }

    public String getProfileId() {
        return ProfileId;
    }

    public String getAuthToken() {
        return AuthToken;
    }
}
