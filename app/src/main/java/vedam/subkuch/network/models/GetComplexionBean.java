package vedam.subkuch.network.models;

import java.util.List;

public class GetComplexionBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"Complexionid":1,"Complexionname":"Very Fair"},{"Complexionid":2,"Complexionname":"Fair"},{"Complexionid":3,"Complexionname":"Wheatish"},{"Complexionid":4,"Complexionname":"Dark"}]
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
         * Complexionid : 1
         * Complexionname : Very Fair
         */

        private int Complexionid;
        private String Complexionname;

        public int getComplexionid() {
            return Complexionid;
        }

        public void setComplexionid(int Complexionid) {
            this.Complexionid = Complexionid;
        }

        public String getComplexionname() {
            return Complexionname;
        }

        public void setComplexionname(String Complexionname) {
            this.Complexionname = Complexionname;
        }
    }
}
