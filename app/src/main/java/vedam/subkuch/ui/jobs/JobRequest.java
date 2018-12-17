package vedam.subkuch.ui.jobs;

import java.util.ArrayList;

public class JobRequest {
    private String CityID;

    private ArrayList<Post> Jobs;

    private String DealingIn;

    private String JobLocation;

    private String OrganisationName;

    private String HowToContact;

    private String longitude;

    private String latitude;

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String CityID) {
        this.CityID = CityID;
    }

    public ArrayList<Post> getJobs() {
        return Jobs;
    }

    public void setJobs(ArrayList<Post> Jobs) {
        this.Jobs = Jobs;
    }

    public String getDealingIn() {
        return DealingIn;
    }

    public void setDealingIn(String DealingIn) {
        this.DealingIn = DealingIn;
    }

    public String getJobLocation() {
        return JobLocation;
    }

    public void setJobLocation(String JobLocation) {
        this.JobLocation = JobLocation;
    }

    public String getOrganisationName() {
        return OrganisationName;
    }

    public void setOrganisationName(String OrganisationName) {
        this.OrganisationName = OrganisationName;
    }

    public String getHowToContact() {
        return HowToContact;
    }

    public void setHowToContact(String HowToContact) {
        this.HowToContact = HowToContact;
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
        return "ClassPojo [CityID = " + CityID + ", Jobs = " + Jobs + ", DealingIn = " + DealingIn + ", JobLocation = " + JobLocation + ", OrganisationName = " + OrganisationName + ", HowToContact = " + HowToContact + ", longitude = " + longitude + ", latitude = " + latitude + "]";
    }
}
