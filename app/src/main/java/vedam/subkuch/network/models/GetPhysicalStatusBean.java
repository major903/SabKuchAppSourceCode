package vedam.subkuch.network.models;

import java.util.List;

public class GetPhysicalStatusBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"PhysicalStatusid":1,"PhysicalStatusName":"Normal"},{"PhysicalStatusid":2,"PhysicalStatusName":"Physically Challenged"}]
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
         * PhysicalStatusid : 1
         * PhysicalStatusName : Normal
         */

        private int PhysicalStatusid;
        private String PhysicalStatusName;

        public int getPhysicalStatusid() {
            return PhysicalStatusid;
        }

        public void setPhysicalStatusid(int PhysicalStatusid) {
            this.PhysicalStatusid = PhysicalStatusid;
        }

        public String getPhysicalStatusName() {
            return PhysicalStatusName;
        }

        public void setPhysicalStatusName(String PhysicalStatusName) {
            this.PhysicalStatusName = PhysicalStatusName;
        }
    }
}
