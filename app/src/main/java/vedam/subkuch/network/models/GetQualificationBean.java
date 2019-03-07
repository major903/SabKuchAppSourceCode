package vedam.subkuch.network.models;

import java.util.List;

public class GetQualificationBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"qualificationid":1,"qualificationname":"High School"},{"qualificationid":2,"qualificationname":"Pre Degree"},{"qualificationid":3,"qualificationname":"Diploma"},{"qualificationid":4,"qualificationname":"Degree - Non Technical"},{"qualificationid":5,"qualificationname":"Degree - Technical"},{"qualificationid":6,"qualificationname":"Degree - Medical"},{"qualificationid":7,"qualificationname":"PG - Non Technical"},{"qualificationid":8,"qualificationname":"PG - Technical"},{"qualificationid":9,"qualificationname":"PG - Medical"},{"qualificationid":10,"qualificationname":"Doctorate"}]
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
         * qualificationid : 1
         * qualificationname : High School
         */

        private int qualificationid;
        private String qualificationname;

        public ReturnDataBean(int qualificationid, String qualificationname) {
            this.qualificationid = qualificationid;
            this.qualificationname = qualificationname;
        }

        public int getQualificationid() {
            return qualificationid;
        }

        public void setQualificationid(int qualificationid) {
            this.qualificationid = qualificationid;
        }

        public String getQualificationname() {
            return qualificationname;
        }

        public void setQualificationname(String qualificationname) {
            this.qualificationname = qualificationname;
        }
    }
}
