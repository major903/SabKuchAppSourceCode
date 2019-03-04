package vedam.subkuch.network.models.getReligion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnDatum {

@SerializedName("Religionid")
@Expose
private Integer religionid;
@SerializedName("Religionname")
@Expose
private String religionname;

public Integer getReligionid() {
return religionid;
}

public void setReligionid(Integer religionid) {
this.religionid = religionid;
}

public String getReligionname() {
return religionname;
}

public void setReligionname(String religionname) {
this.religionname = religionname;
}

}