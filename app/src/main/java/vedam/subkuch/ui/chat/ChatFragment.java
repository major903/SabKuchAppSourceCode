package vedam.subkuch.ui.chat;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseFragment;
import vedam.subkuch.databinding.FragmentChatBinding;
import vedam.subkuch.db.chat.Chat;
import vedam.subkuch.db.chat.ChatRepository;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnInsertUpdateDoneListener;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.LogUtils;


/**
 * A sends to B
 * 1. A sends Message to server with SocketType Msg. Server Sends to A with SocketType Read for app to save UUID
 * 2. Server sends message to B with SocketType Msg.
 * 3. B receives message from server with SocketType Msg. Then sends a message to server with SocketType Ack and status = 2
 * 4. Server sends a message to A with SocketType Ack and Status = 2.
 * 5. A sends a message to server SocketType Ack and Status = 3
 */
public class ChatFragment extends BaseFragment implements OnInsertUpdateDoneListener {

    private FragmentChatBinding fragmentChatBinding;
    public ChatAdapter chatAdapter;
    public static String TAG = "TAG_CHAT";
    private String senderName, chatToId;
    private WebSocket webSocket;
    private ChatRepository chatRepository;
    private boolean isConnected;

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
            chatToId = getArguments().getString(Constants.EXTRA_CHAT_TO_ID);
            senderName = getArguments().getString(Constants.EXTRA_NAME);
            setTitle(senderName);
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

        chatRepository.getIndividualChat(AppPrefs.getPrefsUserId(context), chatToId)
                .observe(this, chats -> chatAdapter.setChat(chats));
    }

    public void setIsConnected(boolean isConnected) {
        LogUtils.LOGD(TAG, String.valueOf(isConnected));
        this.isConnected = isConnected;
        if (isConnected || webSocket == null) {
            fragmentChatBinding.flInternetConnection.setVisibility(View.GONE);
            connectWebSocket();
        } else
            fragmentChatBinding.flInternetConnection.setVisibility(View.VISIBLE);

    }

    private void storeMessage(String message) {

        Chat chat = new Chat();
        chat.setSocketType(Constants.SOCKET_TYPE_MESSAGE);
        chat.setFromProfileId(AppPrefs.getPrefsUserId(context));
        chat.setToProfileId(chatToId);
        chat.setMessage(message);
        String time = String.valueOf(System.currentTimeMillis());
        chat.setTimeStamp(time);
        chat.setStatus(Constants.CHAT_STATUS_NOT_SENT);
        chat.setSenderName(senderName);
//        this.chat = chat;
        chatRepository.insert(chat, true);
    }


    @Override
    public void onInsertDone(Chat chat, boolean isOwnMessage) {
        if (chat != null && chat.getId() != -1 && isOwnMessage)
            sendMessage(chat);
    }

    @Override
    public void onUpdateDone(int updatedRowsCount, boolean isOwnMessage) {
        LogUtils.LOGD(TAG, " " + updatedRowsCount);
    }


    private void connectWebSocket() {

        okhttp3.Request request = new okhttp3.Request.Builder().url(NetworkConstants.WEB_SOCKET_END_POINT)
                .addHeader(NetworkConstants.Authorization, AppPrefs.getPrefsToken(context)).build();
        ChatWebSocketListener listener = new ChatWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    private void sendMessage(Chat chat) {

        String chatJson = new Gson().toJson(chat);
        if (webSocket != null) {
            boolean isSent = webSocket.send(chatJson);
            LogUtils.LOGD(TAG, chatJson + "\n Sent " + isSent);
            if (isSent) {
                chat.setStatus(Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED);
                chatRepository.update(chat, true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocket != null)
            webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
    }

    // WebSocket
    private final class ChatWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            LogUtils.LOGD(TAG, "WebSocket connected");
            sendPendingMessages();
            sendPendingAcknowledgement();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtils.LOGD(TAG, "Rx: " + text);
            handleIncomingMessage(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
            ChatFragment.this.webSocket = null;
            LogUtils.LOGD(TAG, "Closed: " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            ChatFragment.this.webSocket = null;
            if (BuildConfig.DEBUG)
                t.printStackTrace();
            LogUtils.LOGD(TAG, "Error: " + t.getMessage());
            connectWebSocket();
        }
    }

    private void sendPendingMessages() {

        handlePendingChats(chatRepository.getPendingChat());
    }

    private void handlePendingChats(List<Chat> pendingChats) {
        for (Chat chat : pendingChats)
            sendMessage(chat);
    }

    private void sendPendingAcknowledgement() {

        chatRepository.getPendingAckChat(AppPrefs.getPrefsUserId(context), chatToId)
                .observe(this, this::handlePendingAckChats);
    }

    private void handlePendingAckChats(List<Chat> pendingAckChats) {

        String myId = AppPrefs.getPrefsUserId(context);
        for (Chat chat : pendingAckChats) {
            if (myId != null && myId.equals(chat.getToProfileId()))
                handleChatToBeRead(chat);
            else
                handleChatToBeAcknowledged(chat);
        }
    }

    private void handleChatToBeRead(Chat chat) {
        if (webSocket != null) {
            chat.setStatus(Constants.CHAT_STATUS_READ);
            chat.setSocketType(Constants.SOCKET_TYPE_ACKNOWLEDGEMENT);
            String chatJson = new Gson().toJson(chat);
            boolean isSent = webSocket.send(chatJson);
            LogUtils.LOGD(TAG, chatJson + "Ack read " + isSent);
            if (isSent)
                chatRepository.update(chat, false);
        } else
            connectWebSocket();
    }

    private void handleChatToBeAcknowledged(Chat chat) {
        if (webSocket != null) {
            chat.setStatus(Constants.CHAT_STATUS_ACKNOWLEDGED);
            chat.setSocketType(Constants.SOCKET_TYPE_ACKNOWLEDGEMENT);
            String chatJson = new Gson().toJson(chat);
            boolean isSent = webSocket.send(chatJson);
            LogUtils.LOGD(TAG, chatJson + "Ack Acknow " + isSent);
            if (isSent)
                chatRepository.update(chat, false);
        } else
            connectWebSocket();
    }

    private void handleIncomingMessage(String text) {

        Chat chat = new Gson().fromJson(text, Chat.class);
        if (chat != null)
            /*Check if a chat message is present with same uuid. If yes, then update the status
             * else just update the timestamp to current time and insert it. */
            if (Constants.SOCKET_TYPE_MESSAGE.equals(chat.getSocketType())) {
                String uuid = chat.getUuid();
                if (!TextUtils.isEmpty(uuid)) {
                    Chat existingChat = chatRepository.getChatByUUID(uuid);
                    if (existingChat != null) {
                        existingChat.setStatus(chat.getStatus());
                        chatRepository.update(existingChat, false);
                    } else {
                        chat.setTimeStamp(String.valueOf(System.currentTimeMillis()));
                        chatRepository.insert(chat, false);
                    }
                }
            } else if (Constants.SOCKET_TYPE_ACKNOWLEDGEMENT.equals(chat.getSocketType())) {
                String uniqueId = chat.getUuid();
                Chat existingChat = chatRepository.getChatByUUID(uniqueId);
                if (existingChat != null) {
                    existingChat.setStatus(chat.getStatus());
                    chatRepository.update(existingChat, false);
                }
            } else if (Constants.SOCKET_TYPE_READ.equals(chat.getSocketType())) {
                long id = chat.getId();
                Chat existingChat = chatRepository.getChatById(id);
                if (existingChat != null) {
                    existingChat.setUuid(chat.getUuid());
                    chatRepository.update(existingChat, false);
                }
            }
    }
}
