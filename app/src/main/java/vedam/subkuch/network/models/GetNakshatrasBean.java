package vedam.subkuch.network.models;

import java.util.List;

public class GetNakshatrasBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"nakshatraid":1,"nakshatraname":"Anuradha / Anusham / Anizham"},{"nakshatraid":2,"nakshatraname":"Arudra / Thiruvathira"},{"nakshatraid":3,"nakshatraname":"Ashlesha / Ayilyam"},{"nakshatraid":4,"nakshatraname":"Aswini / Ashwathi"},{"nakshatraid":5,"nakshatraname":"Bharani"},{"nakshatraid":6,"nakshatraname":"Chitra /Chitha"},{"nakshatraid":7,"nakshatraname":"Dhanista / Avittam"},{"nakshatraid":8,"nakshatraname":"Hastha / Atham"},{"nakshatraid":9,"nakshatraname":"Jyesta / Kettai / Thrikketa"},{"nakshatraid":10,"nakshatraname":"Krithika / Karthika"},{"nakshatraid":11,"nakshatraname":"Makha/ Magham"},{"nakshatraid":12,"nakshatraname":"Moolam / Moola"},{"nakshatraid":13,"nakshatraname":"Mrigasira / Magayiram"},{"nakshatraid":14,"nakshatraname":"Poorvabadrapada / Puratathi"},{"nakshatraid":15,"nakshatraname":"Poorvapalguni / Puram / Pubbhe"},{"nakshatraid":16,"nakshatraname":"Poorvashada / Pooradam"},{"nakshatraid":17,"nakshatraname":"Punarvasu / Punarpusam"},{"nakshatraid":18,"nakshatraname":"Pushya / Poosam / Pooyam"},{"nakshatraid":19,"nakshatraname":"Revathi"},{"nakshatraid":20,"nakshatraname":"Rohini"},{"nakshatraid":21,"nakshatraname":"Sharavan / Thiruvonam"},{"nakshatraid":22,"nakshatraname":"Shatataraka / Sadayam / Satabishek"},{"nakshatraid":23,"nakshatraname":"Swati / Chothi"},{"nakshatraid":24,"nakshatraname":"Uttarabadrapada / Uthratadhi"},{"nakshatraid":25,"nakshatraname":"Uttarapalguni / Uthram"},{"nakshatraid":26,"nakshatraname":"Uttarashada / Uthradam"},{"nakshatraid":27,"nakshatraname":"Vishaka / Vishakam"}]
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
         * nakshatraid : 1
         * nakshatraname : Anuradha / Anusham / Anizham
         */

        private int nakshatraid;
        private String nakshatraname;

        public ReturnDataBean(int nakshatraid, String nakshatraname) {
            this.nakshatraid = nakshatraid;
            this.nakshatraname = nakshatraname;
        }

        public int getNakshatraid() {
            return nakshatraid;
        }

        public void setNakshatraid(int nakshatraid) {
            this.nakshatraid = nakshatraid;
        }

        public String getNakshatraname() {
            return nakshatraname;
        }

        public void setNakshatraname(String nakshatraname) {
            this.nakshatraname = nakshatraname;
        }
    }
}
