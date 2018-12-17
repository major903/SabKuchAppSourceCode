package vedam.subkuch.ui.directory.models;

import java.util.ArrayList;

public class BusinessesResult {
    private ArrayList<Business> Businesses;

    public ArrayList<Business> getBusinesses() {
        return Businesses;
    }

    public void setBusinesses(ArrayList<Business> Businesses) {
        this.Businesses = Businesses;
    }

    @Override
    public String toString() {
        return "ClassPojo [Businesses = " + Businesses + "]";
    }
}
