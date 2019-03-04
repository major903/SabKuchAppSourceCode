package vedam.subkuch.network.models;

import java.util.List;

public class GetAllCountries {


    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"CountryId":1,"Name":"India","Countrycode":"91"},{"CountryId":3,"Name":"United Arab Emirates","Countrycode":"971"}]
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
         * CountryId : 1
         * Name : India
         * Countrycode : 91
         */

        private int CountryId;
        private String Name;
        private String Countrycode;

        public int getCountryId() {
            return CountryId;
        }

        public void setCountryId(int CountryId) {
            this.CountryId = CountryId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCountrycode() {
            return Countrycode;
        }

        public void setCountrycode(String Countrycode) {
            this.Countrycode = Countrycode;
        }
    }
}
