package vedam.subkuch.ui.jobs;

import java.util.ArrayList;

public class Job {
    private String DealingIn;

    private String OrganisationName;

    private String JobLocation;

    private String HowToContact;

    private ArrayList<Post> posts;

    private String longitude;

    private String latitude;

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
