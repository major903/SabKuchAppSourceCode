package vedam.subkuch.ui.stafftrack;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.android.volley.Response;
import com.google.gson.Gson;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityStaffTrackBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.MessageRequest;
import vedam.subkuch.ui.jobs.models.AddResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

import static vedam.subkuch.utils.AppUtil.deNull;

public class StaffTrackActivity extends BaseActivity {

    private ActivityStaffTrackBinding activityStaffTrackBinding;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStaffTrackBinding = DataBindingUtil.setContentView(this, R.layout.activity_staff_track);
        setTitle(R.string.location);
        setToolbarBackButton();
        requestLocation(true);
        initUI();
        bindCallbacks();
    }

    private void initUI() {

        String userName = deNull(AppPrefs.getPrefsUserName(this));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(String.format("Hello %s\nWhere are you now ?",
                userName));
        stringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary)),
                6, userName.length() + 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        activityStaffTrackBinding.tvName.setText(stringBuilder);

    }

    private void bindCallbacks() {
        activityStaffTrackBinding.btSubmit.setOnClickListener(v -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                submit();
            } else
                UiUtil.showDialog(this, getString(errorMessage), true);
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }

    private void submit() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setLatitude(latitude);
        messageRequest.setLongitude(longitude);
        messageRequest.setPostedMessage(activityStaffTrackBinding.etMessage.getText().toString().trim());
        messageRequest.setStaffId(AppPrefs.getPrefsUserId(this));

        DataFetcher.addStaffTrackLocation(this, new Gson().toJson(messageRequest), onAddMessageSuccessListener, AddResponse.class, onErrorListener);
    }

    private Response.Listener<AddResponse> onAddMessageSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            activityStaffTrackBinding.etMessage.setText("");
            UiUtil.showToast(StaffTrackActivity.this, getString(R.string.message_posted));
            StaffTrackActivity.this.finish();
        } else {
            UiUtil.showToast(StaffTrackActivity.this, getString(R.string.err_occurred));
        }


    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(activityStaffTrackBinding.etMessage.getText().toString().trim()))
            errorMessage = R.string.enter_message;
        else if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(latitude))
            errorMessage = R.string.submit_after_location;

        return errorMessage;
    }
}
