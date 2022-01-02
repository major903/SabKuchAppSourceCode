package vedam.subkuch.ui.matrimonial;


import static vedam.subkuch.helpers.Constants.TAG_CHATS_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_MATCHES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PREFERENCES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PROFILE_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_SHOW_PROFILES_FRAGMENT;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityShowProfilesBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.network.DataFetcher;
import vedam.subkuch.network.models.AddEventResponse;
import vedam.subkuch.ui.chat.ChatListFragment;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.matrimonial.editProfile.EditProfileFragment;
import vedam.subkuch.ui.matrimonial.preference.PreferenceFragment;
import vedam.subkuch.utils.AppPrefs;

public class ShowProfilesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private ActivityShowProfilesBinding activityShowProfilesBinding;
    private HashMap<String, Integer> hmNavigationIds;
    private boolean isDating;
    private Menu menu;
    private TextView tvNotificationCount;
    private ListenerRegistration snapshotListener;

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

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CHATS_FRAGMENT);
//        if (fragment != null && fragment.isAdded())
//            ((ChatListFragment) fragment).changeData();

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
        DataFetcher.updateLocation(this, null, AddEventResponse.class, null, String.valueOf(location.getLatitude())
                , String.valueOf(location.getLongitude()));
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
        if (snapshotListener != null)
            snapshotListener.remove();
    }

    private void getUnreadMessages() {

        snapshotListener = FirebaseFirestore.getInstance().collection(Constants.TABLE_MESSAGES)
                .whereEqualTo(Constants.ToProfileId, AppPrefs.getPrefsUserId(this))
                .whereEqualTo(Constants.read, false)
                .addSnapshotListener((value, error) -> {
                    if (value != null)
                        setCount(value.getDocuments().size());
                });
    }
}
