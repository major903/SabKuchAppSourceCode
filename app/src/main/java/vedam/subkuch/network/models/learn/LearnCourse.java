package vedam.subkuch.network.models.learn;

public class LearnCourse {
    private int CourseId;
    private String EncryptedCourseId;
    private int CourseCategoryId;
    private String CategoryName;
    private String Name;
    private String Description;
    private String TrainerName;
    private double Rating;
    private int ReviewCount;
    private double Price;
    private double Mrp;
    private String ImageUrl;
    private String TrainerPic;
    private String PurchaseUrl;
    private boolean IsSubscribed;
    private int SubscriberCount;

    public int getCourseId() { return CourseId; }
    public String getEncryptedCourseId() { return EncryptedCourseId; }
    public int getCourseCategoryId() { return CourseCategoryId; }
    public String getCategoryName() { return CategoryName; }
    public String getName() { return Name; }
    public String getDescription() { return Description; }
    public String getTrainerName() { return TrainerName; }
    public double getRating() { return Rating; }
    public int getReviewCount() { return ReviewCount; }
    public double getPrice() { return Price; }
    public double getMrp() { return Mrp; }
    public String getImageUrl() { return ImageUrl; }
    public String getTrainerPic() { return TrainerPic; }
    public String getPurchaseUrl() { return PurchaseUrl; }
    public boolean isSubscribed() { return IsSubscribed; }
    public int getSubscriberCount() { return SubscriberCount; }
}
