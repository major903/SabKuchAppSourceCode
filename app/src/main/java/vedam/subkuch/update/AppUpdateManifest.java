package vedam.subkuch.update;

import com.google.gson.annotations.SerializedName;

/** Manifest published alongside the APK for internal direct updates. */
public final class AppUpdateManifest {
    @SerializedName("versionCode")
    private long versionCode;
    @SerializedName("versionName")
    private String versionName;
    @SerializedName("apkUrl")
    private String apkUrl;
    @SerializedName("changelog")
    private String changelog;
    @SerializedName("mandatory")
    private boolean mandatory;

    public long getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName == null ? "" : versionName.trim();
    }

    public String getApkUrl() {
        return apkUrl == null ? "" : apkUrl.trim();
    }

    public String getChangelog() {
        return changelog == null || changelog.trim().isEmpty()
                ? "Performance and stability improvements."
                : changelog.trim();
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isValid() {
        return versionCode > 0
                && !getVersionName().isEmpty()
                && getApkUrl().startsWith("https://");
    }
}
