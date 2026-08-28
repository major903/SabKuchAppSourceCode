package vedam.subkuch.network.models.learn;

import java.util.ArrayList;

public class CoursesByCategoryData {
    private LearnCategory Category;
    private ArrayList<LearnCourse> Courses;

    public LearnCategory getCategory() { return Category; }
    public ArrayList<LearnCourse> getCourses() { return Courses; }
}
