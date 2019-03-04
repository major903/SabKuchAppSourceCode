package vedam.subkuch.network.models.getMasterCast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnDatum {

@SerializedName("Mastercasteid")
@Expose
private Integer mastercasteid;
@SerializedName("Mastercastname")
@Expose
private String mastercastname;

public Integer getMastercasteid() {
return mastercasteid;
}

public void setMastercasteid(Integer mastercasteid) {
this.mastercasteid = mastercasteid;
}

public String getMastercastname() {
return mastercastname;
}

public void setMastercastname(String mastercastname) {
this.mastercastname = mastercastname;
}

}