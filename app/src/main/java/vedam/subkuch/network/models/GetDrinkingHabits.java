package vedam.subkuch.network.models;

import java.util.ArrayList;

public class GetDrinkingHabits {

    private String ReturnCode;

    private ArrayList<DrinkingHabits> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<DrinkingHabits> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<DrinkingHabits> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
