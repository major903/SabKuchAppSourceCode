package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

public class Broadcast {
    @SerializedName(value = "message1", alternate = {"Message1"})
    private String Message1;

    public String getMessage1() {
        return Message1;
    }
}
