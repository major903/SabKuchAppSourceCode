package vedam.subkuch.ui.ask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityAddQuestionBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;
import vedam.subkuch.ui.ask.models.AskCategory;
import vedam.subkuch.ui.ask.models.AskCategoryResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AddQuestionActivity extends BaseActivity {

    private EditText etQuestion;
    private ActivityAddQuestionBinding activityAddQuestionBinding;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_question);

        setToolbarBackButton();
        setTitle(R.string.add_question);

        getCategories();
        etQuestion = findViewById(R.id.et_question);

        activityAddQuestionBinding.btSubmit.setOnClickListener(view -> {
            int errorMessage = validateErrorMessage();
            if (errorMessage == 0) {
                submit();
            } else
                UiUtil.showDialog(this, getString(errorMessage), true);
        });
    }

    private void getCategories() {
        UiUtil.showProgressDialog(this, R.string.please_wait);
        DataFetcher.getAskCategories(this, onAskCategorySuccessListener, AskCategoryResponse.class, onErrorListener);

    }

    private Response.Listener<AskCategoryResponse> onAskCategorySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equalsIgnoreCase(Constants.SUCCESS)) {
            setAskCategories(response.getReturnData());
        } else
            UiUtil.showToast(this, getString(R.string.no_data));
    };

    private void setAskCategories(ArrayList<AskCategory> askCategories) {

        AskCategory askCategory = new AskCategory();
        askCategory.setCategoryname(getString(R.string.select_a_category));
        askCategories.add(0, askCategory);

        ArrayAdapter<AskCategory> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, askCategories);
        activityAddQuestionBinding.spCategory.setAdapter(adapter);
        activityAddQuestionBinding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = ((AskCategory) parent.getItemAtPosition(position)).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityAddQuestionBinding.spCategory.setSelection(0);
    }

    private void submit() {

        UiUtil.showProgressDialog(this, getString(R.string.please_wait));
        Map<String, String> request = new HashMap<>();
        String userId = AppPrefs.getInstance(this).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        request.put(Constants.topic, etQuestion.getText().toString());
        request.put(Constants.userid, userId);
        request.put(Constants.categoryid, categoryId);

        DataFetcher.addQuestion(this, new Gson().toJson(request), onAddQuestionSuccessListener, AddEventResponse.class, onErrorListener);
    }

    private Response.Listener<AddEventResponse> onAddQuestionSuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && !TextUtils.isEmpty(response.getReturnMessage())) {
            UiUtil.showToast(this, response.getReturnMessage());
            setResult(RESULT_OK);
            finish();
        } else
            UiUtil.showToast(this, this.getString(R.string.err_occurred));
    };

    private int validateErrorMessage() {
        int errorMessage = 0;
        if (TextUtils.isEmpty(categoryId))
            errorMessage = R.string.select_a_category;
        else if (TextUtils.isEmpty(etQuestion.getText().toString().trim()))
            errorMessage = R.string.enter_question;

        return errorMessage;
    }
}
