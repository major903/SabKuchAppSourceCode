package vedam.subkuch.ui.ask;

import android.support.annotation.NonNull;

public class AskCategory {
    private String categoryname;

    private String Id;

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    @NonNull
    @Override
    public String toString() {
        return categoryname;
    }
}
