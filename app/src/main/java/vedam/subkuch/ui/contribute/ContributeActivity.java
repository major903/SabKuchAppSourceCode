package vedam.subkuch.ui.contribute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.network.RegistrationApiClient;
import vedam.subkuch.network.models.ContribItem;
import vedam.subkuch.network.models.ContribResponse;
import vedam.subkuch.ui.stafftrack.StaffTrackActivity;

/** Full-screen contribution picker: company data, NRI data or a real estate lead. */
public class ContributeActivity extends BaseActivity {

    public static final String EXTRA_CONTRIB_DETAIL = "extra_contrib_detail";
    public static final String CONTRIB_PREFS = "contrib_meta_cache";
    public static final int CONTRIB_ID_COMPANY = 2;
    public static final int CONTRIB_ID_REAL_ESTATE = 3;
    public static final int CONTRIB_ID_NRI = 4;

    private TextView tvCompanyTitle;
    private TextView tvNriTitle;
    private TextView tvRealEstateTitle;

    private String companyDetail;
    private String nriDetail;
    private String realEstateDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        setTitle(R.string.contribute);
        setToolbarBackButton();

        tvCompanyTitle = findViewById(R.id.tv_company_title);
        tvNriTitle = findViewById(R.id.tv_nri_title);
        tvRealEstateTitle = findViewById(R.id.tv_real_estate_title);

        findViewById(R.id.option_company_data).setOnClickListener(v -> {
            Intent intent = new Intent(this, StaffTrackActivity.class);
            if (!TextUtils.isEmpty(companyDetail)) {
                intent.putExtra(EXTRA_CONTRIB_DETAIL, companyDetail);
            }
            startActivity(intent);
        });

        findViewById(R.id.option_nri_data).setOnClickListener(v -> {
            Intent intent = new Intent(this, NriDataEntryActivity.class);
            if (!TextUtils.isEmpty(nriDetail)) {
                intent.putExtra(EXTRA_CONTRIB_DETAIL, nriDetail);
            }
            startActivity(intent);
        });

        findViewById(R.id.option_real_estate_lead).setOnClickListener(v -> {
            Intent intent = new Intent(this, RealEstateLeadActivity.class);
            if (!TextUtils.isEmpty(realEstateDetail)) {
                intent.putExtra(EXTRA_CONTRIB_DETAIL, realEstateDetail);
            }
            startActivity(intent);
        });

        loadCachedContribData();
        fetchContribData();
    }

    private void loadCachedContribData() {
        SharedPreferences prefs = getSharedPreferences(CONTRIB_PREFS, MODE_PRIVATE);

        String companyTitle = prefs.getString("key_title_" + CONTRIB_ID_COMPANY, null);
        String companyDesc = prefs.getString("key_detail_" + CONTRIB_ID_COMPANY, null);
        if (!TextUtils.isEmpty(companyTitle) && tvCompanyTitle != null) {
            tvCompanyTitle.setText(companyTitle);
        }
        if (!TextUtils.isEmpty(companyDesc)) {
            companyDetail = companyDesc;
        }

        String nriTitle = prefs.getString("key_title_" + CONTRIB_ID_NRI, null);
        String nriDesc = prefs.getString("key_detail_" + CONTRIB_ID_NRI, null);
        if (!TextUtils.isEmpty(nriTitle) && tvNriTitle != null) {
            tvNriTitle.setText(nriTitle);
        }
        if (!TextUtils.isEmpty(nriDesc)) {
            nriDetail = nriDesc;
        }

        String estateTitle = prefs.getString("key_title_" + CONTRIB_ID_REAL_ESTATE, null);
        String estateDesc = prefs.getString("key_detail_" + CONTRIB_ID_REAL_ESTATE, null);
        if (!TextUtils.isEmpty(estateTitle) && tvRealEstateTitle != null) {
            tvRealEstateTitle.setText(estateTitle);
        }
        if (!TextUtils.isEmpty(estateDesc)) {
            realEstateDetail = estateDesc;
        }
    }

    private void fetchContribData() {
        if (!RegistrationApiClient.isConfigured()) return;

        fetchContrib(CONTRIB_ID_COMPANY);
        fetchContrib(CONTRIB_ID_REAL_ESTATE);
        fetchContrib(CONTRIB_ID_NRI);
    }

    private void fetchContrib(int contribId) {
        RegistrationApiClient.getApi(this).getContrib(contribId).enqueue(new Callback<ContribResponse>() {
            @Override
            public void onResponse(Call<ContribResponse> call, Response<ContribResponse> response) {
                if (isFinishing() || isDestroyed()) return;
                if (response.isSuccessful() && response.body() != null && response.body().getReturnData() != null) {
                    applyContrib(response.body().getReturnData());
                }
            }

            @Override
            public void onFailure(Call<ContribResponse> call, Throwable throwable) {
                // Ignore network failure and keep current displayed text
            }
        });
    }

    private void applyContrib(ContribItem item) {
        if (item == null) return;
        int contribId = item.getContribId();
        String type = item.getType();
        String detail = item.getDetail();

        SharedPreferences.Editor editor = getSharedPreferences(CONTRIB_PREFS, MODE_PRIVATE).edit();
        if (!TextUtils.isEmpty(type)) {
            editor.putString("key_title_" + contribId, type);
        }
        if (!TextUtils.isEmpty(detail)) {
            editor.putString("key_detail_" + contribId, detail);
        }
        editor.apply();

        if (contribId == CONTRIB_ID_COMPANY) {
            if (!TextUtils.isEmpty(type) && tvCompanyTitle != null) {
                tvCompanyTitle.setText(type);
            }
            if (!TextUtils.isEmpty(detail)) {
                companyDetail = detail;
            }
        } else if (contribId == CONTRIB_ID_NRI) {
            if (!TextUtils.isEmpty(type) && tvNriTitle != null) {
                tvNriTitle.setText(type);
            }
            if (!TextUtils.isEmpty(detail)) {
                nriDetail = detail;
            }
        } else if (contribId == CONTRIB_ID_REAL_ESTATE) {
            if (!TextUtils.isEmpty(type) && tvRealEstateTitle != null) {
                tvRealEstateTitle.setText(type);
            }
            if (!TextUtils.isEmpty(detail)) {
                realEstateDetail = detail;
            }
        }
    }
}
