package vedam.subkuch.ui.directory.models;

public class CategoryResult {
    private Category[] Categories;

    public Category[] getCategories() {
        return Categories;
    }

    public void setCategories(Category[] Categories) {
        this.Categories = Categories;
    }

    @Override
    public String toString() {
        return "ClassPojo [Categories = " + Categories + "]";
    }
}
