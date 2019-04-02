package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

public class ShareResponse {
    private boolean success;
    @SerializedName("date")
    private ShareObject data;

    public boolean isSuccess() {
        return success;
    }

    public ShareObject getData() {
        return data;
    }
}
