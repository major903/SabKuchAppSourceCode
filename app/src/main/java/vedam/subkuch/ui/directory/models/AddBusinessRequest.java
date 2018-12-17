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

    private String Mobile;

    private String BusinessName;

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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String BusinessName) {
        this.BusinessName = BusinessName;
    }

    @Override
    public String toString() {
        return "ClassPojo [BusinessImage = " + BusinessImage + ", Phone = " + Phone + ", BusinessAddresses = " + BusinessAddresses + ", SubCategoryID = " + SubCategoryID + ", Email = " + Email + ", ContactPerson = " + ContactPerson + ", Website = " + Website + ", CategoryID = " + CategoryID + ", Mobile = " + Mobile + ", BusinessName = " + BusinessName + "]";
    }
}
