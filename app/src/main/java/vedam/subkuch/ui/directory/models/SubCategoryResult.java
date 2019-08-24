package vedam.subkuch.ui.directory.models;

import java.util.ArrayList;

import vedam.subkuch.network.models.SubCategory;

public class SubCategoryResult {
    private ArrayList<SubCategory> SubCategories;

    public ArrayList<SubCategory> getSubCategories ()
    {
        return SubCategories;
    }

    public void setSubCategories (ArrayList<SubCategory> SubCategories)
    {
        this.SubCategories = SubCategories;
    }
}
