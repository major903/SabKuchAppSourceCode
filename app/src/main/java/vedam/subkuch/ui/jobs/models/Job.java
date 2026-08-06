package vedam.subkuch.ui.jobs.models;

import java.util.ArrayList;

public class Job {
    private String DealingIn;

    private String OrganisationName;

    private String JobLocation;

    private String HowToContact;
    private String Mobile1;
    private String Mobile2;
    private String Email;
    private Boolean IsCall;
    private Boolean IsWhatsApp;

    private ArrayList<Post> posts;

    private String longitude;

    private String latitude;

    private String Distance;

    private String City;

    public String getCity() {
        return City;
    }

    public String getDistance() {
        return Distance;
    }

    public String getDealingIn() {
        return DealingIn;
    }

    public void setDealingIn(String DealingIn) {
        this.DealingIn = DealingIn;
    }

    public String getOrganisationName() {
        return OrganisationName;
    }

    public void setOrganisationName(String OrganisationName) {
        this.OrganisationName = OrganisationName;
    }

    public String getJobLocation() {
        return JobLocation;
    }

    public void setJobLocation(String JobLocation) {
        this.JobLocation = JobLocation;
    }

    public String getHowToContact() {
        return HowToContact;
    }

    public void setHowToContact(String HowToContact) {
        this.HowToContact = HowToContact;
    }

    public String getMobile1() { return Mobile1; }
    public void setMobile1(String mobile1) { Mobile1 = mobile1; }
    public String getMobile2() { return Mobile2; }
    public void setMobile2(String mobile2) { Mobile2 = mobile2; }
    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }
    public Boolean getIsCall() { return IsCall; }
    public void setIsCall(Boolean isCall) { IsCall = isCall; }
    public Boolean getIsWhatsApp() { return IsWhatsApp; }
    public void setIsWhatsApp(Boolean isWhatsApp) { IsWhatsApp = isWhatsApp; }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "ClassPojo [DealingIn = " + DealingIn + ", OrganisationName = " + OrganisationName + ", JobLocation = " + JobLocation + ", HowToContact = " + HowToContact + ", posts = " + posts + ", longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}
