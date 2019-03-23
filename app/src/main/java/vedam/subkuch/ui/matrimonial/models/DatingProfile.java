package vedam.subkuch.ui.matrimonial.models;

import android.os.Parcel;
import android.os.Parcelable;

import vedam.subkuch.network.models.UserDetail.ImageObject;

public class DatingProfile implements Parcelable {

    private String ProfileId;

    private String ComplexionName;

    private String UpdatedDate;

    private String Income;

    private String Latitude;

    private String Gender;

    private String MothertongueName;

    private String AccessPin;

    private String DOB;

    private String Dating;

    private String DoshamName;

    private String BodyTypeid;

    private String MasterCastName;

    private String AnualIncomeid;

    private String QualificationName;

    private String Gotraid;

    private String Nakshakraid;

    private String CityName;

    private String Longitude;

    private String countryid;

    private String ReligionName;

    private String MotherTougeid;

    private String GotraName;

    private String CreatedDate;

    private String LastName;

    private String FoodHabitsName;

    private String BodyTypeName;

    private String UserTypeName;

    private String CountryName;

    private String DeviceId;

    private String DrinkingStatusid;

    private String DrinkingStatusName;

    private String OccupationOther;

    private String Doshamid;

    private String CityId;

    private String PhysicalStatusName;

    private String Occupationid;

    private String MaritalStatusName;

    private String Matrimonial;

    private String TokenId;

    private String NakshatraName;

    private String Complexionid;

    private String FoodHabitsid;

    private String FirstName;

    private String LivingWithId;

    private String AboutMe;

    private String Qualificationid;

    private String EMail;

    private String Mobile;

    private String OccupationName;

    private String UserTypeId;

    private String CasteId;

    private String MatrialStatusid;

    private String PhysicalStatusid;

    private String LivingWithName;

    private String ReligionId;

    private String age;

    private String Height;
    private String Weight;
    private String OwnCarType;
    private String OwnHouseType;
    private String SmokingType;
    private String Distance;
    private ImageObject[] ImagesList;

