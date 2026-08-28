package vedam.subkuch.network.models.learn;

import java.util.ArrayList;

public class LearnCourseDetailsData {
    private LearnCourse Course;
    private ArrayList<LearnChapter> Chapters;
    private double MyRating;

    public LearnCourse getCourse() { return Course; }
    public ArrayList<LearnChapter> getChapters() { return Chapters; }
    public double getMyRating() { return MyRating; }
}
