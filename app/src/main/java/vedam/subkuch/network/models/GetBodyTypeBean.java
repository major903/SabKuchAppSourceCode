package vedam.subkuch.network.models;

import java.util.List;

public class GetBodyTypeBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"bodytypeid":1,"bodytypename":"Average"},{"bodytypeid":2,"bodytypename":"Athletic"},{"bodytypeid":3,"bodytypename":"Slim"},{"bodytypeid":4,"bodytypename":"Heavy"}]
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
         * bodytypeid : 1
         * bodytypename : Average
         */

        private int bodytypeid;
        private String bodytypename;

        public ReturnDataBean(int bodytypeid, String bodytypename) {
            this.bodytypeid = bodytypeid;
            this.bodytypename = bodytypename;
        }

        public int getBodytypeid() {
            return bodytypeid;
        }

        public void setBodytypeid(int bodytypeid) {
            this.bodytypeid = bodytypeid;
        }

        public String getBodytypename() {
            return bodytypename;
        }

        public void setBodytypename(String bodytypename) {
            this.bodytypename = bodytypename;
        }
    }
}
