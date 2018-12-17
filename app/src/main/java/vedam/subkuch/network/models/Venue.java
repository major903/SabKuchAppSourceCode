package vedam.subkuch.network.models;

public class Venue {

    private String venuewithtime;

    public String getVenuewithtime ()
    {
        return venuewithtime;
    }

    public void setVenuewithtime (String venuewithtime)
    {
        this.venuewithtime = venuewithtime;
    }

    @Override
    public String toString()
    {
        return "Venue [venuewithtime = "+venuewithtime+"]";
    }
}
