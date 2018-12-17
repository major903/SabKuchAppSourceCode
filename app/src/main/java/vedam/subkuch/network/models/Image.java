package vedam.subkuch.network.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nansari on 5/25/2017.
 */

public class Image implements Parcelable {

    private String id;
    private String url;

    public Image(String url) {
        this.url = url;
    }

    protected Image(Parcel in) {
        id = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ClassImage [id = " + id + ", url = " + url + "]";
    }
}
