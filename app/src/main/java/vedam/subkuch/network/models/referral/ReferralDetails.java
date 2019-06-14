package vedam.subkuch.network.models.referral;

import java.util.ArrayList;

public class ReferralDetails {

    private ArrayList<MyReferral> MyReferral;
    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public ArrayList<MyReferral> getReferralDetails() {
        return MyReferral;
    }

    public void setReferralDetails(ArrayList<MyReferral> myReferral) {
        this.MyReferral = myReferral;
    }
}
