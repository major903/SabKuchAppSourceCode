package vedam.subkuch.network.models.getPreferencesResponse;

public class GetPreferenceResponse {


    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : {"PreferenceId":2,"CityId":0,"countryid":0,"ReligionId":0,"CasteId":0,"OwnCar":true,"OwnHouse":true,"LivingWithId":0,"Dating":false,"Matrimonial":true,"MinHeight":"string","MaxHeight":"string","MinWeight":"string","MaxWeight":"string","MinAge":"string","MaxAge":"string","MinIncome":"0","MaxIncome":"0","Gotraid":0,"Nakshakraid":0,"BodyTypeid":0,"Complexionid":0,"Occupationid":0,"Qualificationid":0,"AnualIncomeid":0,"IsSmoking":true,"DrinkingStatusid":0,"FoodHabitsid":0,"MotherTougeid":0,"PhysicalStatusid":0,"MatrialStatusid":0,"Doshamid":0,"OccupationOther":"","CityName":"","CountryName":"","ReligionName":"","MasterCastName":"","LivingWithName":"","GotraName":"","NakshatraName":"","BodyTypeName":"","ComplexionName":"","OccupationName":"","QualificationName":"","Income":"","DrinkingStatusName":"","FoodHabitsName":"","MothertongueName":"","PhysicalStatusName":"","MaritalStatusName":"","DoshamName":""}
     */

    private int ReturnCode;
    private String ReturnMessage;
    private ReturnDataBean ReturnData;

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

    public ReturnDataBean getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ReturnDataBean ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean {
        /**
         * PreferenceId : 2
         * CityId : 0
         * countryid : 0
         * ReligionId : 0
         * CasteId : 0
         * OwnCar : true
         * OwnHouse : true
         * LivingWithId : 0
         * Dating : false
         * Matrimonial : true
         * MinHeight : string
         * MaxHeight : string
         * MinWeight : string
         * MaxWeight : string
         * MinAge : string
         * MaxAge : string
         * MinIncome : 0
         * MaxIncome : 0
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
         * OccupationOther :
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

        private int PreferenceId;
        private int CityId;
        private int countryid;
        private int ReligionId;
        private int CasteId;
        private int LivingWithId;
        private boolean Dating;
        private boolean Matrimonial;
        private String MinHeight;
        private String MaxHeight;
        private String MinWeight;
        private String MaxWeight;
        private String MinAge;
        private String MaxAge;
        private String MinIncome;
        private String MaxIncome;
        private String MinDistance;
        private String MaxDistance;
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
        private String OccupationOther;
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
        private int OwnCarId;
        private int OwnHouseId;
        private int SmokingId;
        private String OwnCarType;
        private String OwnHouseType;
        private String SmokingType;

        public int getOwnCarId() {
            return OwnCarId;
        }

        public int getOwnHouseId() {
            return OwnHouseId;
        }

        public int getSmokingId() {
            return SmokingId;
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

        public String getMinDistance() {
            return MinDistance;
        }

        public String getMaxDistance() {
            return MaxDistance;
        }

        public int getPreferenceId() {
            return PreferenceId;
        }

        public void setPreferenceId(int PreferenceId) {
            this.PreferenceId = PreferenceId;
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

        public String getMinHeight() {
            return MinHeight;
        }

        public void setMinHeight(String MinHeight) {
            this.MinHeight = MinHeight;
        }

        public String getMaxHeight() {
            return MaxHeight;
        }

        public void setMaxHeight(String MaxHeight) {
            this.MaxHeight = MaxHeight;
        }

        public String getMinWeight() {
            return MinWeight;
        }

        public void setMinWeight(String MinWeight) {
            this.MinWeight = MinWeight;
        }

        public String getMaxWeight() {
            return MaxWeight;
        }

        public void setMaxWeight(String MaxWeight) {
            this.MaxWeight = MaxWeight;
        }

        public String getMinAge() {
            return MinAge;
        }

        public void setMinAge(String MinAge) {
            this.MinAge = MinAge;
        }

        public String getMaxAge() {
            return MaxAge;
        }

        public void setMaxAge(String MaxAge) {
            this.MaxAge = MaxAge;
        }

        public String getMinIncome() {
            return MinIncome;
        }

        public void setMinIncome(String MinIncome) {
            this.MinIncome = MinIncome;
        }

        public String getMaxIncome() {
            return MaxIncome;
        }

        public void setMaxIncome(String MaxIncome) {
            this.MaxIncome = MaxIncome;
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

        public String getOccupationOther() {
            return OccupationOther;
        }

        public void setOccupationOther(String OccupationOther) {
            this.OccupationOther = OccupationOther;
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