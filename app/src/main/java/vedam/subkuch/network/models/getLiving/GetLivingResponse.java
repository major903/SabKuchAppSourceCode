package vedam.subkuch.network.models.getLiving;

import java.util.List;

public class GetLivingResponse {


    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"LivingWithId":1,"LivingWithName":"Alone"},{"LivingWithId":2,"LivingWithName":"With Parents"},{"LivingWithId":3,"LivingWithName":"Joint Family"},{"LivingWithId":4,"LivingWithName":"Orphan"}]
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
         * LivingWithId : 1
         * LivingWithName : Alone
         */

        private int LivingWithId;
        private String LivingWithName;


        public ReturnDataBean(int livingWithId, String livingWithName) {
            LivingWithId = livingWithId;
            LivingWithName = livingWithName;
        }

        public int getLivingWithId() {
            return LivingWithId;
        }

        public void setLivingWithId(int LivingWithId) {
            this.LivingWithId = LivingWithId;
        }

        public String getLivingWithName() {
            return LivingWithName;
        }

        public void setLivingWithName(String LivingWithName) {
            this.LivingWithName = LivingWithName;
        }
    }
}