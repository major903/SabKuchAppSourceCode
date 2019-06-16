package vedam.subkuch.ui.offers.models;

import java.util.ArrayList;

public class OfferResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Offer> ReturnData;

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

    public ArrayList<Offer> getReturnData() {
        return ReturnData;

    }
}
