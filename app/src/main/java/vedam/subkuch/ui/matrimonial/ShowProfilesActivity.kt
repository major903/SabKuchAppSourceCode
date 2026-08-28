package vedam.subkuch.ui.matrimonial

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import vedam.subkuch.network.Response
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.reflect.TypeToken
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityShowProfilesBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.DataFetcher.updateLocation
import vedam.subkuch.network.models.AddEventResponse
import vedam.subkuch.network.models.BaseResponse
import vedam.subkuch.network.models.MenuIds
import vedam.subkuch.network.models.MenuPage
import vedam.subkuch.network.models.OMenu
import vedam.subkuch.ui.chat.ChatListFragment.Companion.newInstance
import vedam.subkuch.ui.home.HomeActivity
import vedam.subkuch.ui.inbox.InboxActivity
import java.util.Locale
import vedam.subkuch.ui.matrimonial.editProfile.EditProfileFragment
import vedam.subkuch.ui.matrimonial.preference.PreferenceFragment
import vedam.subkuch.ui.contribute.ContributeActivity
import vedam.subkuch.ui.profile.EditProfileActivity
import vedam.subkuch.ui.wallet.WalletActivity
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.MenuCache
import vedam.subkuch.utils.UiUtil

class ShowProfilesActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentManager.OnBackStackChangedListener {
    private var binding: ActivityShowProfilesBinding? = null
    private var hmNavigationIds: HashMap<String?, Int>? = null
    private var isDating = false
    private var menu: Menu? = null
    private var tvNotificationCount: TextView? = null
    private var snapshotListener: ListenerRegistration? = null
    private var renderedMenus: List<OMenu>? = null

