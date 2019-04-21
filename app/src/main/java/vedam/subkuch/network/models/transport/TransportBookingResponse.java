package vedam.subkuch.network.models.transport;

import java.util.ArrayList;

public class TransportBookingResponse {

    private String ReturnCode;

    private ArrayList<TransportBooking> ReturnData;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public ArrayList<TransportBooking> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<TransportBooking> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
