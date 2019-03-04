package vedam.subkuch.network.models.insertImage;

import java.util.List;

public class InsertImageResponse {


    /**
     * ReturnMessage : success
     * ReturnCode : 1
     * ReturnData : [{"Imagedataid":378,"Userid":100,"Image":"http://sabkuch.visitmydemo.xyz/APIIMAGE/636791175405603911c5136.jpg"}]
     */

    private String ReturnMessage;
    private String ReturnCode;
    private List<ReturnDataBean> ReturnData;

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public List<ReturnDataBean> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(List<ReturnDataBean> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean {
        /**
         * Imagedataid : 378
         * Userid : 100
         * Image : http://sabkuch.visitmydemo.xyz/APIIMAGE/636791175405603911c5136.jpg
         */

        private int Imagedataid;
        private int Userid;
        private String Image;

        public int getImagedataid() {
            return Imagedataid;
        }

        public void setImagedataid(int Imagedataid) {
            this.Imagedataid = Imagedataid;
        }

        public int getUserid() {
            return Userid;
        }

        public void setUserid(int Userid) {
            this.Userid = Userid;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String Image) {
            this.Image = Image;
        }
    }
}