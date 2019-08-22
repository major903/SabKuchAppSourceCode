package vedam.subkuch.network.models.classifieds;

import android.os.Parcel;
import android.os.Parcelable;

public class Classified implements Parcelable {
    private String CategoryId;

    private String Category;

    private String StatusText;

    private String CreatedAt;

    private String Latitude;

    private String CityId;

    private String Currency;

    private String Distance;

    private String Status;

    private String Locality;

    private String Rate;

    private String SubCategoryId;

    private String Title;

    private String ImageUrl;

    private String TomorrowsPrice;

    private String CityName;

    private String TodaysPrice;

    private String UpdatedAt;

    private String Longitude;

    private String FormattedDistance;

    private String About;

    private String Contact;

    private String UserId;

    private String SubCategory;

    private String DailyDiscount;

    private String ClassifiedAdId;

    private String ExpiresAt;

    private String Location;

    private String UserName;
    private String PostedAdId;
    private String FormattedRate;

    public Classified() {
    }


    protected Classified(Parcel in) {
        CategoryId = in.readString();
        Category = in.readString();
        StatusText = in.readString();
        CreatedAt = in.readString();
        Latitude = in.readString();
        CityId = in.readString();
        Currency = in.readString();
        Distance = in.readString();
        Status = in.readString();
        Locality = in.readString();
        Rate = in.readString();
        SubCategoryId = in.readString();
        Title = in.readString();
        ImageUrl = in.readString();
        TomorrowsPrice = in.readString();
        CityName = in.readString();
        TodaysPrice = in.readString();
        UpdatedAt = in.readString();
        Longitude = in.readString();
        FormattedDistance = in.readString();
        About = in.readString();
        Contact = in.readString();
        UserId = in.readString();
        SubCategory = in.readString();
        DailyDiscount = in.readString();
        ClassifiedAdId = in.readString();
        ExpiresAt = in.readString();
        Location = in.readString();
        UserName = in.readString();
        PostedAdId = in.readString();
        FormattedRate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CategoryId);
        dest.writeString(Category);
        dest.writeString(StatusText);
        dest.writeString(CreatedAt);
        dest.writeString(Latitude);
        dest.writeString(CityId);
        dest.writeString(Currency);
        dest.writeString(Distance);
        dest.writeString(Status);
        dest.writeString(Locality);
        dest.writeString(Rate);
        dest.writeString(SubCategoryId);
        dest.writeString(Title);
        dest.writeString(ImageUrl);
        dest.writeString(TomorrowsPrice);
        dest.writeString(CityName);
        dest.writeString(TodaysPrice);
        dest.writeString(UpdatedAt);
        dest.writeString(Longitude);
        dest.writeString(FormattedDistance);
        dest.writeString(About);
        dest.writeString(Contact);
        dest.writeString(UserId);
        dest.writeString(SubCategory);
        dest.writeString(DailyDiscount);
        dest.writeString(ClassifiedAdId);
        dest.writeString(ExpiresAt);
        dest.writeString(Location);
        dest.writeString(UserName);
        dest.writeString(PostedAdId);
        dest.writeString(FormattedRate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Classified> CREATOR = new Creator<Classified>() {
        @Override
        public Classified createFromParcel(Parcel in) {
            return new Classified(in);
        }

        @Override
        public Classified[] newArray(int size) {
            return new Classified[size];
        }
    };

    public String getFormattedRate() {
        return FormattedRate;
    }

    public String getPostedAdId() {
        return PostedAdId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getStatusText() {
        return StatusText;
    }

    public void setStatusText(String StatusText) {
        this.StatusText = StatusText;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String Distance) {
        this.Distance = Distance;
    }

    public String

    getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String Locality) {
        this.Locality = Locality;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String Rate) {
        this.Rate = Rate;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String SubCategoryId) {
        this.SubCategoryId = SubCategoryId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getTomorrowsPrice() {
        return TomorrowsPrice;
    }

    public void setTomorrowsPrice(String TomorrowsPrice) {
        this.TomorrowsPrice = TomorrowsPrice;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getTodaysPrice() {
        return TodaysPrice;
    }

    public void setTodaysPrice(String TodaysPrice) {
        this.TodaysPrice = TodaysPrice;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String UpdatedAt) {
        this.UpdatedAt = UpdatedAt;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getFormattedDistance() {
        return FormattedDistance;
    }

    public void setFormattedDistance(String FormattedDistance) {
        this.FormattedDistance = FormattedDistance;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String About) {
        this.About = About;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String Contact) {
        this.Contact = Contact;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String SubCategory) {
        this.SubCategory = SubCategory;
    }

    public String getDailyDiscount() {
        return DailyDiscount;
    }

    public void setDailyDiscount(String DailyDiscount) {
        this.DailyDiscount = DailyDiscount;
    }

    public String getClassifiedAdId() {
        return ClassifiedAdId;
    }

    public void setClassifiedAdId(String ClassifiedAdId) {
        this.ClassifiedAdId = ClassifiedAdId;
    }

    public String getExpiresAt() {
        return ExpiresAt;
    }

    public void setExpiresAt(String ExpiresAt) {
        this.ExpiresAt = ExpiresAt;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
}