    protected DatingProfile(Parcel in) {
        ProfileId = in.readString();
        ComplexionName = in.readString();
        UpdatedDate = in.readString();
        Income = in.readString();
        Latitude = in.readString();
        Gender = in.readString();
        MothertongueName = in.readString();
        AccessPin = in.readString();
        DOB = in.readString();
        Dating = in.readString();
        DoshamName = in.readString();
        BodyTypeid = in.readString();
        MasterCastName = in.readString();
        AnualIncomeid = in.readString();
        QualificationName = in.readString();
        Gotraid = in.readString();
        Nakshakraid = in.readString();
        CityName = in.readString();
        Longitude = in.readString();
        countryid = in.readString();
        ReligionName = in.readString();
        MotherTougeid = in.readString();
        GotraName = in.readString();
        CreatedDate = in.readString();
        LastName = in.readString();
        FoodHabitsName = in.readString();
        BodyTypeName = in.readString();
        UserTypeName = in.readString();
        CountryName = in.readString();
        DeviceId = in.readString();
        DrinkingStatusid = in.readString();
        DrinkingStatusName = in.readString();
        OccupationOther = in.readString();
        Doshamid = in.readString();
        CityId = in.readString();
        PhysicalStatusName = in.readString();
        Occupationid = in.readString();
        MaritalStatusName = in.readString();
        Matrimonial = in.readString();
        TokenId = in.readString();
        NakshatraName = in.readString();
        Complexionid = in.readString();
        FoodHabitsid = in.readString();
        FirstName = in.readString();
        LivingWithId = in.readString();
        AboutMe = in.readString();
        Qualificationid = in.readString();
        EMail = in.readString();
        Mobile = in.readString();
        OccupationName = in.readString();
        UserTypeId = in.readString();
        CasteId = in.readString();
        MatrialStatusid = in.readString();
        PhysicalStatusid = in.readString();
        LivingWithName = in.readString();
        ReligionId = in.readString();
        age = in.readString();
        Height = in.readString();
        Weight = in.readString();
        OwnCarType = in.readString();
        OwnHouseType = in.readString();
        SmokingType = in.readString();
        Distance = in.readString();
        ImagesList = in.createTypedArray(ImageObject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ProfileId);
        dest.writeString(ComplexionName);
        dest.writeString(UpdatedDate);
        dest.writeString(Income);
        dest.writeString(Latitude);
        dest.writeString(Gender);
        dest.writeString(MothertongueName);
        dest.writeString(AccessPin);
        dest.writeString(DOB);
        dest.writeString(Dating);
        dest.writeString(DoshamName);
        dest.writeString(BodyTypeid);
        dest.writeString(MasterCastName);
        dest.writeString(AnualIncomeid);
        dest.writeString(QualificationName);
        dest.writeString(Gotraid);
        dest.writeString(Nakshakraid);
        dest.writeString(CityName);
        dest.writeString(Longitude);
        dest.writeString(countryid);
        dest.writeString(ReligionName);
        dest.writeString(MotherTougeid);
        dest.writeString(GotraName);
        dest.writeString(CreatedDate);
        dest.writeString(LastName);
        dest.writeString(FoodHabitsName);
        dest.writeString(BodyTypeName);
        dest.writeString(UserTypeName);
        dest.writeString(CountryName);
        dest.writeString(DeviceId);
        dest.writeString(DrinkingStatusid);
        dest.writeString(DrinkingStatusName);
        dest.writeString(OccupationOther);
        dest.writeString(Doshamid);
        dest.writeString(CityId);
        dest.writeString(PhysicalStatusName);
        dest.writeString(Occupationid);
        dest.writeString(MaritalStatusName);
        dest.writeString(Matrimonial);
        dest.writeString(TokenId);
        dest.writeString(NakshatraName);
        dest.writeString(Complexionid);
        dest.writeString(FoodHabitsid);
        dest.writeString(FirstName);
        dest.writeString(LivingWithId);
        dest.writeString(AboutMe);
        dest.writeString(Qualificationid);
        dest.writeString(EMail);
        dest.writeString(Mobile);
        dest.writeString(OccupationName);
        dest.writeString(UserTypeId);
        dest.writeString(CasteId);
        dest.writeString(MatrialStatusid);
        dest.writeString(PhysicalStatusid);
        dest.writeString(LivingWithName);
        dest.writeString(ReligionId);
        dest.writeString(age);
        dest.writeString(Height);
        dest.writeString(Weight);
        dest.writeString(OwnCarType);
        dest.writeString(OwnHouseType);
        dest.writeString(SmokingType);
        dest.writeString(Distance);
        dest.writeTypedArray(ImagesList, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DatingProfile> CREATOR = new Creator<DatingProfile>() {
        @Override
        public DatingProfile createFromParcel(Parcel in) {
            return new DatingProfile(in);
        }

        @Override
        public DatingProfile[] newArray(int size) {
            return new DatingProfile[size];
        }
    };

    public ImageObject[] getImagesList() {
        return ImagesList;
    }

    public String getHeight() {
        return Height;
    }

    public String getWeight() {
        return Weight;
    }

    public String getOwnCarType() {
        return OwnCarType;
    }

    public String getOwnHouseType() {
        return OwnHouseType;
    }

    public String getSmokingType() {
        return SmokingType;
    }

    public String getProfileId() {
        return ProfileId;
    }

    public void setProfileId(String ProfileId) {
        this.ProfileId = ProfileId;
    }

    public String getComplexionName() {
        return ComplexionName;
    }

    public void setComplexionName(String ComplexionName) {
        this.ComplexionName = ComplexionName;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getIncome() {
        return Income;
    }

    public void setIncome(String Income) {
        this.Income = Income;
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

    public String getMothertongueName() {
        return MothertongueName;
    }

    public void setMothertongueName(String MothertongueName) {
        this.MothertongueName = MothertongueName;
    }

    public String getAccessPin() {
        return AccessPin;
    }

    public void setAccessPin(String AccessPin) {
        this.AccessPin = AccessPin;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDating() {
        return Dating;
    }

    public void setDating(String Dating) {
        this.Dating = Dating;
    }

    public String getDoshamName() {
        return DoshamName;
    }

    public void setDoshamName(String DoshamName) {
        this.DoshamName = DoshamName;
    }

    public String getBodyTypeid() {
        return BodyTypeid;
    }

    public void setBodyTypeid(String BodyTypeid) {
        this.BodyTypeid = BodyTypeid;
    }

    public String getMasterCastName() {
        return MasterCastName;
    }

    public void setMasterCastName(String MasterCastName) {
        this.MasterCastName = MasterCastName;
    }

    public String getAnualIncomeid() {
        return AnualIncomeid;
    }

    public void setAnualIncomeid(String AnualIncomeid) {
        this.AnualIncomeid = AnualIncomeid;
    }

    public String getQualificationName() {
        return QualificationName;
    }

    public void setQualificationName(String QualificationName) {
        this.QualificationName = QualificationName;
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

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
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

    public String getReligionName() {
        return ReligionName;
    }

    public void setReligionName(String ReligionName) {
        this.ReligionName = ReligionName;
    }

    public String getMotherTougeid() {
        return MotherTougeid;
    }

    public void setMotherTougeid(String MotherTougeid) {
        this.MotherTougeid = MotherTougeid;
    }

    public String getGotraName() {
        return GotraName;
    }

    public void setGotraName(String GotraName) {
        this.GotraName = GotraName;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getFoodHabitsName() {
        return FoodHabitsName;
    }

    public void setFoodHabitsName(String FoodHabitsName) {
        this.FoodHabitsName = FoodHabitsName;
    }

    public String getBodyTypeName() {
        return BodyTypeName;
    }

    public void setBodyTypeName(String BodyTypeName) {
        this.BodyTypeName = BodyTypeName;
    }

    public String getUserTypeName() {
        return UserTypeName;
    }

    public void setUserTypeName(String UserTypeName) {
        this.UserTypeName = UserTypeName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
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

    public String getDrinkingStatusName() {
        return DrinkingStatusName;
    }

    public void setDrinkingStatusName(String DrinkingStatusName) {
        this.DrinkingStatusName = DrinkingStatusName;
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

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getPhysicalStatusName() {
        return PhysicalStatusName;
    }

    public void setPhysicalStatusName(String PhysicalStatusName) {
        this.PhysicalStatusName = PhysicalStatusName;
    }

    public String getOccupationid() {
        return Occupationid;
    }

    public void setOccupationid(String Occupationid) {
        this.Occupationid = Occupationid;
    }

    public String getMaritalStatusName() {
        return MaritalStatusName;
    }

    public void setMaritalStatusName(String MaritalStatusName) {
        this.MaritalStatusName = MaritalStatusName;
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

    public String getNakshatraName() {
        return NakshatraName;
    }

    public void setNakshatraName(String NakshatraName) {
        this.NakshatraName = NakshatraName;
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

    public String getOccupationName() {
        return OccupationName;
    }

    public void setOccupationName(String OccupationName) {
        this.OccupationName = OccupationName;
    }

    public String getUserTypeId() {
        return UserTypeId;
    }

    public void setUserTypeId(String UserTypeId) {
        this.UserTypeId = UserTypeId;
    }

    public String getCasteId() {
        return CasteId;
    }

    public void setCasteId(String CasteId) {
        this.CasteId = CasteId;
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

    public String getLivingWithName() {
        return LivingWithName;
    }

    public void setLivingWithName(String LivingWithName) {
        this.LivingWithName = LivingWithName;
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

    public String getDistance() {
        return Distance;
    }
}
