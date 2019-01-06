package vedam.subkuch.ui.ask;

import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseDialog;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.ui.events.AddEventResponse;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.UiUtil;

public class AskReplyDialog extends BaseDialog {

    private AskReplyPostedListener listener;
    private String questionId;
    private EditText etReply;
    private Context context;

    public AskReplyDialog(Context context, AskReplyPostedListener listener, String questionId) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.questionId = questionId;
        initUI();
    }

    public AskReplyDialog(Context context, String questionId) {
        super(context);
        this.context = context;
        this.questionId = questionId;
        initUI();
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ask_reply);

        etReply = findViewById(R.id.et_reply);
        Button btSubmit = findViewById(R.id.bt_submit);


        btSubmit.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etReply.getText()) || TextUtils.isEmpty(etReply.getText().toString().trim()))
                UiUtil.showToast(context, context.getString(R.string.enter_a_reply));
            else
                submit();

        });
    }

    private void submit() {

        UiUtil.showProgressDialog(context, context.getString(R.string.please_wait));
        Map<String, String> request = new HashMap<>();
        String userId = AppPrefs.getInstance(context).getSharedPreferences().getString(AppPrefs.PREFS_USER_ID, "");
        request.put(Constants.Blogid, questionId);
        request.put(Constants.userid, userId);
        request.put(Constants.Replaymessage, etReply.getText().toString());

        DataFetcher.addAskReply(context, new Gson().toJson(request), onAddReplySuccessListener, AddEventResponse.class, onErrorListener);
    }

    private Response.Listener<AddEventResponse> onAddReplySuccessListener = response -> {

        UiUtil.cancelProgressDialog();
        if (response != null && response.getReturnMessage().equals(Constants.SUCCESS)) {
            UiUtil.showToast(context, context.getString(R.string.reply_posted));
            if (listener != null)
                listener.onReplyPosted();
            dismiss();
        } else
            UiUtil.showToast(context, context.getString(R.string.err_occurred));
    };

    public interface AskReplyPostedListener {
        void onReplyPosted();
    }
}