    val iconHash = mapOf(
        MenuIds.EDIT_PROFILE to R.drawable.ic_menu_profile,
        MenuIds.WALLET to R.drawable.ic_menu_wallet,
        MenuIds.CONTRIBUTE to R.drawable.ic_drawer_contribute,
        MenuIds.INBOX to R.drawable.ic_menu_inbox
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_profiles)
        // Keep the supplied vector colors instead of NavigationView's default gray tint.
        binding!!.navView.itemIconTintList = null
        showMenuImmediately()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                    return
                }
                val backStackEntryCount = supportFragmentManager.backStackEntryCount
                if (backStackEntryCount > 0) {
                    if (backStackEntryCount == 1) finish() else supportFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
        setTitle(R.string.profiles)
        setToolbarBackButton()
        initUI()
        getMenus()
        bindData()
        bindCallbacks()
        setHashMap()
    }

    private fun getMenus() {
        val type = object : TypeToken<BaseResponse<MenuPage>>() {}.type
        DataFetcher.getMenus(
            this,
            onMenuSuccessListener,
            type,
            onErrorListener
        )
    }

    private val onMenuSuccessListener =
        Response.Listener { response: BaseResponse<MenuPage>? ->
            if (response != null) {
                if (response.returnCode == Constants.SUCCESS_RETURN_CODE) {
                    response.returnData?.menus?.let { menus ->
                        MenuCache.save(this, menus)
                        setMenu(menus)
                    }
                } else {
                    if (!TextUtils.isEmpty(response.returnMessage)) UiUtil.showToast(
                        this@ShowProfilesActivity,
                        response.returnMessage
                    )
                }
            } else UiUtil.showToast(this@ShowProfilesActivity, getString(R.string.err_occurred))
        }

    private fun setMenu(menus: List<OMenu>?) {
        menus?.let { incomingMenus ->
            val stableMenus = MenuCache.stableOrder(incomingMenus)
            if (MenuCache.hasSameVisibleContent(renderedMenus, stableMenus)) {
                renderedMenus = stableMenus
                return
            }
            renderedMenus = stableMenus
            binding?.navView?.menu?.clear()
            for (menu in stableMenus) {
                val a = binding?.navView?.menu?.add(0, menu.MenuId, 0, menu.name)
                a?.apply {
                    setIcon(iconHash[menu.MenuId] ?: R.drawable.ic_menu_inbox)
                    isCheckable = true
                    isChecked = menu.MenuId == MenuIds.EDIT_PROFILE
                }
            }
            binding?.navView?.invalidate()
        }
    }

    private fun showMenuImmediately() {
        val cachedMenus = MenuCache.load(this)
        if (cachedMenus != null) setMenu(cachedMenus)
    }

    private fun initUI() {
        isDating = intent.getBooleanExtra(Constants.EXTRA_IS_DATING, false)
        val toggle = ActionBarDrawerToggle(
            this,
            binding!!.drawerLayout,
            getToolbar(),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding!!.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        addFragment(
            R.id.content_frame,
            ShowProfilesFragment.newInstance(isDating),
            Constants.TAG_SHOW_PROFILES_FRAGMENT,
            true,
            0,
            0,
            0,
            0
        )
    }

    private fun bindCallbacks() {
        supportFragmentManager.addOnBackStackChangedListener(this)
        binding!!.navView.setNavigationItemSelectedListener(this)
        findViewById<View>(R.id.iv_match).setOnClickListener { v: View? ->
            changeFragment(
                MatchedProfileFragment.newInstance(isDating),
                Constants.TAG_MATCHES_FRAGMENT
            )
        }
        findViewById<View>(R.id.iv_chat).setOnClickListener { v: View? ->
            changeFragment(
                newInstance(
                    isDating
                ), Constants.TAG_CHATS_FRAGMENT
            )
        }
        findViewById<View>(R.id.iv_settings).setOnClickListener { v: View? ->
            changeFragment(
                PreferenceFragment.newInstance(isDating), Constants.TAG_PREFERENCES_FRAGMENT
            )
        }
        findViewById<View>(R.id.iv_profile).setOnClickListener { v: View? ->
            changeFragment(
                EditProfileFragment.newInstance(isDating),
                Constants.TAG_PROFILE_FRAGMENT
            )
        }
    }

    private fun bindData() {
        val tvName = binding!!.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
        tvName.text = AppPrefs.getPrefsUserName(this)
        binding!!.navView.getHeaderView(0).findViewById<View>(R.id.btn_close_drawer)
            .setOnClickListener { binding!!.drawerLayout.closeDrawer(GravityCompat.START) }
    }

    private fun setCount(count: Int) {
        if (count == 0) tvNotificationCount!!.visibility = View.GONE else if (count < 100) {
            tvNotificationCount!!.visibility = View.VISIBLE
            tvNotificationCount!!.text = String.format(Locale.US, "%d", count)
        } else {
            tvNotificationCount!!.visibility = View.VISIBLE
            tvNotificationCount!!.text = getString(R.string.max_notification_number)
        }

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_CHATS_FRAGMENT);
//        if (fragment != null && fragment.isAdded())
//            ((ChatListFragment) fragment).changeData();
    }

    private fun setHashMap() {
        hmNavigationIds = HashMap()
        hmNavigationIds!![Constants.TAG_HOME_FRAGMENT] = R.id.nav_home
        hmNavigationIds!![Constants.TAG_MATCHES_FRAGMENT] = R.id.nav_matches
        hmNavigationIds!![Constants.TAG_PROFILE_FRAGMENT] = R.id.nav_profile
        hmNavigationIds!![Constants.TAG_PREFERENCES_FRAGMENT] = R.id.nav_preferences
        hmNavigationIds!![Constants.TAG_CHATS_FRAGMENT] = R.id.nav_chats
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == MenuIds.EDIT_PROFILE) {
            startActivity(Intent(this, EditProfileActivity::class.java))
        } else if (id == MenuIds.WALLET) {
            startActivity(Intent(this, WalletActivity::class.java))
        } else if (id == MenuIds.CONTRIBUTE) {
            startActivity(Intent(this, ContributeActivity::class.java))
        } else if (id == MenuIds.INBOX) {
            startActivity(Intent(this, InboxActivity::class.java))
        } else if (id == R.id.nav_home) {
            startHomeActivity()
        } else if (id == R.id.nav_matches) {
            changeFragment(
                MatchedProfileFragment.newInstance(isDating),
                Constants.TAG_MATCHES_FRAGMENT
            )
        } else if (id == R.id.nav_profile) {
            changeFragment(
                EditProfileFragment.newInstance(isDating),
                Constants.TAG_PROFILE_FRAGMENT
            )
        } else if (id == R.id.nav_preferences) {
            changeFragment(
                PreferenceFragment.newInstance(isDating),
                Constants.TAG_PREFERENCES_FRAGMENT
            )
        } else if (id == R.id.nav_chats) {
            changeFragment(newInstance(isDating), Constants.TAG_CHATS_FRAGMENT)
        }
        item.isChecked = true
        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        val id = item.itemId
