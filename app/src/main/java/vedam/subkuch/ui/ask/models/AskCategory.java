package vedam.subkuch.ui.ask.models;

import android.support.annotation.NonNull;

public class AskCategory {
    private String categoryname;

    private String categoryid;

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getId() {
        return categoryid;
    }

    public void setId(String categoryid) {
        this.categoryid = categoryid;
    }

    @NonNull
    @Override
    public String toString() {
        return categoryname;
    }
}
