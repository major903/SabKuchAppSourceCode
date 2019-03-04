package vedam.subkuch.network.models;

import java.util.List;

public class GetDoshamBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"Doshamid":1,"DoshamName":"Yes"},{"Doshamid":2,"DoshamName":"No"},{"Doshamid":3,"DoshamName":"Don't know"}]
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
         * Doshamid : 1
         * DoshamName : Yes
         */

        private int Doshamid;
        private String DoshamName;

        public int getDoshamid() {
            return Doshamid;
        }

        public void setDoshamid(int Doshamid) {
            this.Doshamid = Doshamid;
        }

        public String getDoshamName() {
            return DoshamName;
        }

        public void setDoshamName(String DoshamName) {
            this.DoshamName = DoshamName;
        }
    }
}
