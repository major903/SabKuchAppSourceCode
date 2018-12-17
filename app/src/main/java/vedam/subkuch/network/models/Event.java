package vedam.subkuch.network.models;

public class Event {

    private String EventImage;

    private String EntryFee;

    private String Time;

    private String Venue;

    private String Date;

    private String ID;

    private String About;

    private String Title;

    public String getEntryFee() {
        return EntryFee;
    }

    public String getEventImage() {
        return EventImage;
    }

    public void setEventImage(String EventImage) {
        this.EventImage = EventImage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String Venue) {
        this.Venue = Venue;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String About) {
        this.About = About;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    @Override
    public String toString() {
        return "Event [EventImage = " + EventImage + ", Time = " + Time + ", Venue = " + Venue + ", Date = " + Date + ", ID = " + ID + ", About = " + About + ", Title = " + Title + "]";
    }
}
