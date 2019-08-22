package vedam.subkuch.network.models.needs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Provider implements Parcelable {
    private String ProviderId;

    private String Providers;

    private int UserType;

    public Provider() {
    }

    protected Provider(Parcel in) {
        ProviderId = in.readString();
        Providers = in.readString();
        UserType = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ProviderId);
        dest.writeString(Providers);
        dest.writeInt(UserType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

    public String getProviderId() {
        return ProviderId;
    }

    public void setProviderId(String ProviderId) {
        this.ProviderId = ProviderId;
    }

    public String getProviders() {
        return Providers;
    }

    public void setProviders(String Providers) {
        this.Providers = Providers;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int UserType) {
        this.UserType = UserType;
    }

    @NonNull
    @Override
    public String toString() {
        return Providers;
    }
}
