package vedam.subkuch.helpers;

import android.Manifest;

/**
 * Created by nansari on 10/4/17.
 */

public class Constants {

    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_DIRECTORY_DETAIL = "EXTRA_DIRECTORY_DETAIL";
    public static final String EXTRA_COUNTRY_CODE = "EXTRA_COUNTRY_CODE";
    public static final String EXTRA_CODE = "EXTRA_CODE";
    public static final String EXTRA_PIN = "EXTRA_PIN";
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CITY_NAME = "EXTRA_CITY_NAME";
    public static final String EXTRA_BUSINESS_ID = "EXTRA_BUSINESS_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_SUB_CATEGORY_NAME = "EXTRA_SUB_CATEGORY_NAME";
    public static final String EXTRA_SUB_CATEGORY_ID = "EXTRA_SUB_CATEGORY_ID";
    public static final String EXTRA_IMAGE_ITEMS = "EXTRA_IMAGE_ITEMS";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_IS_IMAGE_URLS = "EXTRA_IS_IMAGE_URLS";
    public static final String EXTRA_CHAT_TO_ID = "EXTRA_CHAT_TO_ID";
    public static final int PERMISSIONS_REQUEST_STORAGE = 2;
    public static final int PERMISSIONS_REQUEST_CAMERA = 3;

    public static final String[] CAMERA_GALLERY_GROUP_PERMISSION = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] READ_WRITE_EXTERNAL_GROUP_PERMISSION = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //onActivityResult Request constants
    public static final int REQUEST_CHECK_SETTINGS = 2;
    public static final int REQUEST_PICK_IMAGE_FROM_GALLERY = 3;
    public static final int REQUEST_PICK_IMAGE_FROM_CAMERA = 4;
    public static final int REQUEST_ADD_QUESTION = 5;
    public static final int REQUEST_PLACE_PICKER = 6;

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String EXTRA_LOCATION_LATITUDE = "EXTRA_LOCATION_LATITUDE";
    public static final String EXTRA_LOCATION_LONGITUDE = "EXTRA_LOCATION_LONGITUDE";
    public static final String EXTRA_SERVICE_ADDRESS = "EXTRA_SERVICE_ADDRESS";

    //Permission constants
    public static final int PERMISSION_REQUEST_READ_LOCATION = 1;
    public static final String FRESHER = "1";
    public static final int COUNTRY_ID = 1;
    public

    static final int NORMAL_CLOSURE_STATUS = 1000;
    public static String COUNTRY_CODE = "91";

    public static final int PERMISSIONS_REQUEST_SMS = 4;

    public static final String STATUS_SUCCESS = "1";
    public static final String SUCCESS = "success";
    public static final String TRUE = "true";

    public static final String FirstName = "FirstName";
    public static final String LastName = "LastName";
    public static final String Mobile = "Mobile";
    public static final String EMail = "EMail";
    public static final String Usertypeid = "Usertypeid";
    public static final String userid = "userid";
    public static final String Latitude = "Latitude";
    public static final String Longitude = "Longitude";
    public static final String DOB = "DOB";
    public static final String Gender = "Gender";
    public static final String cityid = "cityid";
    public static final String countryid = "countryid";
    public static final String categoryid = "categoryid";
    public static final String topic = "topic";
    public static final String Blogid = "Blogid";
    public static final String Replaymessage = "Replaymessage";

    public static final String Directory = "Directory";
    public static final String Jobs = "Jobs";
    public static final String Movies = "Movies Timings";
    public static final String Phone_book = "Phonebook";
    public static final String Dating = "Matrimonial";
    public static final String Ask_Me = "Ask Me";
    public static final String Transport = "Public Transport Timings";
    public static final String Offers = "Offers";
    public static final String Gift_A_Life = "Gift A Life";
    public static final String Events = "Events";

    public static final String Title = "Title";
    public static final String Date = "Date";
    public static final String Time = "Time";
    public static final String About = "About";
    public static final String EntryFee = "EntryFee";
    public static final String Venue = "Venue";
    public static final String image = "image";
    public static final String quation = "quation";
    public static final String UserTypeId = "UserTypeId";

    public static final String BusinessID = "BusinessID";
    public static final String Rating = "Rating";
    public static final String BusinessReview = "BusinessReview";

    public static final String jobcategory = "jobcategory";
    public static final String title = "title";
    public static final String discription = "discription";

    public static final String PRIVACY_POLICY_URL = "https://sabkuchworld.com/privacypolicy.html";

    public static final int ENTER_PIN_CODE = 0;
    public static final int SET_PIN_CODE = 1;
    public static final int RE_ENTER_PIN_CODE = 2;

    public static final int REACTION_TYPE_LIKE = 1;
    public static final int REACTION_TYPE_DISLIKE = 2;

    //Deepak Constants
    public final static String APP_NAME = "SabKuch";
    //public final static String BASE_URL = "http://sabkuch.visitmydemo.xyz/AllAPI/";
    public final static int GALLERY_INTENT = 1;
    public final static int CAMERA_INTENT = 2;
    public final static int REQUEST_PERMISSION = 3;
    public final static int REQUEST_PERMISSION_SETTING = 4;
    public final static int CROP_IMAGE_INTENT = 5;


    //SOCKET_TYPE
    public final static String SOCKET_TYPE_MESSAGE = "Msg";
    public final static String SOCKET_TYPE_ACKNOWLEDGEMENT = "Ack";

    //CHAT status
    public final static int CHAT_STATUS_NOT_SENT = -1;
    public final static int CHAT_STATUS_NOT_DELIVERED = 0;
    public final static int CHAT_STATUS_DELIVERED = 1;
    public final static int CHAT_STATUS_READ = 2;
    public final static int CHAT_STATUS_ACKNOWLEDGED = 3;
}
