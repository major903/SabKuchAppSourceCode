package vedam.subkuch.network.models;


import java.util.ArrayList;

public class MoviesResponse {

    private String ReturnCode;

    private String ReturnMessage;

    private ArrayList<Movie> ReturnData;

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

    public ArrayList<Movie> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(ArrayList<Movie> ReturnData) {
        this.ReturnData = ReturnData;
    }

}
