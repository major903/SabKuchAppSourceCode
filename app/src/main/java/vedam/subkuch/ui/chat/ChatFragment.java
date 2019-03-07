package vedam.subkuch.ui.chat;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentChatBinding;
import vedam.subkuch.db.chat.Chat;
import vedam.subkuch.db.chat.ChatRepository;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnInsertUpdateDoneListener;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.LogUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements OnInsertUpdateDoneListener {

    private FragmentChatBinding fragmentChatBinding;
    public ChatAdapter chatAdapter;
    public static String TAG = "TAG_CHAT";
    private String toName, chatToId;
    private WebSocket webSocket;
    private ChatRepository chatRepository;
    private boolean isConnected;
    private Chat chat;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(Bundle bundle) {

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            toName = getArguments().getString(Constants.EXTRA_NAME);
            chatToId = getArguments().getString(Constants.EXTRA_CHAT_TO_ID);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Let's inflate & return the view
        fragmentChatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        return fragmentChatBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        bindCallbacks();
        bindData();
    }

    private void init() {

        chatAdapter = new ChatAdapter(context);
        fragmentChatBinding.rvChat.setAdapter(chatAdapter);
        fragmentChatBinding.rvChat.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        chatRepository = new ChatRepository(context, this);
        connectSocket();
    }

    private void bindCallbacks() {

        fragmentChatBinding.sendMessageButton.setOnClickListener(v -> {
            if (fragmentChatBinding.etMessage.getText().toString().trim().equals(""))
                Toast.makeText(context, "Empty message!",
                        Toast.LENGTH_SHORT).show();
            else {
                if (!isConnected || webSocket == null)
                    Toast.makeText(context, "No Internet connection.",
                            Toast.LENGTH_SHORT).show();
                else {
                    String message = fragmentChatBinding.etMessage.getText().toString().trim();
                    storeMessage(message);
                    fragmentChatBinding.etMessage.setText("");
                }
            }

        });
    }

    private void bindData() {

        chatRepository.getIndividualChat(AppPrefs.getPrefsUserId(context), chatToId).observe(this, chats -> {
            chatAdapter.setChat(chats);
        });
    }

    public void setIsConnected(boolean isConnected) {
        LogUtils.LOGI(TAG, String.valueOf(isConnected));
        this.isConnected = isConnected;
    }

    private void storeMessage(String message) {

        Chat chat = new Chat();
        chat.setSocketType(Constants.SOCKET_TYPE_MESSAGE);
        chat.setFromProfileId(AppPrefs.getPrefsUserId(context));
        chat.setToProfileId(chatToId);
        chat.setMessage(message);
        chat.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        chat.setStatus(Constants.CHAT_STATUS_NOT_SENT);
        this.chat = chat;
        chatRepository.insert(chat, true);
    }

    @Override
    public void onInsertUpdateDone(long rowId, boolean isOwnMessage) {
        if (rowId != -1 && chat != null)
            sendMessage((int) rowId);
    }

    private void connectSocket() {

        okhttp3.Request request = new okhttp3.Request.Builder().url("ws://sabkuch2.sabkuchworld.com/api/SabkuchChat/Get")
                .addHeader("Authorization", AppPrefs.getPrefsToken(context)).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    private void sendMessage(final int id) {

        String chatJson = new Gson().toJson(chat);
        boolean isSent = webSocket.send(chatJson);
        if (isSent) {
            chat.setId(id);
            chat.setStatus(Constants.CHAT_STATUS_NOT_DELIVERED);
            chatRepository.update(chat, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
    }

    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            LogUtils.LOGI(TAG, "WebSocket connected");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtils.LOGI(TAG, "Rx: " + text);
            handleMessage(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
            LogUtils.LOGI(TAG, "Closed: " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            t.printStackTrace();
            LogUtils.LOGI(TAG, "Error: " + t.getMessage());
        }
    }

    private void handleMessage(String text) {

        Chat chat = new Gson().fromJson(text, Chat.class);
        if (chat != null && chat.getSocketType().equals(Constants.SOCKET_TYPE_MESSAGE)) {
            chatRepository.insert(chat, false);
        } else if (chat != null && chat.getSocketType().equals(Constants.SOCKET_TYPE_ACKNOWLEDGEMENT)) {
        }
    }
}
