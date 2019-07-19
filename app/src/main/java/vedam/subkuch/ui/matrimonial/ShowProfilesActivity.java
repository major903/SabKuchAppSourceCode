package vedam.subkuch.ui.matrimonial;


import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import vedam.subkuch.BuildConfig;
import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityShowProfilesBinding;
import vedam.subkuch.db.chat.Chat;
import vedam.subkuch.db.chat.ChatRepository;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.interfaces.OnInsertUpdateDoneListener;
import vedam.subkuch.network.NetworkConstants;
import vedam.subkuch.ui.chat.ChatListFragment;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.matrimonial.editProfile.EditProfileFragment;
import vedam.subkuch.ui.matrimonial.preference.PreferenceFragment;
import vedam.subkuch.utils.AppPrefs;
import vedam.subkuch.utils.AppUtil;
import vedam.subkuch.utils.LogUtils;

import static vedam.subkuch.helpers.Constants.TAG_CHATS_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_MATCHES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PREFERENCES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PROFILE_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_SHOW_PROFILES_FRAGMENT;

public class ShowProfilesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener, OnInsertUpdateDoneListener {

    private ActivityShowProfilesBinding activityShowProfilesBinding;
    private HashMap<String, Integer> hmNavigationIds;
    private boolean isDating;
    private Menu menu;
    private Disposable internetDisposable, unreadMessagesDisposable;
    private WebSocket webSocket;
    private ChatRepository chatRepository;
    private TextView tvNotificationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityShowProfilesBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_profiles);
        setTitle(R.string.profiles);
        setToolbarBackButton();
        initUI();
        bindData();
        bindCallbacks();
        setHashMap();
    }

    private void initUI() {

        isDating = getIntent().getBooleanExtra(Constants.EXTRA_IS_DATING, false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityShowProfilesBinding.drawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityShowProfilesBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        addFragment(R.id.content_frame, ShowProfilesFragment.newInstance(isDating), TAG_SHOW_PROFILES_FRAGMENT, true, 0, 0, 0, 0);
    }


    private void bindCallbacks() {

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        activityShowProfilesBinding.navView.setNavigationItemSelectedListener(this);
    }

    private void bindData() {

        chatRepository = new ChatRepository(this, this);
        /*chatRepository.getTotalUnreadMessagesCount()
                .observe(this, this::setCount);*/
        TextView tvName = activityShowProfilesBinding.navView.getHeaderView(0).findViewById(R.id.tv_name);
        tvName.setText(AppPrefs.getPrefsUserName(this));
    }

    private void setCount(Integer count) {

        if (count == 0)
            tvNotificationCount.setVisibility(View.GONE);
        else if (count < 100) {
            tvNotificationCount.setVisibility(View.VISIBLE);
            tvNotificationCount.setText(String.valueOf(count));
        } else {
            tvNotificationCount.setVisibility(View.VISIBLE);
            tvNotificationCount.setText(getString(R.string.max_notification_number));
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CHATS_FRAGMENT);
        if (fragment != null && fragment.isAdded())
            ((ChatListFragment) fragment).changeData();

    }

    private void setHashMap() {
        hmNavigationIds = new HashMap<>();
        hmNavigationIds.put(Constants.TAG_HOME_FRAGMENT, R.id.nav_home);
        hmNavigationIds.put(TAG_MATCHES_FRAGMENT, R.id.nav_matches);
        hmNavigationIds.put(TAG_PROFILE_FRAGMENT, R.id.nav_profile);
        hmNavigationIds.put(TAG_PREFERENCES_FRAGMENT, R.id.nav_preferences);
        hmNavigationIds.put(TAG_CHATS_FRAGMENT, R.id.nav_chats);
    }

    @Override
    public void onBackPressed() {
        if (activityShowProfilesBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityShowProfilesBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (backStackEntryCount > 0) {
                if (backStackEntryCount == 1)
                    finish();
                else
                    getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startHomeActivity();
        } else if (id == R.id.nav_matches) {
            changeFragment(MatchedProfileFragment.newInstance(isDating), TAG_MATCHES_FRAGMENT);
        } else if (id == R.id.nav_profile) {
            changeFragment(EditProfileFragment.newInstance(isDating), TAG_PROFILE_FRAGMENT);
        } else if (id == R.id.nav_preferences) {
            changeFragment(PreferenceFragment.newInstance(isDating), TAG_PREFERENCES_FRAGMENT);
        } else if (id == R.id.nav_chats) {
            changeFragment(ChatListFragment.newInstance(isDating), TAG_CHATS_FRAGMENT);
        }

        activityShowProfilesBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startHomeActivity() {
        int flags = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
        startActivity(new Intent(this, HomeActivity.class).addFlags(flags));
    }

    private void changeFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();

        boolean fragmentPopped = fm.popBackStackImmediate(tag, 0);

        if (!fragmentPopped && fm.findFragmentByTag(tag) == null) {
            addFragmentWithAnimation(R.id.content_frame, fragment, tag, true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof EditProfileFragment)
            ((EditProfileFragment) fragment).onLocationChanged(location);
    }

    @Override
    public void onBackStackChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null && fragment.getTag() != null) {
            String tag = fragment.getTag();
            setTitle(tag);
            if (!Constants.TAG_SHOW_PROFILES_FRAGMENT.equals(tag))
                activityShowProfilesBinding.navView.setCheckedItem(hmNavigationIds.get(tag));
            if (tag.equals(Constants.TAG_CHATS_FRAGMENT))
                setMenuItemVisibility(false);
            else
                setMenuItemVisibility(true);
        }
    }

    private void setMenuItemVisibility(boolean visibility) {
        if (menu != null) {
            MenuItem menuItem = menu.findItem(R.id.action_chats);
            if (menuItem != null)
                menuItem.setVisible(visibility);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat, menu);
        this.menu = menu;

        setNotificationViews();
        getUnreadMessages();
        return super.onCreateOptionsMenu(menu);
    }

    private void setNotificationViews() {
        View vNotificationCount = menu.findItem(R.id.action_chats).getActionView();
        tvNotificationCount = vNotificationCount.findViewById(R.id.tv_notification_count);

        vNotificationCount.setOnClickListener(v -> changeFragment(ChatListFragment.newInstance(isDating), TAG_CHATS_FRAGMENT));
        tvNotificationCount.setOnClickListener(v -> changeFragment(ChatListFragment.newInstance(isDating), TAG_CHATS_FRAGMENT));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        if (internetDisposable != null && !internetDisposable.isDisposed())
            internetDisposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();

        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setConnection, Crashlytics::logException);
        if (menu != null)
            getUnreadMessages();
    }

    private void setConnection(Boolean isConnected) {

        if (isConnected || webSocket == null) {
            connectWebSocket();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webSocket != null)
            webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable subscription) {

        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

    private void connectWebSocket() {

        okhttp3.Request request = new okhttp3.Request.Builder().url(NetworkConstants.WEB_SOCKET_END_POINT)
                .addHeader(NetworkConstants.Authorization, AppPrefs.getPrefsToken(this)).build();
        ChatWebSocketListener listener = new ChatWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    @Override
    public void onInsertDone(Chat chat, boolean isOwnMessage) {
        getUnreadMessages();
    }

    @Override
    public void onUpdateDone(int updatedRowsCount, boolean isOwnMessage) {
        getUnreadMessages();
    }

    private void getUnreadMessages() {

        unreadMessagesDisposable = Observable.fromCallable(() -> chatRepository.getTotalUnreadMessagesCount(AppPrefs.getPrefsUserId(this), AppUtil.getChatType(isDating)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCount, Crashlytics::logException);
    }

    // WebSocket
    private final class ChatWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            LogUtils.LOGD(TAG, "WebSocket connected");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtils.LOGD(TAG, "Rx: " + text);
            handleIncomingMessage(text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(Constants.NORMAL_CLOSURE_STATUS, null);
            ShowProfilesActivity.this.webSocket = null;
            LogUtils.LOGD(TAG, "Closed: " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            ShowProfilesActivity.this.webSocket = null;
            if (BuildConfig.DEBUG)
                t.printStackTrace();
            LogUtils.LOGD(TAG, "Error: " + t.getMessage());
            connectWebSocket();
        }
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
            }
    }
}
