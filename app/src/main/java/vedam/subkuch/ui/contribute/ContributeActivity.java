package vedam.subkuch.ui.contribute;

import android.content.Intent;
import android.os.Bundle;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.ui.stafftrack.StaffTrackActivity;

/** Full-screen contribution picker: company data, NRI data or a real estate lead. */
public class ContributeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        setTitle(R.string.contribute);
        setToolbarBackButton();
        findViewById(R.id.option_company_data).setOnClickListener(v ->
                startActivity(new Intent(this, StaffTrackActivity.class)));
        findViewById(R.id.option_nri_data).setOnClickListener(v ->
                startActivity(new Intent(this, NriDataEntryActivity.class)));
        findViewById(R.id.option_real_estate_lead).setOnClickListener(v ->
                startActivity(new Intent(this, RealEstateLeadActivity.class)));
    }
}
