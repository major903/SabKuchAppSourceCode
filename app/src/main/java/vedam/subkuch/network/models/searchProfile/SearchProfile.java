package vedam.subkuch.network.models.searchProfile;

import java.util.List;

public class SearchProfile {
    /**
     * ReturnMessage : success
     * ReturnCode : 1
     * ReturnData : [{"ProfileId":36,"FirstName":"Emily","LastName":"Daniel","height":"5.5","Weight":"56","city":"Ahmedabad","Country":"India","ReligionName":"Hinduism","Image":"http://sabkuch.visitmydemo.xyz/APIIMAGE/downloadimage.jpg"},{"ProfileId":37,"FirstName":"Nadeem","LastName":"Nadeem","height":"6","Weight":"46","city":"Mysure","Country":"India","ReligionName":"Hinduism","Image":null},{"ProfileId":61,"FirstName":"Nadeem","LastName":"Ansari","height":"5","Weight":"80","city":"banglore","Country":"India","ReligionName":"Hinduism","Image":"http://sabkuch.visitmydemo.xyz/APIIMAGE/downloadimage.jpg"},{"ProfileId":62,"FirstName":"Nadeem","LastName":"Ansari","height":"4","Weight":"40","city":"banglore","Country":"India","ReligionName":"Hinduism","Image":"http://sabkuch.visitmydemo.xyz/APIIMAGE/downloadimage.jpg"},{"ProfileId":63,"FirstName":"Kiran","LastName":"Kumar","height":"2","Weight":"60","city":"Mysure","Country":"India","ReligionName":"Hinduism","Image":"http://sabkuch.visitmydemo.xyz/APIIMAGE/downloadimage.jpg"}]
     */

    private String ReturnMessage;
    private String ReturnCode;
    private List<ReturnDataBean> ReturnData;

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public List<ReturnDataBean> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(List<ReturnDataBean> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean {
        /**
         * ProfileId : 36
         * FirstName : Emily
         * LastName : Daniel
         * height : 5.5
         * Weight : 56
         * city : Ahmedabad
         * Country : India
         * ReligionName : Hinduism
         * Image : http://sabkuch.visitmydemo.xyz/APIIMAGE/downloadimage.jpg
         */

        private int ProfileId;
        private String FirstName;
        private String LastName;
        private String height;
        private String Weight;
        private String city;
        private String Country;
        private String ReligionName;
        private String Image;

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

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String Weight) {
            this.Weight = Weight;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getReligionName() {
            return ReligionName;
        }

        public void setReligionName(String ReligionName) {
            this.ReligionName = ReligionName;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }
}
