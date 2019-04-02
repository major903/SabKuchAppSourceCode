package vedam.subkuch.ui.matrimonial;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

import vedam.subkuch.R;
import vedam.subkuch.base.BaseActivity;
import vedam.subkuch.databinding.ActivityShowProfilesBinding;
import vedam.subkuch.helpers.Constants;
import vedam.subkuch.ui.home.HomeActivity;
import vedam.subkuch.ui.matrimonial.editProfile.EditProfileFragment;
import vedam.subkuch.ui.matrimonial.preference.PreferenceFragment;
import vedam.subkuch.utils.AppPrefs;

import static vedam.subkuch.helpers.Constants.TAG_CHATS_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_MATCHES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PREFERENCES_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_PROFILE_FRAGMENT;
import static vedam.subkuch.helpers.Constants.TAG_SHOW_PROFILES_FRAGMENT;

public class ShowProfilesActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private ActivityShowProfilesBinding activityShowProfilesBinding;
    private HashMap<String, Integer> hmNavigationIds;
    private boolean isDating;
    private Menu menu;

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startHomeActivity();
        } else if (id == R.id.nav_matches) {
            changeFragment(MatchedProfileFragment.newInstance(false, isDating), TAG_MATCHES_FRAGMENT);
        } else if (id == R.id.nav_profile) {
            changeFragment(EditProfileFragment.newInstance(isDating), TAG_PROFILE_FRAGMENT);
        } else if (id == R.id.nav_preferences) {
            changeFragment(PreferenceFragment.newInstance(isDating), TAG_PREFERENCES_FRAGMENT);
        } else if (id == R.id.nav_chats) {
            changeFragment(MatchedProfileFragment.newInstance(true, isDating), TAG_CHATS_FRAGMENT);
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chats:
                changeFragment(MatchedProfileFragment.newInstance(true, isDating), TAG_CHATS_FRAGMENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
    }
}
