package vedam.subkuch.ui.directory.models;

public class Category {
    private String Name;

    private String Description;

    private String CategoryId;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getCategoryId ()
    {
        return CategoryId;
    }

    public void setCategoryId (String CategoryId)
    {
        this.CategoryId = CategoryId;
    }

    @Override
    public String toString()
    {
        return Name;
    }
}
