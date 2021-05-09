package vedam.subkuch.ui.chat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentChatListBinding
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.ui.matrimonial.models.DatingProfile
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil
import java.util.*

class ChatListFragment : BaseFragment(), OnListViewItemClickListener {
    private var fragmentChatListBinding: FragmentChatListBinding? = null
    private var adapter: ChatListAdapter? = null
    private val datingProfiles = ArrayList<DatingProfile>()
    private var loading = true
    private var linearLayoutManager: LinearLayoutManager? = null
    private var pageNo = 1
    private val pageSize = 20
    private var hasMoreProjects = true
    private var isDating = false
    private val viewModel: ChatViewModel by activityViewModels()
    private var snapshotListener: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDating = arguments?.getBoolean(Constants.EXTRA_IS_DATING) ?: false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentChatListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false)
        return fragmentChatListBinding?.root
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        setTitle(getString(R.string.matches))
        initUI()
        getMatchedProfiles()
    }

    private fun initUI() {
        linearLayoutManager = LinearLayoutManager(context)
        fragmentChatListBinding!!.rvChatList.layoutManager = linearLayoutManager
        fragmentChatListBinding!!.rvChatList.setHasFixedSize(true)
        adapter = ChatListAdapter(context, datingProfiles, this)
        fragmentChatListBinding!!.rvChatList.adapter = adapter
        fragmentChatListBinding!!.rvChatList.addOnScrollListener(ProfilesOnScrollListener())

        val task1 = Firebase.firestore.collection(Constants.TABLE_MESSAGES)
            .whereEqualTo(Constants.ToProfileId, AppPrefs.getPrefsUserId(context)).get()

        val task2 = Firebase.firestore.collection(Constants.TABLE_MESSAGES)
            .whereEqualTo(Constants.FromProfileId, AppPrefs.getPrefsUserId(context)).get()
//        val combinedTask = Tasks.whenAllSuccess<>()

//            .addSnapshotListener { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
//                if (value != null) setCount(
//                    value.documents.size
//                )
//            }
//        viewModel.chat.observe(viewLifecycleOwner, {
//            it?.let {
//                val profile = datingProfiles.firstOrNull { datingProfile ->
//                    datingProfile.profileId.getIdPair(
//                        AppPrefs.getPrefsUserId(context)
//                    ) == it.idPair
//                }
//                profile?.latestChat = it
//                adapter?.notifyDataSetChanged()
//            }
//        })
    }

    private fun String.getIdPair(chatToId: String): String {
        return if (this > chatToId)
            "${this}_$chatToId"
        else
            "${chatToId}_${this}"
    }

    fun getMatchedProfiles() {
        UiUtil.showProgressDialog(context, getString(R.string.please_wait))
        if (isDating) DataFetcher.getDatingMatchedChatProfiles(
            context,
            onMatchedProfilesSuccessListener,
            DatingProfileResponse::class.java,
            onErrorListener,
            pageNo,
            pageSize
        ) else DataFetcher.getMatrimonialMatchedChatProfiles(
            context,
            onMatchedProfilesSuccessListener,
            DatingProfileResponse::class.java,
            onErrorListener,
            pageNo,
            pageSize
        )
    }

    private fun startChatActivity(datingProfile: DatingProfile) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.firstName))
        intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.profileId)
        intent.putExtra(Constants.EXTRA_IS_DATING, isDating)
        startActivityForResult(intent, Constants.REQUEST_CHAT)
    }

    private val onMatchedProfilesSuccessListener =
        Response.Listener { response: DatingProfileResponse? ->
            UiUtil.cancelProgressDialog()
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                if (response.returnData.size > 0) {
                    hasMoreProjects = response.returnData.size >= pageSize
                    loading = true
                    loadValues(response.returnData)
                } else UiUtil.showToast(context, getString(R.string.no_matches_found))
            } else UiUtil.showToast(context, getString(R.string.err_occurred))
        }

    private fun loadValues(response: ArrayList<DatingProfile>?) {
        if (response != null && response.isNotEmpty()) {
            pageNo++
            datingProfiles.addAll(response)
            adapter = ChatListAdapter(context, datingProfiles, this)
            fragmentChatListBinding!!.rvChatList.adapter = adapter
        }
    }

    override fun <E> onItemClick(item: E?, position: Int, view: View?, action: ListItemClickAction?) {
        if (item != null) {
            val datingProfile = item as DatingProfile
            startChatActivity(datingProfile)
        }
    }

    inner class ProfilesOnScrollListener : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) //check for scroll down
            {
                val visibleItemCount = linearLayoutManager!!.childCount
                val totalItemCount = linearLayoutManager!!.itemCount
                val pastVisibleItems = linearLayoutManager!!.findFirstVisibleItemPosition()
                if (loading) {
                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        loading = false
                        if (hasMoreProjects) getMatchedProfiles()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CHAT && resultCode == Activity.RESULT_OK) {
            refreshData()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun refreshData() {
        pageNo = 1
        hasMoreProjects = true
        loading = true
        datingProfiles.clear()
        getMatchedProfiles()
    }

    fun changeData() {
        adapter!!.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(isDating: Boolean): ChatListFragment {
            val chatListFragment = ChatListFragment()
            val bundle = Bundle()
            bundle.putBoolean(Constants.EXTRA_IS_DATING, isDating)
            chatListFragment.arguments = bundle
            return chatListFragment
        }
    }
}