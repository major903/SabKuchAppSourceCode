package vedam.subkuch.network.models.UserDetail;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageObject implements Parcelable {
    private String Userid;

    private String Imagedataid;

    private String Image;

    protected ImageObject(Parcel in) {
        Userid = in.readString();
        Imagedataid = in.readString();
        Image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Userid);
        dest.writeString(Imagedataid);
        dest.writeString(Image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageObject> CREATOR = new Creator<ImageObject>() {
        @Override
        public ImageObject createFromParcel(Parcel in) {
            return new ImageObject(in);
        }

        @Override
        public ImageObject[] newArray(int size) {
            return new ImageObject[size];
        }
    };

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getImagedataid() {
        return Imagedataid;
    }

    public void setImagedataid(String Imagedataid) {
        this.Imagedataid = Imagedataid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

}
