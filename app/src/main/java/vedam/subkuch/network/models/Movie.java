package vedam.subkuch.network.models;

import java.util.ArrayList;

public class Movie {

    private String Name;

    private ArrayList<Venue> obj_Venue;

    private String Movievideo;

    private String Movieid;

    private String Movieposter;

    private boolean isExpanded;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public ArrayList<Venue> getObj_Venue() {
        return obj_Venue;
    }

    public void setObj_Venue(ArrayList<Venue> obj_Venue) {
        this.obj_Venue = obj_Venue;
    }

    public String getMovievideo() {
        return Movievideo;
    }

    public void setMovievideo(String Movievideo) {
        this.Movievideo = Movievideo;
    }

    public String getMovieid() {
        return Movieid;
    }

    public void setMovieid(String Movieid) {
        this.Movieid = Movieid;
    }

    public String getMovieposter() {
        return Movieposter;
    }

    public void setMovieposter(String Movieposter) {
        this.Movieposter = Movieposter;
    }

    @Override
    public String toString() {
        return "Movie [Name = " + Name + ", obj_Venue = " + obj_Venue + ", Movievideo = " + Movievideo + ", Movieid = " + Movieid + ", Movieposter = " + Movieposter + "]";
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
