package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ContribListResponse {

    @SerializedName("ReturnCode")
    private int returnCode;

    @SerializedName("ReturnMessage")
    private String returnMessage;

    @SerializedName("ReturnData")
    private ContribListData returnData;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public ContribListData getReturnData() {
        return returnData;
    }

    public void setReturnData(ContribListData returnData) {
        this.returnData = returnData;
    }

    public static class ContribListData {

        @SerializedName("TotalCount")
        private int totalCount;

        @SerializedName("PageIndex")
        private int pageIndex;

        @SerializedName("PageSize")
        private int pageSize;

        @SerializedName("Contribs")
        private List<ContribItem> contribs = new ArrayList<>();

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            this.pageIndex = pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<ContribItem> getContribs() {
            return contribs != null ? contribs : new ArrayList<>();
        }

        public void setContribs(List<ContribItem> contribs) {
            this.contribs = contribs;
        }
    }
}
