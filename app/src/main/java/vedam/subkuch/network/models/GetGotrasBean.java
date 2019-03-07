package vedam.subkuch.network.models;

import java.util.List;

public class GetGotrasBean {

    /**
     * ReturnCode : 1
     * ReturnMessage : success
     * ReturnData : [{"gotrasid":1,"Name":"Aatharvas"},{"gotrasid":2,"Name":"Agasthi"},{"gotrasid":3,"Name":"Ahabhunasa"},{"gotrasid":4,"Name":"Airan"},{"gotrasid":5,"Name":"Alampayana"},{"gotrasid":6,"Name":"Angiras"},{"gotrasid":7,"Name":"Arrishinimi"},{"gotrasid":8,"Name":"Athrevasva/Athrevasa"},{"gotrasid":9,"Name":"Atri"},{"gotrasid":10,"Name":"Attarishi"},{"gotrasid":11,"Name":"Aukshanas"},{"gotrasid":12,"Name":"Babrahvya"},{"gotrasid":13,"Name":"Badarayana"},{"gotrasid":14,"Name":"Baivayas"},{"gotrasid":15,"Name":"Bansal"},{"gotrasid":16,"Name":"Bashan"},{"gotrasid":17,"Name":"Bhandal"},{"gotrasid":18,"Name":"Bharadwaj"},{"gotrasid":19,"Name":"Bhargava/Bhargav"},{"gotrasid":20,"Name":"Bhasyan"},{"gotrasid":21,"Name":"Bhrigu"},{"gotrasid":22,"Name":"Bindal"},{"gotrasid":23,"Name":"Birthare"},{"gotrasid":24,"Name":"Bodhaaynas"},{"gotrasid":25,"Name":"Chandratri"},{"gotrasid":26,"Name":"Chikithasa"},{"gotrasid":27,"Name":"Chyavanasa"},{"gotrasid":28,"Name":"Daksha"},{"gotrasid":29,"Name":"Dalabhya"},{"gotrasid":30,"Name":"Darbhas"},{"gotrasid":31,"Name":"Devrata"},{"gotrasid":32,"Name":"Dhananjaya"},{"gotrasid":33,"Name":"Dhanvantri"},{"gotrasid":34,"Name":"Dhara Gautam"},{"gotrasid":35,"Name":"Dharan"},{"gotrasid":36,"Name":"Dharanas"},{"gotrasid":37,"Name":"Dixit"},{"gotrasid":38,"Name":"Duttatreyas"},{"gotrasid":39,"Name":"Galiva"},{"gotrasid":40,"Name":"Ganganas"},{"gotrasid":41,"Name":"Gangyanas"},{"gotrasid":42,"Name":"Gardhmukh Sandilya"},{"gotrasid":43,"Name":"Garg"},{"gotrasid":44,"Name":"Garga/Gargya"},{"gotrasid":45,"Name":"Gargya Sainasa"},{"gotrasid":46,"Name":"Gautam/Gouthama"},{"gotrasid":47,"Name":"Ghrit Kaushika"},{"gotrasid":48,"Name":"Goyal"},{"gotrasid":49,"Name":"Goyan"},{"gotrasid":50,"Name":"Haritasya/Harithasa/Haritha"},{"gotrasid":51,"Name":"Jaiminiyas"},{"gotrasid":52,"Name":"Jamadagni"},{"gotrasid":53,"Name":"Jatukarna"},{"gotrasid":54,"Name":"Jindal"},{"gotrasid":55,"Name":"Kaakavas"},{"gotrasid":56,"Name":"Kabi"},{"gotrasid":57,"Name":"Kalabouddasa"},{"gotrasid":58,"Name":"Kalpangeerasa"},{"gotrasid":59,"Name":"Kamakayana Vishwamitra"},{"gotrasid":60,"Name":"Kamsa"},{"gotrasid":61,"Name":"Kanav"},{"gotrasid":62,"Name":"Kansal"},{"gotrasid":63,"Name":"Kanva"},{"gotrasid":64,"Name":"Kapi"},{"gotrasid":65,"Name":"Kapila Baradwaj"},{"gotrasid":66,"Name":"Kapinjal"},{"gotrasid":67,"Name":"Kapishthalas"},{"gotrasid":68,"Name":"Kaplish"},{"gotrasid":69,"Name":"Kashish"},{"gotrasid":70,"Name":"Kashyapa/Kaashyapa"},{"gotrasid":71,"Name":"Katyayan/Katyan"},{"gotrasid":72,"Name":"Kaundinya/Koundanya/Kaundilya"},{"gotrasid":73,"Name":"Kaunsa"},{"gotrasid":74,"Name":"Kaushal"},{"gotrasid":75,"Name":"Kaushika/Kaushik/Kausikasa"},{"gotrasid":76,"Name":"Keshoryas"},{"gotrasid":77,"Name":"Koushika Visvamitrasa"},{"gotrasid":78,"Name":"Krishnatrey"},{"gotrasid":79,"Name":"Kucchal"},{"gotrasid":80,"Name":"Kusa"},{"gotrasid":81,"Name":"Kutsa/Kutsas/Kutsasa"},{"gotrasid":82,"Name":"Laakshmanas"},{"gotrasid":83,"Name":"Laugakshi"},{"gotrasid":84,"Name":"Lavania"},{"gotrasid":85,"Name":"Lodwan"},{"gotrasid":86,"Name":"Lohit"},{"gotrasid":87,"Name":"Lokaahyas"},{"gotrasid":88,"Name":"Lomasha"},{"gotrasid":89,"Name":"Madelia"},{"gotrasid":90,"Name":"Madhukul"},{"gotrasid":91,"Name":"Maitraya"},{"gotrasid":92,"Name":"Manava"},{"gotrasid":93,"Name":"Mandavya"},{"gotrasid":94,"Name":"Mangal"},{"gotrasid":95,"Name":"Marica"},{"gotrasid":96,"Name":"Markendya"},{"gotrasid":97,"Name":"Maudlas"},{"gotrasid":98,"Name":"Maunas"},{"gotrasid":99,"Name":"Mihir"},{"gotrasid":100,"Name":"Mittal"},{"gotrasid":101,"Name":"Moudgalya"},{"gotrasid":102,"Name":"Mauna Bhargava"},{"gotrasid":103,"Name":"Munish"},{"gotrasid":104,"Name":"Mythravaruna"},{"gotrasid":105,"Name":"Naagal"},{"gotrasid":106,"Name":"Nagasya"},{"gotrasid":107,"Name":"Naidrupa Kashyapa"},{"gotrasid":108,"Name":"Narayanas"},{"gotrasid":109,"Name":"Nithyandala"},{"gotrasid":110,"Name":"Paaniyas"},{"gotrasid":111,"Name":"Pachori"},{"gotrasid":112,"Name":"Paing"},{"gotrasid":113,"Name":"Parashar/Parashara"},{"gotrasid":114,"Name":"Parthivasa"},{"gotrasid":115,"Name":"Paulastya"},{"gotrasid":116,"Name":"Poothamanasa"},{"gotrasid":117,"Name":"Pourugutsa"},{"gotrasid":118,"Name":"Prachinas"},{"gotrasid":119,"Name":"Raghuvanshi"},{"gotrasid":120,"Name":"Rajoria"},{"gotrasid":121,"Name":"Rathitar"},{"gotrasid":122,"Name":"Rohinya"},{"gotrasid":123,"Name":"Rohita"},{"gotrasid":124,"Name":"Sakalya"},{"gotrasid":125,"Name":"Sakhyanasa"},{"gotrasid":126,"Name":"Salankayanasa"},{"gotrasid":127,"Name":"Sankash"},{"gotrasid":128,"Name":"Sankha-Pingala-Kausta"},{"gotrasid":129,"Name":"Sankrut"},{"gotrasid":130,"Name":"Sankyanasa"},{"gotrasid":131,"Name":"Savanaka"},{"gotrasid":132,"Name":"Savarna/Sabarna/Savarna/Sraborna"},{"gotrasid":133,"Name":"Shaalaksha"},{"gotrasid":134,"Name":"Shadamarshana/Shatamarshanam"},{"gotrasid":135,"Name":"Sharkaras"},{"gotrasid":136,"Name":"Sharkvas"},{"gotrasid":137,"Name":"Shaunak"},{"gotrasid":138,"Name":"Shravanesya"},{"gotrasid":139,"Name":"Shrimukh Shandilya"},{"gotrasid":140,"Name":"Shukla Atreyas"},{"gotrasid":141,"Name":"Sigidha"},{"gotrasid":142,"Name":"Singhal"},{"gotrasid":143,"Name":"Sri Vatsa/Vatsa/Vats/Vacchas"},{"gotrasid":144,"Name":"Srungi Bharadwajasa"},{"gotrasid":145,"Name":"Suparnasa"},{"gotrasid":146,"Name":"Swathantra Kapisa"},{"gotrasid":147,"Name":"Tayal"},{"gotrasid":148,"Name":"Tarakayanam"},{"gotrasid":149,"Name":"Thingal"},{"gotrasid":150,"Name":"Titwal"},{"gotrasid":151,"Name":"Tushar"},{"gotrasid":152,"Name":"Udbahu"},{"gotrasid":153,"Name":"Udhalaka"},{"gotrasid":154,"Name":"Uditha Gautham"},{"gotrasid":155,"Name":"Udithya"},{"gotrasid":156,"Name":"Upamanyu Vasishtasa"},{"gotrasid":157,"Name":"Upamanyu"},{"gotrasid":158,"Name":"Upathya"},{"gotrasid":159,"Name":"Vadoola/Vadulasa"},{"gotrasid":160,"Name":"Vainya"},{"gotrasid":161,"Name":"Vardheyasa"},{"gotrasid":162,"Name":"Vashishtha"},{"gotrasid":163,"Name":"Veethahavya"},{"gotrasid":164,"Name":"Vishnordhageerasa"},{"gotrasid":165,"Name":"Vishnu Vridhha"},{"gotrasid":166,"Name":"Vishwamithra"},{"gotrasid":167,"Name":"Yaska"},{"gotrasid":168,"Name":"Others"},{"gotrasid":169,"Name":"Don't Know"}]
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
         * gotrasid : 1
         * Name : Aatharvas
         */

        private int gotrasid;
        private String Name;

        public ReturnDataBean(int gotrasid, String name) {
            this.gotrasid = gotrasid;
            Name = name;
        }

        public int getGotrasid() {
            return gotrasid;
        }

        public void setGotrasid(int gotrasid) {
            this.gotrasid = gotrasid;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
