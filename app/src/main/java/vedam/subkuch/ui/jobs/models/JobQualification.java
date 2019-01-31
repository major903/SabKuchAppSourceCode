package vedam.subkuch.ui.jobs.models;

import android.support.annotation.NonNull;

public class JobQualification {

    private String QulaificationId;

    private String QualificationName;

    public String getQulaificationId() {
        return QulaificationId;
    }

    public void setQulaificationId(String QulaificationId) {
        this.QulaificationId = QulaificationId;
    }

    public String getQualificationName() {
        return QualificationName;
    }

    public void setQualificationName(String QualificationName) {
        this.QualificationName = QualificationName;
    }

    @NonNull
    @Override
    public String toString() {
        return QualificationName;
    }
}
