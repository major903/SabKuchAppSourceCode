package vedam.subkuch.ui.ask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.events.AddEventResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AskActivity extends BaseActivity {

    private EditText etQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        setToolbarBackButton();
        setTitle(R.string.ask);

        etQuestion = findViewById(R.id.et_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                int errorMessage = validateErrorMessage();
                if (errorMessage == 0) {
                    submit();
                } else
                    UiUtil.showDialog(this, getString(errorMessage), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void submit() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        Map<String, String> request = new HashMap<>();
        String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        request.put(Constants.quation, etQuestion.getText().toString());
        request.put(Constants.userid, userId);

        DataFetcher.addQuestion(this, new Gson().toJson(request), onAddQuestionSuccessListener, AddEventResponse.class, onErrorListener);
    }

    private Response.Listener<AddEventResponse> onAddQuestionSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            UiUtil.showToast(this, getString(R.string.question_posted));
        } else
            UiUtil.showToast(this, this.getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(etQuestion.getText().toString().trim()))
            errorMessage = R.string.enter_question;

        return errorMessage;
    }
}
