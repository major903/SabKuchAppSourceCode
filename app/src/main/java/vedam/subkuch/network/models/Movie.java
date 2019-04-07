package vedam.subkuch.network.models;

import java.util.ArrayList;

public class Movie {

    private String name;

    private ArrayList<Venue> obj_Venue;

    private String movievideo;

    private String movieid;

    private String movieposter;

    private boolean isExpanded;

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public ArrayList<Venue> getObj_Venue() {
        return obj_Venue;
    }

    public void setObj_Venue(ArrayList<Venue> obj_Venue) {
        this.obj_Venue = obj_Venue;
    }

    public String getMovievideo() {
        return movievideo;
    }

    public void setMovievideo(String Movievideo) {
        this.movievideo = Movievideo;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String Movieid) {
        this.movieid = Movieid;
    }

    public String getMovieposter() {
        return movieposter;
    }

    public void setMovieposter(String Movieposter) {
        this.movieposter = Movieposter;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
