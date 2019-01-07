package vedam.subkuch.ui.ask.models;

import java.util.ArrayList;

public class ConversationResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Conversation> ReturnData;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public ArrayList<Conversation> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Conversation> ReturnData) {
        this.ReturnData = ReturnData;
    }
}
