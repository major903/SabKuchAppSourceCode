package vedam.subkuch.ui.offers;

public class Offer {

    private String Imageurl;
    private String RedirectURL;

    public String getRedirectURL() {
        return RedirectURL;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String Imageurl) {
        this.Imageurl = Imageurl;
    }

    @Override
    public String toString() {
        return "Offer [Imageurl = " + Imageurl + "]";
    }
}
