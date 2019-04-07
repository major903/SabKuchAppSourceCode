package vedam.subkuch.network.models;


import java.util.ArrayList;

public class MoviesResponse {

    private String returnCode;

    private String returnMessage;

    private ArrayList<Movie> returnData;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.returnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.returnMessage = ReturnMessage;
    }

    public ArrayList<Movie> getReturnData() {
        return returnData;
    }

    public void setReturnData(ArrayList<Movie> ReturnData) {
        this.returnData = ReturnData;
    }

}
