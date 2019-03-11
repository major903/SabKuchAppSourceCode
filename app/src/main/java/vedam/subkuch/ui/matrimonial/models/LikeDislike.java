package vedam.subkuch.ui.matrimonial.models;

public class LikeDislike {
    private String ProfileId;

    private int ReactionType;

    private String TargetProfileId;

    private String Id;

    public String getProfileId() {
        return ProfileId;
    }

    public void setProfileId(String ProfileId) {
        this.ProfileId = ProfileId;
    }

    public int getReactionType() {
        return ReactionType;
    }

    public void setReactionType(int ReactionType) {
        this.ReactionType = ReactionType;
    }

    public String getTargetProfileId() {
        return TargetProfileId;
    }

    public void setTargetProfileId(String TargetProfileId) {
        this.TargetProfileId = TargetProfileId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
}
