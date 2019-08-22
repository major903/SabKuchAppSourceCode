package vedam.subkuch.network.models.classifieds;

import androidx.annotation.NonNull;

public class ClassifiedSubCategory {

    private String CategoryId;

    private String SubCategoryId;

    private String SubCategory;

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String SubCategoryId) {
        this.SubCategoryId = SubCategoryId;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String SubCategory) {
        this.SubCategory = SubCategory;
    }

    @NonNull
    @Override
    public String toString() {
        return SubCategory;
    }
}
