package vedam.subkuch.network.models.insertImage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReturnData {

@SerializedName("returnimageid")
@Expose
private List<Integer> returnimageid = null;

public List<Integer> getReturnimageid() {
return returnimageid;
}

public void setReturnimageid(List<Integer> returnimageid) {
this.returnimageid = returnimageid;
}

}