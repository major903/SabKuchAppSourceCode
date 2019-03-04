package vedam.subkuch.network.models;

import java.util.List;

public class GetFoodHabitsBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"FoodHabitsid":1,"FoodHabitsName":"Vegetarian"},{"FoodHabitsid":2,"FoodHabitsName":"Non-Vegetarian"},{"FoodHabitsid":3,"FoodHabitsName":"Occasionally Non-Vegetarian"},{"FoodHabitsid":4,"FoodHabitsName":"Eggetarian"},{"FoodHabitsid":5,"FoodHabitsName":"Jain"},{"FoodHabitsid":6,"FoodHabitsName":"Vegan"}]
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
         * FoodHabitsid : 1
         * FoodHabitsName : Vegetarian
         */

        private int FoodHabitsid;
        private String FoodHabitsName;

        public int getFoodHabitsid() {
            return FoodHabitsid;
        }

        public void setFoodHabitsid(int FoodHabitsid) {
            this.FoodHabitsid = FoodHabitsid;
        }

        public String getFoodHabitsName() {
            return FoodHabitsName;
        }

        public void setFoodHabitsName(String FoodHabitsName) {
            this.FoodHabitsName = FoodHabitsName;
        }
    }
}
