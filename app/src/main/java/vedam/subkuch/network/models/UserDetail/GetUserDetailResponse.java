package vedam.subkuch.network.models.UserDetail;

import java.util.List;

public class GetUserDetailResponse {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"ProfileId":34,"FirstName":"string","LastName":"string","Gender":"string","DOB":"2019-01-21T14:52:43.087","Mobile":"string","EMail":"a@gmail.com","UserTypeId":0,"TokenId":"string","DeviceId":"string","CreatedDate":"2019-01-21T00:00:00","UpdatedDate":"2019-01-21T00:00:00","Latitude":"string","Longitude":"string","CityId":0,"countryid":0,"ReligionId":0,"CasteId":0,"OwnCar":true,"OwnHouse":true,"LivingWithId":0,"AccessPin":"string","Dating":true,"Matrimonial":true,"height":"string","Weight":"string","age":"string","Gotraid":0,"Nakshakraid":0,"BodyTypeid":0,"Complexionid":0,"Occupationid":0,"Qualificationid":0,"AnualIncomeid":0,"IsSmoking":true,"DrinkingStatusid":0,"FoodHabitsid":0,"MotherTougeid":0,"PhysicalStatusid":0,"MatrialStatusid":0,"Doshamid":0,"AboutMe":"string","OccupationOther":"string","UserTypeName":"","CityName":"","CountryName":"","ReligionName":"","MasterCastName":"","LivingWithName":"","GotraName":"","NakshatraName":"","BodyTypeName":"","ComplexionName":"","OccupationName":"","QualificationName":"","Income":"","DrinkingStatusName":"","FoodHabitsName":"","MothertongueName":"","PhysicalStatusName":"","MaritalStatusName":"","DoshamName":""}]
     */

    private int ReturnCode;
    private String ReturnMessage;
    private List<ReturnDataBean> ReturnData;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public List<ReturnDataBean> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(List<ReturnDataBean> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean {
        /**
         * ProfileId : 34
         * FirstName : string
         * LastName : string
         * Gender : string
         * DOB : 2019-01-21T14:52:43.087
         * Mobile : string
         * EMail : a@gmail.com
         * UserTypeId : 0
         * TokenId : string
         * DeviceId : string
         * CreatedDate : 2019-01-21T00:00:00
         * UpdatedDate : 2019-01-21T00:00:00
         * Latitude : string
         * Longitude : string
         * CityId : 0
         * countryid : 0
         * ReligionId : 0
         * CasteId : 0
         * OwnCar : true
         * OwnHouse : true
         * LivingWithId : 0
         * AccessPin : string
         * Dating : true
         * Matrimonial : true
         * height : string
         * Weight : string
         * age : string
         * Gotraid : 0
         * Nakshakraid : 0
         * BodyTypeid : 0
         * Complexionid : 0
         * Occupationid : 0
         * Qualificationid : 0
         * AnualIncomeid : 0
         * IsSmoking : true
         * DrinkingStatusid : 0
         * FoodHabitsid : 0
         * MotherTougeid : 0
         * PhysicalStatusid : 0
         * MatrialStatusid : 0
         * Doshamid : 0
         * AboutMe : string
         * OccupationOther : string
         * UserTypeName :
         * CityName :
         * CountryName :
         * ReligionName :
         * MasterCastName :
         * LivingWithName :
         * GotraName :
         * NakshatraName :
         * BodyTypeName :
         * ComplexionName :
         * OccupationName :
         * QualificationName :
         * Income :
         * DrinkingStatusName :
         * FoodHabitsName :
         * MothertongueName :
         * PhysicalStatusName :
         * MaritalStatusName :
         * DoshamName :
         */

        private int ProfileId;
        private String FirstName;
        private String LastName;
        private String Gender;
        private String DOB;
        private String Mobile;
        private String EMail;
        private int UserTypeId;
        private String TokenId;
        private String DeviceId;
        private String CreatedDate;
        private String UpdatedDate;
        private String Latitude;
        private String Longitude;
        private int CityId;
        private int countryid;
        private int ReligionId;
        private int CasteId;
        private int LivingWithId;
        private String AccessPin;
        private boolean Dating;
        private boolean Matrimonial;
        private String age;
        private int Gotraid;
        private int Nakshakraid;
        private int BodyTypeid;
        private int Complexionid;
        private int Occupationid;
        private int Qualificationid;
        private int AnualIncomeid;
        private int DrinkingStatusid;
        private int FoodHabitsid;
        private int MotherTougeid;
        private int PhysicalStatusid;
        private int MatrialStatusid;
        private int Doshamid;
        private String AboutMe;
        private String OccupationOther;
        private String UserTypeName;
        private String CityName;
        private String CountryName;
        private String ReligionName;
        private String MasterCastName;
        private String LivingWithName;
        private String GotraName;
        private String NakshatraName;
        private String BodyTypeName;
        private String ComplexionName;
        private String OccupationName;
        private String QualificationName;
        private String Income;
        private String DrinkingStatusName;
        private String FoodHabitsName;
        private String MothertongueName;
        private String PhysicalStatusName;
        private String MaritalStatusName;
        private String DoshamName;
        private ImageObject[] ImagesList;
        private int HeightId;
        private int WeightId;
        private int OwnCarId;
        private int OwnHouseId;
        private int SmokingId;
        private String Height;
        private String Weight;
        private String OwnCarType;
        private String OwnHouseType;
        private String SmokingType;

        public int getHeightId() {
            return HeightId;
        }

        public int getWeightId() {
            return WeightId;
        }

        public int getOwnCarId() {
            return OwnCarId;
        }

        public int getOwnHouseId() {
            return OwnHouseId;
        }

        public int getSmokingId() {
            return SmokingId;
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

        public ImageObject[] getImagesList() {
            return ImagesList;
        }

        public void setImagesList(ImageObject[] imagesList) {
            ImagesList = imagesList;
        }

        public int getProfileId() {
            return ProfileId;
        }

        public void setProfileId(int ProfileId) {
            this.ProfileId = ProfileId;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getEMail() {
            return EMail;
        }

        public void setEMail(String EMail) {
            this.EMail = EMail;
        }

        public int getUserTypeId() {
            return UserTypeId;
        }

        public void setUserTypeId(int UserTypeId) {
            this.UserTypeId = UserTypeId;
        }

        public String getTokenId() {
            return TokenId;
        }

        public void setTokenId(String TokenId) {
            this.TokenId = TokenId;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(String DeviceId) {
            this.DeviceId = DeviceId;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String CreatedDate) {
            this.CreatedDate = CreatedDate;
        }

        public String getUpdatedDate() {
            return UpdatedDate;
        }

        public void setUpdatedDate(String UpdatedDate) {
            this.UpdatedDate = UpdatedDate;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public int getCityId() {
            return CityId;
        }

        public void setCityId(int CityId) {
            this.CityId = CityId;
        }

        public int getCountryid() {
            return countryid;
        }

        public void setCountryid(int countryid) {
            this.countryid = countryid;
        }

        public int getReligionId() {
            return ReligionId;
        }

        public void setReligionId(int ReligionId) {
            this.ReligionId = ReligionId;
        }

        public int getCasteId() {
            return CasteId;
        }

        public void setCasteId(int CasteId) {
            this.CasteId = CasteId;
        }

        public int getLivingWithId() {
            return LivingWithId;
        }

        public void setLivingWithId(int LivingWithId) {
            this.LivingWithId = LivingWithId;
        }

        public String getAccessPin() {
            return AccessPin;
        }

        public void setAccessPin(String AccessPin) {
            this.AccessPin = AccessPin;
        }

        public boolean isDating() {
            return Dating;
        }

        public void setDating(boolean Dating) {
            this.Dating = Dating;
        }

        public boolean isMatrimonial() {
            return Matrimonial;
        }

        public void setMatrimonial(boolean Matrimonial) {
            this.Matrimonial = Matrimonial;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public int getGotraid() {
            return Gotraid;
        }

        public void setGotraid(int Gotraid) {
            this.Gotraid = Gotraid;
        }

        public int getNakshakraid() {
            return Nakshakraid;
        }

        public void setNakshakraid(int Nakshakraid) {
            this.Nakshakraid = Nakshakraid;
        }

        public int getBodyTypeid() {
            return BodyTypeid;
        }

        public void setBodyTypeid(int BodyTypeid) {
            this.BodyTypeid = BodyTypeid;
        }

        public int getComplexionid() {
            return Complexionid;
        }

        public void setComplexionid(int Complexionid) {
            this.Complexionid = Complexionid;
        }

        public int getOccupationid() {
            return Occupationid;
        }

        public void setOccupationid(int Occupationid) {
            this.Occupationid = Occupationid;
        }

        public int getQualificationid() {
            return Qualificationid;
        }

        public void setQualificationid(int Qualificationid) {
            this.Qualificationid = Qualificationid;
        }

        public int getAnualIncomeid() {
            return AnualIncomeid;
        }

        public void setAnualIncomeid(int AnualIncomeid) {
            this.AnualIncomeid = AnualIncomeid;
        }

        public int getDrinkingStatusid() {
            return DrinkingStatusid;
        }

        public void setDrinkingStatusid(int DrinkingStatusid) {
            this.DrinkingStatusid = DrinkingStatusid;
        }

        public int getFoodHabitsid() {
            return FoodHabitsid;
        }

        public void setFoodHabitsid(int FoodHabitsid) {
            this.FoodHabitsid = FoodHabitsid;
        }

        public int getMotherTougeid() {
            return MotherTougeid;
        }

        public void setMotherTougeid(int MotherTougeid) {
            this.MotherTougeid = MotherTougeid;
        }

        public int getPhysicalStatusid() {
            return PhysicalStatusid;
        }

        public void setPhysicalStatusid(int PhysicalStatusid) {
            this.PhysicalStatusid = PhysicalStatusid;
        }

        public int getMatrialStatusid() {
            return MatrialStatusid;
        }

        public void setMatrialStatusid(int MatrialStatusid) {
            this.MatrialStatusid = MatrialStatusid;
        }

        public int getDoshamid() {
            return Doshamid;
        }

        public void setDoshamid(int Doshamid) {
            this.Doshamid = Doshamid;
        }

        public String getAboutMe() {
            return AboutMe;
        }

        public void setAboutMe(String AboutMe) {
            this.AboutMe = AboutMe;
        }

        public String getOccupationOther() {
            return OccupationOther;
        }

        public void setOccupationOther(String OccupationOther) {
            this.OccupationOther = OccupationOther;
        }

        public String getUserTypeName() {
            return UserTypeName;
        }

        public void setUserTypeName(String UserTypeName) {
            this.UserTypeName = UserTypeName;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }

        public String getCountryName() {
            return CountryName;
        }

        public void setCountryName(String CountryName) {
            this.CountryName = CountryName;
        }

        public String getReligionName() {
            return ReligionName;
        }

        public void setReligionName(String ReligionName) {
            this.ReligionName = ReligionName;
        }

        public String getMasterCastName() {
            return MasterCastName;
        }

        public void setMasterCastName(String MasterCastName) {
            this.MasterCastName = MasterCastName;
        }

        public String getLivingWithName() {
            return LivingWithName;
        }

        public void setLivingWithName(String LivingWithName) {
            this.LivingWithName = LivingWithName;
        }

        public String getGotraName() {
            return GotraName;
        }

        public void setGotraName(String GotraName) {
            this.GotraName = GotraName;
        }

        public String getNakshatraName() {
            return NakshatraName;
        }

        public void setNakshatraName(String NakshatraName) {
            this.NakshatraName = NakshatraName;
        }

        public String getBodyTypeName() {
            return BodyTypeName;
        }

        public void setBodyTypeName(String BodyTypeName) {
            this.BodyTypeName = BodyTypeName;
        }

        public String getComplexionName() {
            return ComplexionName;
        }

        public void setComplexionName(String ComplexionName) {
            this.ComplexionName = ComplexionName;
        }

        public String getOccupationName() {
            return OccupationName;
        }

        public void setOccupationName(String OccupationName) {
            this.OccupationName = OccupationName;
        }

        public String getQualificationName() {
            return QualificationName;
        }

        public void setQualificationName(String QualificationName) {
            this.QualificationName = QualificationName;
        }

        public String getIncome() {
            return Income;
        }

        public void setIncome(String Income) {
            this.Income = Income;
        }

        public String getDrinkingStatusName() {
            return DrinkingStatusName;
        }

        public void setDrinkingStatusName(String DrinkingStatusName) {
            this.DrinkingStatusName = DrinkingStatusName;
        }

        public String getFoodHabitsName() {
            return FoodHabitsName;
        }

        public void setFoodHabitsName(String FoodHabitsName) {
            this.FoodHabitsName = FoodHabitsName;
        }

        public String getMothertongueName() {
            return MothertongueName;
        }

        public void setMothertongueName(String MothertongueName) {
            this.MothertongueName = MothertongueName;
        }

        public String getPhysicalStatusName() {
            return PhysicalStatusName;
        }

        public void setPhysicalStatusName(String PhysicalStatusName) {
            this.PhysicalStatusName = PhysicalStatusName;
        }

        public String getMaritalStatusName() {
            return MaritalStatusName;
        }

        public void setMaritalStatusName(String MaritalStatusName) {
            this.MaritalStatusName = MaritalStatusName;
        }

        public String getDoshamName() {
            return DoshamName;
        }

        public void setDoshamName(String DoshamName) {
            this.DoshamName = DoshamName;
        }
    }
}