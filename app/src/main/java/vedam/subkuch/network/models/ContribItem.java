package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

public class ContribItem {

    @SerializedName("ContribId")
    private int contribId;

    @SerializedName("Type")
    private String type;

    @SerializedName("Detail")
    private String detail;

    @SerializedName("CreatedDate")
    private String createdDate;

    public ContribItem() {
    }

    public ContribItem(int contribId, String type, String detail) {
        this.contribId = contribId;
        this.type = type;
        this.detail = detail;
    }

    public int getContribId() {
        return contribId;
    }

    public void setContribId(int contribId) {
        this.contribId = contribId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
