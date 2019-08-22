package vedam.subkuch.network.models.classifieds;

import androidx.annotation.NonNull;

public class ClassifiedCategory {
    private String CategoryId;

    private String AdLimit;

    private String Category;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getAdLimit() {
        return AdLimit;
    }

    public void setAdLimit(String AdLimit) {
        this.AdLimit = AdLimit;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    @NonNull
    @Override
    public String toString() {
        return Category;
    }
}
