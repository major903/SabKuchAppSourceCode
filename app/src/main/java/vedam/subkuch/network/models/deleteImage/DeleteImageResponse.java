package vedam.subkuch.network.models.deleteImage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteImageResponse {

@SerializedName("ReturnMessage")
@Expose
private String returnMessage;
@SerializedName("ReturnCode")
@Expose
private String returnCode;
@SerializedName("ReturnData")
@Expose
private Object returnData;

public String getReturnMessage() {
return returnMessage;
}

public void setReturnMessage(String returnMessage) {
this.returnMessage = returnMessage;
}

public String getReturnCode() {
return returnCode;
}

public void setReturnCode(String returnCode) {
this.returnCode = returnCode;
}

public Object getReturnData() {
return returnData;
}

public void setReturnData(Object returnData) {
this.returnData = returnData;
}

}