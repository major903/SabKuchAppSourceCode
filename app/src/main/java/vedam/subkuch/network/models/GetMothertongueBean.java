package vedam.subkuch.network.models;

import java.util.List;

public class GetMothertongueBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"mothertongueid":1,"mothertongueName":"Aka"},{"mothertongueid":2,"mothertongueName":"Arabic"},{"mothertongueid":3,"mothertongueName":"Arunachali"},{"mothertongueid":4,"mothertongueName":"Assamese"},{"mothertongueid":5,"mothertongueName":"Awadhi"},{"mothertongueid":6,"mothertongueName":"Baluchi"},{"mothertongueid":7,"mothertongueName":"Bengali"},{"mothertongueid":8,"mothertongueName":"Bhojpuri"},{"mothertongueid":9,"mothertongueName":"Bhutia"},{"mothertongueid":10,"mothertongueName":"Brahui"},{"mothertongueid":11,"mothertongueName":"Brij"},{"mothertongueid":12,"mothertongueName":"Burmese"},{"mothertongueid":13,"mothertongueName":"Byari"},{"mothertongueid":14,"mothertongueName":"Chattisgarhi"},{"mothertongueid":15,"mothertongueName":"Chinese"},{"mothertongueid":16,"mothertongueName":"Coorgi"},{"mothertongueid":17,"mothertongueName":"Dogri"},{"mothertongueid":18,"mothertongueName":"English"},{"mothertongueid":19,"mothertongueName":"French"},{"mothertongueid":20,"mothertongueName":"Garo"},{"mothertongueid":21,"mothertongueName":"Garwali"},{"mothertongueid":22,"mothertongueName":"Gujarati"},{"mothertongueid":23,"mothertongueName":"Haryanavi"},{"mothertongueid":24,"mothertongueName":"Himachali/pahari"},{"mothertongueid":25,"mothertongueName":"Hindi"},{"mothertongueid":26,"mothertongueName":"Hindko"},{"mothertongueid":27,"mothertongueName":"Kakbarak"},{"mothertongueid":28,"mothertongueName":"Kanauji"},{"mothertongueid":29,"mothertongueName":"Kannada"},{"mothertongueid":30,"mothertongueName":"Kashmiri"},{"mothertongueid":31,"mothertongueName":"Khandesi"},{"mothertongueid":32,"mothertongueName":"Khasi"},{"mothertongueid":33,"mothertongueName":"Konkani"},{"mothertongueid":34,"mothertongueName":"Koshali"},{"mothertongueid":35,"mothertongueName":"Kumaoni"},{"mothertongueid":36,"mothertongueName":"Kutchi"},{"mothertongueid":37,"mothertongueName":"Ladakhi"},{"mothertongueid":38,"mothertongueName":"Lepcha"},{"mothertongueid":39,"mothertongueName":"Magahi"},{"mothertongueid":40,"mothertongueName":"Maithili"},{"mothertongueid":41,"mothertongueName":"Malaya"},{"mothertongueid":42,"mothertongueName":"Malayalam"},{"mothertongueid":43,"mothertongueName":"Manipuri"},{"mothertongueid":44,"mothertongueName":"Marathi"},{"mothertongueid":45,"mothertongueName":"Marwari"},{"mothertongueid":46,"mothertongueName":"Miji"},{"mothertongueid":47,"mothertongueName":"Mizo"},{"mothertongueid":48,"mothertongueName":"Monpa"},{"mothertongueid":49,"mothertongueName":"More"},{"mothertongueid":50,"mothertongueName":"Nepali"},{"mothertongueid":51,"mothertongueName":"Odia"},{"mothertongueid":52,"mothertongueName":"Pashto"},{"mothertongueid":53,"mothertongueName":"Persian"},{"mothertongueid":54,"mothertongueName":"Punjabi"},{"mothertongueid":55,"mothertongueName":"Rajasthani"},{"mothertongueid":56,"mothertongueName":"Russian"},{"mothertongueid":57,"mothertongueName":"Sanskrit"},{"mothertongueid":58,"mothertongueName":"Santhali"},{"mothertongueid":59,"mothertongueName":"Seraiki"},{"mothertongueid":60,"mothertongueName":"Sindhi"},{"mothertongueid":61,"mothertongueName":"Sinhala"},{"mothertongueid":62,"mothertongueName":"Sourashtra"},{"mothertongueid":63,"mothertongueName":"Spanish"},{"mothertongueid":64,"mothertongueName":"Swedish"},{"mothertongueid":65,"mothertongueName":"Tagalog"},{"mothertongueid":66,"mothertongueName":"Tamil"},{"mothertongueid":67,"mothertongueName":"Telugu"},{"mothertongueid":68,"mothertongueName":"Tulu"},{"mothertongueid":69,"mothertongueName":"Urdu"},{"mothertongueid":70,"mothertongueName":"Other"}]
     */

    private int ReturnCode;
    private String ReturnMessage;
    private List<ReturnDataBean> ReturnData;

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public List<ReturnDataBean> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(List<ReturnDataBean> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean {
        /**
         * mothertongueid : 1
         * mothertongueName : Aka
         */

        private int mothertongueid;
        private String mothertongueName;

        public int getMothertongueid() {
            return mothertongueid;
        }

        public void setMothertongueid(int mothertongueid) {
            this.mothertongueid = mothertongueid;
        }

        public String getMothertongueName() {
            return mothertongueName;
        }

        public void setMothertongueName(String mothertongueName) {
            this.mothertongueName = mothertongueName;
        }
    }
}
