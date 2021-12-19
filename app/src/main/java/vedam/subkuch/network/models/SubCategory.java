package vedam.subkuch.network.models;

public class SubCategory {

    private String SubCategoryCityId;

    private String Description;

    private String SubCategoryName;

    public String getSubCategoryId() {
        return SubCategoryCityId;
    }

    public void setSubCategoryId(String SubCategoryCityId) {
        this.SubCategoryCityId = SubCategoryCityId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String SubCategoryName) {
        this.SubCategoryName = SubCategoryName;
    }

    @Override
    public String toString() {
        return SubCategoryName;
    }
}