//        if (id == R.id.nav_home) {
//            startHomeActivity()
//        } else if (id == R.id.nav_matches) {
//            changeFragment(
//                MatchedProfileFragment.newInstance(isDating),
//                Constants.TAG_MATCHES_FRAGMENT
//            )
//        } else if (id == R.id.nav_profile) {
//            changeFragment(
//                EditProfileFragment.newInstance(isDating),
//                Constants.TAG_PROFILE_FRAGMENT
//            )
//        } else if (id == R.id.nav_preferences) {
//            changeFragment(
//                PreferenceFragment.newInstance(isDating),
//                Constants.TAG_PREFERENCES_FRAGMENT
//            )
//        } else if (id == R.id.nav_chats) {
//            changeFragment(newInstance(isDating), Constants.TAG_CHATS_FRAGMENT)
//        }
//        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
//        return true
//    }

    private fun startHomeActivity() {
        val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent(this, HomeActivity::class.java).addFlags(flags))
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        val fm = supportFragmentManager
        val fragmentPopped = fm.popBackStackImmediate(tag, 0)
        if (!fragmentPopped && fm.findFragmentByTag(tag) == null) {
            addFragmentWithAnimation(R.id.content_frame, fragment, tag, true)
        }
    }

    override fun onLocationChanged(location: Location?) {
        super.onLocationChanged(location)
        updateLocation(
            this,
            null,
            AddEventResponse::class.java,
            null,
            location!!.latitude.toString(),
            location.longitude.toString()
        )
        val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (fragment is EditProfileFragment) fragment.onLocationChanged(location)
    }

    override fun onBackStackChanged() {
        val fragment = supportFragmentManager.findFragmentById(R.id.content_frame)
        if (fragment != null && fragment.tag != null) {
            val tag = fragment.tag
            title = tag!!
//            if (Constants.TAG_SHOW_PROFILES_FRAGMENT != tag) binding!!.navView.setCheckedItem(
//                hmNavigationIds!![tag]!!
//            )
            if (tag == Constants.TAG_CHATS_FRAGMENT) setMenuItemVisibility(false) else setMenuItemVisibility(
                true
            )
        }
    }

    private fun setMenuItemVisibility(visibility: Boolean) {
        if (menu != null) {
            val menuItem = menu!!.findItem(R.id.action_chats)
            if (menuItem != null) menuItem.isVisible = visibility
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chat, menu)
        this.menu = menu
        setNotificationViews()
        getUnreadMessages()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setNotificationViews() {
        val vNotificationCount = menu!!.findItem(R.id.action_chats).actionView
        tvNotificationCount = vNotificationCount?.findViewById(R.id.tv_notification_count)
        vNotificationCount?.setOnClickListener { v: View? ->
            changeFragment(
                newInstance(isDating),
                Constants.TAG_CHATS_FRAGMENT
            )
        }
        tvNotificationCount?.setOnClickListener { v: View? ->
            changeFragment(
                newInstance(isDating), Constants.TAG_CHATS_FRAGMENT
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.removeOnBackStackChangedListener(this)
        if (snapshotListener != null) snapshotListener!!.remove()
    }

    private fun getUnreadMessages() {
        snapshotListener = FirebaseFirestore.getInstance().collection(Constants.TABLE_MESSAGES)
            .whereEqualTo(Constants.ToProfileId, AppPrefs.getPrefsUserId(this))
            .whereEqualTo(Constants.read, false)
            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (value != null) setCount(
                    value.documents.size
                )
            }
    }
}
