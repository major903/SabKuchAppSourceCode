package vedam.subkuch.ui.directory;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import vedam.subkuch.network.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityAddReviewBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AddReviewActivity extends BaseActivity {

    private ActivityAddReviewBinding activityAddReviewBinding;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddReviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_review);
        setToolbarBackButton();
        setTitle(R.string.add_review);
        businessId = getIntent().getStringExtra(Constants.EXTRA_BUSINESS_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_done) {
                int errorMessage = validateErrorMessage();
                if (errorMessage == 0) {
                    submit();
                } else
                    UiUtil.showDialog(this, getString(errorMessage), true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        Map<String, String> request = new HashMap<>();
        String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        request.put(Constants.userid, userId);
        request.put(Constants.BusinessID, businessId);
        request.put(Constants.Rating, String.valueOf(activityAddReviewBinding.rbRating.getRating()));
        request.put(Constants.BusinessReview, activityAddReviewBinding.etReview.getText().toString());

        DataFetcher.addReview(this, new Gson().toJson(request), onAddReviewSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddReviewSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.isStatus()) {
            UiUtil.showToast(this, getString(R.string.review_added));
            finish();
        } else
            UiUtil.showToast(this, this.getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (activityAddReviewBinding.rbRating.getRating() == 0)
            errorMessage = R.string.select_rating;
        else if (TextUtils.isEmpty(activityAddReviewBinding.etReview.getText()))
            errorMessage = R.string.enter_review;


        return errorMessage;
    }
}
