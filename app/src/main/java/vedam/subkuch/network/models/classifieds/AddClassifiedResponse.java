package vedam.subkuch.network.models.classifieds;

public class AddClassifiedResponse {
    private int ReturnCode;

    private Classified ReturnData;

    private String ReturnMessage;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public Classified getClassified() {
        return ReturnData;
    }

    public void setClassified(Classified ReturnData) {
        this.ReturnData = ReturnData;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }
}
