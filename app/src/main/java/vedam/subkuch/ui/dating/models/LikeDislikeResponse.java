package vedam.subkuch.ui.dating.models;

import com.google.gson.annotations.SerializedName;

public class LikeDislikeResponse {
    private String ReturnCode;

    @SerializedName("ReturnData")
    private LikeDislike LikeDislike;

    private String ReturnMessage;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public LikeDislike getLikeDislike() {
        return LikeDislike;
    }

    public void setLikeDislike(LikeDislike LikeDislike) {
        this.LikeDislike = LikeDislike;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    @Override
    public String toString() {
        return "ClassPojo [ReturnCode = " + ReturnCode + ", LikeDislike = " + LikeDislike + ", ReturnMessage = " + ReturnMessage + "]";
    }
}
