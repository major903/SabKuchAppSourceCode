package vedam.subkuch.network.models;

import com.google.gson.annotations.SerializedName;

/** Selects the profile areas that the authenticated user wants to permanently delete. */
public final class DeleteProfileRequest {

    @SerializedName("isDating")
    private final boolean isDating;

    @SerializedName("isMatrimony")
    private final boolean isMatrimony;

    @SerializedName("isUser")
    private final boolean isUser;

    public DeleteProfileRequest(boolean isDating, boolean isMatrimony, boolean isUser) {
        this.isDating = isDating;
        this.isMatrimony = isMatrimony;
        this.isUser = isUser;
    }

    public boolean isDating() {
        return isDating;
    }

    public boolean isMatrimony() {
        return isMatrimony;
    }

    public boolean isUser() {
        return isUser;
    }
}
