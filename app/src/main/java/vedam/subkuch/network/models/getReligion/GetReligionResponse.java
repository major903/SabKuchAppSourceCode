package vedam.subkuch.network.models.getReligion;

import java.util.List;

public class GetReligionResponse {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"ReligionID":1,"ReligionName":"Hindu"},{"ReligionID":2,"ReligionName":"Muslim"},{"ReligionID":3,"ReligionName":"Christian"},{"ReligionID":4,"ReligionName":"Sikh"},{"ReligionID":5,"ReligionName":"Parsi"},{"ReligionID":6,"ReligionName":"Jain"},{"ReligionID":7,"ReligionName":"Buddhist"},{"ReligionID":8,"ReligionName":"Jewish"},{"ReligionID":9,"ReligionName":"Other"}]
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
         * ReligionID : 1
         * ReligionName : Hindu
         */

        private int ReligionID;
        private String ReligionName;

        public ReturnDataBean(int religionID, String religionName) {
            ReligionID = religionID;
            ReligionName = religionName;
        }

        public int getReligionID() {
            return ReligionID;
        }

        public void setReligionID(int ReligionID) {
            this.ReligionID = ReligionID;
        }

        public String getReligionName() {
            return ReligionName;
        }

        public void setReligionName(String ReligionName) {
            this.ReligionName = ReligionName;
        }
    }
}