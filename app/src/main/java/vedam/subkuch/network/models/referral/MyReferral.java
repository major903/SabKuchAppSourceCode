package vedam.subkuch.network.models.referral;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;

@JsonAdapter(MyReferral.Adapter.class)
public class MyReferral {
    private String FirstName;

    private String RefferalCode;

    private String ProfileImage;

    private String LastName;

    private String Mobile;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getRefferalCode() {
        return RefferalCode;
    }

    public void setRefferalCode(String RefferalCode) {
        this.RefferalCode = RefferalCode;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String ProfileImage) {
        this.ProfileImage = ProfileImage;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    /**
     * The referral endpoint has historically varied the casing of its JSON keys.
     * Read those keys case-insensitively while retaining the existing model API.
     */
    public static final class Adapter extends TypeAdapter<MyReferral> {
        @Override
        public MyReferral read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }

            MyReferral referral = new MyReferral();
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName().toLowerCase(Locale.ROOT);
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    continue;
                }

                switch (name) {
                    case "firstname":
                        referral.setFirstName(reader.nextString());
                        break;
                    case "lastname":
                        referral.setLastName(reader.nextString());
                        break;
                    case "mobile":
                        referral.setMobile(reader.nextString());
                        break;
                    case "refferalcode":
                    case "referralcode":
                        referral.setRefferalCode(reader.nextString());
                        break;
                    case "profileimage":
                        referral.setProfileImage(reader.nextString());
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return referral;
        }

        @Override
        public void write(JsonWriter writer, MyReferral referral) throws IOException {
            if (referral == null) {
                writer.nullValue();
                return;
            }

            writer.beginObject();
            writer.name("firstName").value(referral.getFirstName());
            writer.name("lastName").value(referral.getLastName());
            writer.name("Mobile").value(referral.getMobile());
            writer.name("RefferalCode").value(referral.getRefferalCode());
            writer.name("ProfileImage").value(referral.getProfileImage());
            writer.endObject();
        }
    }
}
