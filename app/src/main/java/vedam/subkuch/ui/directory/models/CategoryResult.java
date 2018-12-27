package vedam.subkuch.ui.directory.models;

import java.util.ArrayList;

public class CategoryResult {
    private ArrayList<Category> Categories;

    public ArrayList<Category> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<Category> Categories) {
        this.Categories = Categories;
    }

    @Override
    public String toString() {
        return "ClassPojo [Categories = " + Categories + "]";
    }
}
