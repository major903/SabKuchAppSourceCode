package vedam.subkuch.network.models.getLiving;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnDatum {

@SerializedName("Livingwithid")
@Expose
private Integer livingwithid;
@SerializedName("Livingwithname")
@Expose
private String livingwithname;

public Integer getLivingwithid() {
return livingwithid;
}

public void setLivingwithid(Integer livingwithid) {
this.livingwithid = livingwithid;
}

public String getLivingwithname() {
return livingwithname;
}

public void setLivingwithname(String livingwithname) {
this.livingwithname = livingwithname;
}

}