package vedam.subkuch.ui.directory.models;

import java.util.ArrayList;

public class AddBusinessRequest {

    private String BusinessImage;

    private String Phone;

    private ArrayList<BusinessAddress> BusinessAddresses;

    private String SubCategoryID;

    private String Email;

    private String ContactPerson;

    private String Website;

    private String CategoryID;

    private String BusinessName;

    private String countryid;

    private String cityid;

    public String getBusinessImage() {
        return BusinessImage;
    }

    public void setBusinessImage(String BusinessImage) {
        this.BusinessImage = BusinessImage;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public ArrayList<BusinessAddress> getBusinessAddresses() {
        return BusinessAddresses;
    }

    public void setBusinessAddresses(ArrayList<BusinessAddress> BusinessAddresses) {
        this.BusinessAddresses = BusinessAddresses;
    }

    public String getSubCategoryID() {
        return SubCategoryID;
    }

    public void setSubCategoryID(String SubCategoryID) {
        this.SubCategoryID = SubCategoryID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String CategoryID) {
        this.CategoryID = CategoryID;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    @Override
    public String toString() {
        return "ClassPojo [BusinessImage = " + BusinessImage + ", Phone = " + Phone + ", BusinessAddresses = " + BusinessAddresses + ", SubCategoryID = " + SubCategoryID + ", Email = " + Email + ", ContactPerson = " + ContactPerson + ", Website = " + Website + ", CategoryID = " + CategoryID + ", BusinessName = " + BusinessName + ", countryid = " + countryid + ", cityid = " + cityid + "]";
    }
}
