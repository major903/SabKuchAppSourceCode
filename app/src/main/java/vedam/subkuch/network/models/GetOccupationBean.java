package vedam.subkuch.network.models;

import java.util.List;

public class GetOccupationBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"occupationid":1,"occupationname":"Accountant"},{"occupationid":2,"occupationname":"Administrator"},{"occupationid":3,"occupationname":"Advocate"},{"occupationid":4,"occupationname":"Agriculturist"},{"occupationid":5,"occupationname":"Architect"},{"occupationid":6,"occupationname":"Banking / Financial Service"},{"occupationid":7,"occupationname":"Beautician"},{"occupationid":8,"occupationname":"Broker / Commission Agent"},{"occupationid":9,"occupationname":"Business Person / Industrialist"},{"occupationid":10,"occupationname":"Chartered Accountant"},{"occupationid":11,"occupationname":"Consultant"},{"occupationid":12,"occupationname":"Company Secretary"},{"occupationid":13,"occupationname":"Customer Care Executive"},{"occupationid":14,"occupationname":"Defence Person"},{"occupationid":15,"occupationname":"Doctor - Alopathy"},{"occupationid":16,"occupationname":"Doctor - Ayurveda"},{"occupationid":17,"occupationname":"Doctor- Homeopathy"},{"occupationid":18,"occupationname":"Engineer - IT"},{"occupationid":19,"occupationname":"Engineer - Non IT"},{"occupationid":20,"occupationname":"Fashion Designer"},{"occupationid":21,"occupationname":"Flight Attendant"},{"occupationid":22,"occupationname":"Government Employee"},{"occupationid":23,"occupationname":"Healthcare Professional"},{"occupationid":24,"occupationname":"Home Maker"},{"occupationid":25,"occupationname":"Hotel / Hospitality Professional"},{"occupationid":26,"occupationname":"Manager / Supervisor"},{"occupationid":27,"occupationname":"Marketing / Sales Person"},{"occupationid":28,"occupationname":"Mechanic / Technician"},{"occupationid":29,"occupationname":"Performing Artiste"},{"occupationid":30,"occupationname":"Student"},{"occupationid":31,"occupationname":"Teacher / Trainer"},{"occupationid":32,"occupationname":"Top Management"},{"occupationid":33,"occupationname":"Other"}]
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
         * occupationid : 1
         * occupationname : Accountant
         */

        private int occupationid;
        private String occupationname;

        public ReturnDataBean(int occupationid, String occupationname) {
            this.occupationid = occupationid;
            this.occupationname = occupationname;
        }

        public int getOccupationid() {
            return occupationid;
        }

        public void setOccupationid(int occupationid) {
            this.occupationid = occupationid;
        }

        public String getOccupationname() {
            return occupationname;
        }

        public void setOccupationname(String occupationname) {
            this.occupationname = occupationname;
        }
    }
}
