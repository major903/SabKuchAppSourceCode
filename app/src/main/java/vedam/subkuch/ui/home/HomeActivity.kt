package vedam.subkuch.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import vedam.subkuch.network.Response
import com.google.android.material.navigation.NavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import contacts.core.BroadQuery
import contacts.core.Contacts
import contacts.core.util.phoneList
import vedam.subkuch.R
import vedam.subkuch.RegistrationIntentService
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityHomeBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.network.DataFetcher.addReferral
import vedam.subkuch.network.DataFetcher.getBroadcastMessage
import vedam.subkuch.network.DataFetcher.getFeatures2
import vedam.subkuch.network.DataFetcher.getShareContent
import vedam.subkuch.network.DataFetcher.updateLocation
import vedam.subkuch.network.models.*
import vedam.subkuch.network.models.feature.Feature
import vedam.subkuch.network.models.feature.FeatureResponse
import vedam.subkuch.network.models.feature.Node
import vedam.subkuch.ui.ask.AskCategoryActivity
import vedam.subkuch.ui.classifieds.ClassifiedsActivity
import vedam.subkuch.ui.directory.DirectoryActivity
import vedam.subkuch.ui.events.EventActivity
import vedam.subkuch.ui.inbox.InboxActivity
import vedam.subkuch.ui.jobs.JobCategoryActivity
import vedam.subkuch.ui.jobs.models.AddResponse
import vedam.subkuch.ui.matrimonial.ShowProfilesActivity
import vedam.subkuch.ui.movies.MoviesActivity
import vedam.subkuch.ui.needs.NeedsActivity
import vedam.subkuch.ui.phonebook.PhoneBookActivity
import vedam.subkuch.ui.profile.EditProfileActivity
import vedam.subkuch.ui.public_utility.PublicUtilityActivity
import vedam.subkuch.ui.shopping.ShoppingActivity
import vedam.subkuch.ui.stafftrack.StaffTrackActivity
import vedam.subkuch.ui.transport.TransportActivity
import vedam.subkuch.ui.vehicle.VehicleActivity
import vedam.subkuch.ui.wallet.WalletActivity
import vedam.subkuch.update.AppUpdateManager
import vedam.subkuch.utils.*

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    InstallReferrerStateListener {
    private companion object {
        const val CASHBACK_MENU_ID = 3
    }

    private var binding: ActivityHomeBinding? = null
    private var referrerClient: InstallReferrerClient? = null
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    val iconHash = mapOf(
        1 to R.drawable.ic_menu_profile,
        2 to R.drawable.ic_menu_wallet,
        4 to R.drawable.ic_menu_inbox,
        5 to R.drawable.ic_drawer_contribute
    )
    private val featureViewIds = intArrayOf(
        R.id.iv_directory,
        R.id.iv_events,
        R.id.iv_jobs,
        R.id.iv_movies,
        R.id.iv_classifieds,
        R.id.iv_needs,
        R.id.iv_ask_me,
        R.id.iv_dating,
        R.id.iv_matrimonial,
        R.id.iv_public_utility,
        R.id.iv_bus,
        R.id.iv_phone_book,
        R.id.iv_transport,
        R.id.iv_offer,
        R.id.iv_gift
    )
    private val fallbackFeatureIcons = mapOf(
        R.id.iv_directory to R.drawable.directory,
        R.id.iv_events to R.drawable.learn,
        R.id.iv_jobs to R.drawable.jobs,
        R.id.iv_movies to R.drawable.movies,
        R.id.iv_classifieds to R.drawable.classifieds,
        R.id.iv_needs to R.drawable.needs,
        R.id.iv_ask_me to R.drawable.ask,
        R.id.iv_dating to R.drawable.dating,
        R.id.iv_matrimonial to R.drawable.matrimonial,
        R.id.iv_public_utility to R.drawable.public_utility,
        R.id.iv_bus to R.drawable.bustrain,
        R.id.iv_phone_book to R.drawable.phonebook,
        R.id.iv_transport to R.drawable.transport,
        R.id.iv_offer to R.drawable.offers,
        R.id.iv_gift to R.drawable.giftalife
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_home
        )
        showHomeImmediately()
        binding!!.drawerLayout.setStatusBarBackgroundColor(getColor(R.color.colorPrimary))
        // Keep the supplied vector colors instead of NavigationView's default gray tint.
        binding!!.navView.itemIconTintList = null
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding!!.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
        val toggle = ActionBarDrawerToggle(
            this,
            binding!!.drawerLayout,
            getToolbar(),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding!!.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding!!.navView.setNavigationItemSelectedListener(this)
        val tvName =
            binding!!.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_name)
        tvName.text = AppPrefs.getPrefsUserName(this)
        binding!!.navView.getHeaderView(0).findViewById<View>(R.id.btn_close_drawer)
            .setOnClickListener { binding!!.drawerLayout.closeDrawer(GravityCompat.START) }
        val privacyFooter = binding!!.navView.findViewById<View>(R.id.ll_tnc)
        val initialPrivacyFooterLeft = privacyFooter.paddingLeft
        val initialPrivacyFooterTop = privacyFooter.paddingTop
        val initialPrivacyFooterRight = privacyFooter.paddingRight
        val initialPrivacyFooterBottom = privacyFooter.paddingBottom
        fun applyPrivacyFooterInsets(insets: WindowInsetsCompat) {
            val footerInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            val rootInset = ViewCompat.getRootWindowInsets(privacyFooter)
                ?.getInsets(WindowInsetsCompat.Type.navigationBars())
                ?.bottom ?: 0
            privacyFooter.setPadding(
                initialPrivacyFooterLeft,
                initialPrivacyFooterTop,
                initialPrivacyFooterRight,
                initialPrivacyFooterBottom + maxOf(footerInset, rootInset)
            )
        }
        ViewCompat.setOnApplyWindowInsetsListener(privacyFooter) { _, insets ->
            applyPrivacyFooterInsets(insets)
            insets
        }
        privacyFooter.post {
            ViewCompat.getRootWindowInsets(privacyFooter)?.let(::applyPrivacyFooterInsets)
        }
        privacyFooter.setOnClickListener { AppUtil.openUrl(this, Constants.PRIVACY_POLICY_URL) }
        handleReferral()
        binding!!.root.post {
            if (!isFinishing && !isDestroyed) {
                requestLocation(false)
                getMenus()
                getFeatures()
                getBroadCastMessage()
                registerFCM()
                requestNotificationPermissionIfNeeded()
            }
        }
    }

    private fun showHomeImmediately() {
        val cachedFeatures = AppPrefs.getPrefsHomeFeatures(this)
            .takeIf { it.isNotBlank() }
            ?.let {
                try {
                    Gson().fromJson(it, Feature::class.java)
                } catch (exception: Exception) {
                    FirebaseCrashlytics.getInstance().recordException(exception)
                    null
                }
            }

        if (cachedFeatures != null) {
            renderFeatures(cachedFeatures)
        } else {
            hideFeatureViews()
            fallbackFeatureIcons.forEach { (viewId, drawableId) ->
                binding!!.root.findViewById<ImageView>(viewId).apply {
                    setImageResource(drawableId)
                    visibility = View.VISIBLE
                    isEnabled = false
                }
            }
            binding!!.root.findViewById<View>(R.id.ll_container).visibility = View.VISIBLE
        }
    }

    private fun renderFeatures(features: Feature) {
        hideFeatureViews()
        enableFeatures(features)
        binding!!.root.findViewById<View>(R.id.ll_container).visibility = View.VISIBLE
    }

    private fun hideFeatureViews() {
        featureViewIds.forEach { viewId ->
            binding!!.root.findViewById<ImageView>(viewId).apply {
                visibility = View.GONE
                isEnabled = false
                tag = null
            }
        }
    }

    private val onBroadcastSuccessListener = Response.Listener { response: BroadcastResponse? ->
        if (response != null && response.returnData != null && response.returnCode == Constants.SUCCESS_RETURN_CODE) {
            val b = response.returnData
            if (!b.status && !TextUtils.isEmpty(b.message2)) {
                showContactsPopup(b.message2)
            } else if (!TextUtils.isEmpty(b.message1)) UiUtil.showDialog(this, b.message1, false)
        }
    }

    private fun showContactsPopup(message: String) {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage(message)
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
                attemptAddContacts()
                dialogInterface.dismiss()
            }
            .setNegativeButton("Decline") { dialogInterface: DialogInterface, i: Int ->
                updateStatus()
                dialogInterface.dismiss()
            }
            .show()
        val font2 = UiUtil.getTypeface(this, getString(R.string.typeface_regular))
        val tvMessage = dialog.findViewById<TextView>(android.R.id.message)
        UiUtil.setTypeface(tvMessage, font2)
    }

    private fun attemptAddContacts() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val result = Contacts(this@HomeActivity).broadQuery().find()
                    addContact(result)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    updateStatus()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }

    private fun addContact(result: BroadQuery.Result) {
        val userId = AppPrefs.getPrefsUserId(this).toIntOrNull() ?: return
        val list = arrayListOf<ContactObject>()
        result.forEach {
            val contactObj =
                ContactObject(UserId = userId, Name = it.displayNamePrimary)

            if (it.hasPhoneNumber == true) {
                for ((index, phone) in it.phoneList().withIndex()) {
                    if (index == 0 && phone.normalizedNumber != null)
                        contactObj.Mobile1 = phone.normalizedNumber
                    else if (index == 1 && phone.normalizedNumber != null)
                        contactObj.Mobile2 = phone.normalizedNumber
                    else if (index == 2 && phone.normalizedNumber != null)
                        contactObj.Mobile3 = phone.normalizedNumber
                    else
                        break
                }
            }
            if (!TextUtils.isEmpty(contactObj.Mobile1))
                list.add(contactObj)
        }

        val type = object : TypeToken<BaseResponse<String>>() {}.type
        DataFetcher.addContacts(this, Gson().toJson(list), onAddContactSuccessListener, type, null)
    }

    private fun updateStatus() {
        val userId = AppPrefs.getPrefsUserId(this).toIntOrNull() ?: return
        val obj = ContactObject(UserId = userId)
        val list = arrayListOf(obj)
        val type = object : TypeToken<BaseResponse<String>>() {}.type
        DataFetcher.addContacts(this, Gson().toJson(list), onAddContactSuccessListener, type, null)
    }

    private val onAddContactSuccessListener = Response.Listener { response: BaseResponse<String>? ->

    }

    private fun getBroadCastMessage() {
        getBroadcastMessage(
            this,
            onBroadcastSuccessListener,
            BroadcastResponse::class.java,
            onErrorListener
        )
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun registerFCM() {
        val sentToken = AppPrefs.getPrefsIsTokenSent(this)
        if (!sentToken) if (AppUtil.checkPlayServices(this@HomeActivity)) {
            // Start IntentService to register this application with FCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            RegistrationIntentService.enqueueWork(this, intent)
        }
    }

    private fun handleReferral() {
        val isReferralDone = AppPrefs.getPrefsIsReferralDone(this)
        if (TextUtils.isEmpty(isReferralDone)) logout() else if (Constants.FALSE.equals(
                isReferralDone,
                ignoreCase = true
            )
        ) {
            referrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient?.startConnection(this)
        }
    }

    override fun onLocationChanged(location: Location?) {
        updateLocation(
            this,
            onUpdateLocationSuccessListener,
            AddEventResponse::class.java,
            null,
            location?.latitude.toString(),
            location?.longitude.toString()
        )
    }

    private val onUpdateLocationSuccessListener = Response.Listener { response: AddEventResponse ->
        LogUtils.LOGD(
            "Update Location",
            response.toString()
        )
    }

    private fun getMenus() {
        val type = object : TypeToken<BaseResponse<ArrayList<ArrayList<OMenu>>>>() {}.type
        DataFetcher.getMenus(
            this,
            onMenuSuccessListener,
            type,
            onErrorListener
        )
    }

    private val onMenuSuccessListener =
        Response.Listener { response: BaseResponse<ArrayList<ArrayList<OMenu>>>? ->
            if (response != null) {
                if (response.returnCode == Constants.SUCCESS_RETURN_CODE) {
                    setMenu(response.returnData)
                } else if (!TextUtils.isEmpty(response.returnMessage)) UiUtil.showToast(
                    this@HomeActivity,
                    response.returnMessage
                )
            } else UiUtil.showToast(this@HomeActivity, getString(R.string.err_occurred))
        }

    private fun setMenu(menus: ArrayList<ArrayList<OMenu>>?) {
        menus?.get(0)?.let {
            binding?.navView?.menu?.clear()
            for (menu in it) {
                if (menu.MenuId == CASHBACK_MENU_ID) continue

                val a = binding?.navView?.menu?.add(0, menu.MenuId, 0, menu.name)
                a?.apply {
                    setIcon(iconHash[menu.MenuId] ?: R.drawable.ic_menu_inbox)
                    isCheckable = true
                    isChecked = menu.MenuId == 1
                }
            }
            binding?.navView?.menu?.add(
                0,
                R.id.nav_app_update,
                0,
                R.string.check_for_app_update
            )?.apply {
                setIcon(R.drawable.ic_drawer_update)
                isCheckable = false
            }
            binding?.navView?.invalidate()
        }
    }

    private fun getFeatures() {
        getFeatures2(
            this,
            onFeaturesSuccessListener,
            FeatureResponse::class.java,
            onErrorListener
        )
    }

    private val onFeaturesSuccessListener = Response.Listener { response: FeatureResponse? ->
        if (response != null) {
            if (response.returnCode == Constants.SUCCESS_RETURN_CODE) {
                response.returnData?.let { features ->
                    AppPrefs.setPrefsHomeFeatures(this, Gson().toJson(features))
                    renderFeatures(features)
                }
            } else if (!TextUtils.isEmpty(response.returnMessage)) UiUtil.showToast(
                this@HomeActivity,
                response.returnMessage
            )
        } else UiUtil.showToast(this@HomeActivity, getString(R.string.err_occurred))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.action_share) getShareMessage()
        return super.onOptionsItemSelected(item)
    }

    private fun getShareMessage() {
        UiUtil.showProgressDialog(this, R.string.loading)
        getShareContent(
            this,
            onShareSuccessListener,
            ShareResponse::class.java,
            onErrorListener
        )
    }

    private val onShareSuccessListener = Response.Listener { response: ShareResponse? ->
        UiUtil.cancelProgressDialog()
        if (response != null && response.returnMessage == Constants.SUCCESS && response.returnData != null) {
            handleAppShare(response.returnData)
        } else UiUtil.showToast(this@HomeActivity, getString(R.string.err_occurred))
    }

    private fun handleAppShare(data: String) {
        if (TextUtils.isEmpty(data)) {
            UiUtil.showToast(this, getString(R.string.no_share_content))
            return
        }
        try {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            i.putExtra(Intent.EXTRA_TEXT, data)
            startActivity(Intent.createChooser(i, "Choose one"))
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun enableFeatures(response: Feature) {
        if (response.node1 != null) enableFeaturesByNode1(response.node1)
        if (response.node2 != null) enableFeaturesByNode2(response.node2)
        if (response.node3 != null) enableFeaturesByNode3(response.node3)
        if (response.node4 != null) enableFeaturesByNode4(response.node4)
        if (response.node5 != null) enableFeaturesByNode5(response.node5)
    }

    private fun enableFeaturesByNode1(nodes: ArrayList<Node>) {
        for (feature in nodes) {
            when (feature.name) {
                Constants.Directory -> {
                    val ivDirectory =
                        binding!!.root.findViewById<ImageView>(R.id.iv_directory)
                    ivDirectory.visibility = View.VISIBLE
                    setImage(ivDirectory, R.drawable.directory, feature)
                }
                Constants.Events -> {
                    val ivEvent = binding!!.root.findViewById<ImageView>(R.id.iv_events)
                    ivEvent.visibility = View.VISIBLE
                    ivEvent.tag = feature
                    ivEvent.isEnabled = true
                    ivEvent.setImageResource(R.drawable.learn)
                }
                Constants.Jobs -> {
                    val ivJobs = binding!!.root.findViewById<ImageView>(R.id.iv_jobs)
                    ivJobs.visibility = View.VISIBLE
                    setImage(ivJobs, R.drawable.jobs, feature)
                }
                Constants.Movies -> {
                    val ivMovies =
                        binding!!.root.findViewById<ImageView>(R.id.iv_movies)
                    ivMovies.visibility = View.VISIBLE
                    setImage(ivMovies, R.drawable.movies, feature)
                }
            }
        }
    }

    private fun setImage(ivDirectory: ImageView, resourceId: Int, feature: Node) {
        ivDirectory.tag = feature
        ivDirectory.isEnabled = true
        UiUtil.setImageView(
            ImageSetter.ImageBuilder(this)
                .setImageLink(feature.iconUrl)
                .setPlaceholderResource(resourceId)
                .setErrorResource(resourceId)
                .setTarget(ivDirectory).build()
        )
    }

    private fun enableFeaturesByNode2(nodes: ArrayList<Node>) {
        for (feature in nodes) {
            when (feature.name) {
                Constants.Ask_Me -> {
                    val ivAskMe = binding!!.root.findViewById<ImageView>(R.id.iv_ask_me)
                    ivAskMe.visibility = View.VISIBLE
                    setImage(ivAskMe, R.drawable.ask, feature)
                }
                Constants.Classifieds -> {
                    val ivClassifieds =
                        binding!!.root.findViewById<ImageView>(R.id.iv_classifieds)
                    ivClassifieds.visibility = View.VISIBLE
                    setImage(ivClassifieds, R.drawable.classifieds, feature)
                }
                Constants.Needs -> {
                    val ivNeeds = binding!!.root.findViewById<ImageView>(R.id.iv_needs)
                    ivNeeds.visibility = View.VISIBLE
                    setImage(ivNeeds, R.drawable.needs, feature)
                }
            }
        }
    }

    private fun enableFeaturesByNode3(nodes: ArrayList<Node>) {
        for (feature in nodes) {
            when (feature.name) {
                Constants.Dating -> {
                    val ivDating =
                        binding!!.root.findViewById<ImageView>(R.id.iv_dating)
                    ivDating.visibility = View.VISIBLE
                    setImage(ivDating, R.drawable.dating, feature)
                }
                Constants.Matrimonial -> {
                    val ivMatrimonial =
                        binding!!.root.findViewById<ImageView>(R.id.iv_matrimonial)
                    ivMatrimonial.visibility = View.VISIBLE
                    setImage(ivMatrimonial, R.drawable.matrimonial, feature)
                }
            }
        }
    }

    private fun enableFeaturesByNode4(nodes: ArrayList<Node>) {
        for (feature in nodes) {
            when (feature.name) {
                Constants.Phone_book -> {
                    val ivPhoneBook =
                        binding!!.root.findViewById<ImageView>(R.id.iv_phone_book)
                    ivPhoneBook.visibility = View.VISIBLE
                    setImage(ivPhoneBook, R.drawable.phonebook, feature)
                }
                Constants.Public_Transport_Timings -> {
                    val ivBus = binding!!.root.findViewById<ImageView>(R.id.iv_bus)
                    ivBus.visibility = View.VISIBLE
                    setImage(ivBus, R.drawable.bustrain, feature)
                }
                Constants.Public_Utility -> {
                    val ivPublicUtility =
                        binding!!.root.findViewById<ImageView>(R.id.iv_public_utility)
                    ivPublicUtility.visibility = View.VISIBLE
                    setImage(ivPublicUtility, R.drawable.public_utility, feature)
                }
            }
        }
    }

    private fun enableFeaturesByNode5(nodes: ArrayList<Node>) {
        for (feature in nodes) {
            when (feature.name) {
                Constants.Goods_Transport -> {
                    val ivTransport =
                        binding!!.root.findViewById<ImageView>(R.id.iv_transport)
                    ivTransport.visibility = View.VISIBLE
                    setImage(ivTransport, R.drawable.transport, feature)
                }
                Constants.Gift_A_Life -> {
                    val ivGift = binding!!.root.findViewById<ImageView>(R.id.iv_gift)
                    ivGift.visibility = View.VISIBLE
                    setImage(ivGift, R.drawable.giftalife, feature)
                }
                Constants.Offers -> {
                    val ivOffers = binding!!.root.findViewById<ImageView>(R.id.iv_offer)
                    ivOffers.visibility = View.VISIBLE
                    setImage(ivOffers, R.drawable.offers, feature)
                }
            }
        }
    }

    /*public void newsClick(View v)
    {
        startActivity(new Intent(this,NewsActivity.class));
    }*/
    fun directoryClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, DirectoryActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    private fun isEnabled(v: View): Boolean {
        return (v.tag as Node).isEnabled
    }

    /*public void datingClick(View v) {
        startActivity(new Intent(this, DatingActivity.class));
    }*/
    /*public void classifiedsClick(View v) {
        startActivity(new Intent(this, ClassifiedsActivity.class));
    }
*/
    /*public void PropertiesClick(View v)
    {
        startActivity(new Intent(this,Properties.class));
    }*/
    fun specialOfferClick(v: View) {
        if (isEnabled(v)) {
//            startActivity(new Intent(this, OffersActivity.class));
            startActivity(Intent(this, ShoppingActivity::class.java))
        } else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    /*public void eCommerceClick(View v)
    {
        startActivity(new Intent(this,Ecommerce.class));
    }*/
    fun busClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, VehicleActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun phoneBookClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, PhoneBookActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun datingClick(v: View) {
        if (isEnabled(v)) startActivity(
            Intent(this, ShowProfilesActivity::class.java).putExtra(
                Constants.EXTRA_IS_DATING, true
            )
        ) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun matrimonialClick(v: View) {
        if (isEnabled(v)) startActivity(
            Intent(this, ShowProfilesActivity::class.java).putExtra(
                Constants.EXTRA_IS_DATING, false
            )
        ) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun askClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, AskCategoryActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun eventsClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, EventActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun jobsClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, JobCategoryActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun moviesClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, MoviesActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun transportClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, TransportActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun giftALifeClick(v: View?) {
//        startActivity(new Intent(this, JobCategoryActivity.class));
    }

    fun needsClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, NeedsActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun publicUtilityClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, PublicUtilityActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    fun classifiedsClick(v: View) {
        if (isEnabled(v)) startActivity(Intent(this, ClassifiedsActivity::class.java)) else {
            val message = (v.tag as Node).message
            if (!TextUtils.isEmpty(message)) UiUtil.showDialog(this, (v.tag as Node).message, true)
        }
    }

    /*public void InfoClick(View v)
    {
        startActivity(new Intent(this,Info.class));
    }*/
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == 1) {
            startActivity(Intent(this, EditProfileActivity::class.java))
        } else if (id == 2) {
            startActivity(Intent(this, WalletActivity::class.java))
        } else if (id == 4) {
            startActivity(Intent(this, InboxActivity::class.java))
        } else if (id == 5) {
            startActivity(Intent(this, StaffTrackActivity::class.java))
        } else if (id == R.id.nav_app_update) {
            AppUpdateManager.checkForUpdates(this)
        }
        if (id != R.id.nav_app_update) item.isChecked = true
        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
            InstallReferrerResponse.OK -> {
                val response: ReferrerDetails
                try {
                    response = referrerClient!!.installReferrer
                    addReferralCode(response.installReferrer)
                } catch (e: Exception) {
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
            InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {}
            InstallReferrerResponse.SERVICE_UNAVAILABLE -> {}
            InstallReferrerResponse.DEVELOPER_ERROR, InstallReferrerResponse.SERVICE_DISCONNECTED -> {}
        }
    }

    override fun onInstallReferrerServiceDisconnected() {
        if (referrerClient != null && !referrerClient!!.isReady) referrerClient!!.startConnection(
            this
        )
    }

    private fun addReferralCode(referrerCode: String) {
        if (!TextUtils.isEmpty(referrerCode) && !referrerCode.startsWith("utm")) {
            val referralRequest = ReferralRequest()
            referralRequest.setProfileId(AppPrefs.getPrefsUserId(this))
            referralRequest.setReferredBy(referrerCode)
            addReferral(
                this,
                Gson().toJson(referralRequest),
                onAddReferralSuccessListener,
                AddResponse::class.java,
                onErrorListener
            )
        }
    }

    private val onAddReferralSuccessListener = Response.Listener { response: AddResponse? ->
        if (response != null && response.returnMessage == Constants.SUCCESS) {
            AppPrefs.setPrefsIsReferralDone(this, Constants.TRUE)
            endConnection()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        endConnection()
    }

    private fun endConnection() {
        if (referrerClient != null && referrerClient!!.isReady) referrerClient!!.endConnection()
    }
}
