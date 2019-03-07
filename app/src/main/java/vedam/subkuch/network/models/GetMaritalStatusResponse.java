package vedam.subkuch.network.models;

import java.util.List;

public class GetMaritalStatusResponse {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"MaritalStatus_Id":1,"MaritalStatus_Name":"Never Married"},{"MaritalStatus_Id":2,"MaritalStatus_Name":"Divorced"},{"MaritalStatus_Id":3,"MaritalStatus_Name":"Widowed"},{"MaritalStatus_Id":4,"MaritalStatus_Name":"Awaiting Divorce"},{"MaritalStatus_Id":5,"MaritalStatus_Name":"Annulled"}]
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
         * MaritalStatus_Id : 1
         * MaritalStatus_Name : Never Married
         */

        private int MaritalStatus_Id;
        private String MaritalStatus_Name;

        public ReturnDataBean(int maritalStatus_Id, String maritalStatus_Name) {
            MaritalStatus_Id = maritalStatus_Id;
            MaritalStatus_Name = maritalStatus_Name;
        }

        public int getMaritalStatus_Id() {
            return MaritalStatus_Id;
        }

        public void setMaritalStatus_Id(int MaritalStatus_Id) {
            this.MaritalStatus_Id = MaritalStatus_Id;
        }

        public String getMaritalStatus_Name() {
            return MaritalStatus_Name;
        }

        public void setMaritalStatus_Name(String MaritalStatus_Name) {
            this.MaritalStatus_Name = MaritalStatus_Name;
        }
    }
}
