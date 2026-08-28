package vedam.subkuch.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import vedam.subkuch.network.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentChatListBinding
import vedam.subkuch.db.chat.LatestChat
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.network.DataFetcher
import vedam.subkuch.ui.matrimonial.models.DatingProfile
import vedam.subkuch.ui.matrimonial.models.DatingProfileResponse
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil


class ChatListFragment : BaseFragment(), OnListViewItemClickListener {
    private var fragmentChatListBinding: FragmentChatListBinding? = null
    private var adapter: ChatListAdapter? = null
    private var datingProfiles: List<DatingProfile>? = null
    private var loading = true

    //    private var pageNo = 1
    private val pageSize = 200
    private var hasMoreProjects = true
    private var isDating = false
    private val latestChatMap = mutableMapOf<String, LatestChat>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(getString(R.string.matches))
        initUI()
    }

    override fun onResume() {
        super.onResume()
        getMatchedProfiles()
    }

    private fun getLatestChats() {

        val task1 = FirebaseFirestore.getInstance().collection(Constants.TABLE_LATEST_CHAT)
            .whereEqualTo(Constants.ToProfileId, AppPrefs.getPrefsUserId(mContext)).get()

        val task2 = FirebaseFirestore.getInstance().collection(Constants.TABLE_LATEST_CHAT)
            .whereEqualTo(Constants.FromProfileId, AppPrefs.getPrefsUserId(mContext)).get()

        Tasks.whenAllSuccess<QuerySnapshot>(task1, task2).addOnSuccessListener {
            for (snapshot in it)
                for (doc in snapshot.documents) {
                    val chat = getLatestChatFromDoc(doc)
                    latestChatMap[chat.idPair!!] = getLatestChatFromDoc(doc)
                }
            filterChatList()
        }.addOnFailureListener {
            UiUtil.cancelProgressDialog()
        }
    }

    private fun filterChatList() {

        val myId = AppPrefs.getPrefsUserId(mContext)
        datingProfiles?.let { list ->
            for (profile in list) {
                val idPair = myId.getIdPair(profile.ProfileId!!)
                val latestChat = latestChatMap[idPair]
                latestChat?.let { profile.latestChat = it }
            }
            val chatList = list.filter { it.latestChat != null }
                .sortedByDescending { it.latestChat?.timeStamp }
            if (chatList.isNotEmpty())
                adapter?.submitList(chatList)
            else
                Toast.makeText(
                    mContext,
                    "No Chats. Go to your Matches to start a Chat.",
                    Toast.LENGTH_LONG
                ).show()
            UiUtil.cancelProgressDialog()
        } ?: kotlin.run {
            UiUtil.cancelProgressDialog()
        }
    }

    private fun getLatestChatFromDoc(doc: DocumentSnapshot): LatestChat {

        val latestChat = LatestChat()
        latestChat.id = doc.id
        latestChat.fromProfileId = doc.getString("fromProfileId")
        latestChat.toProfileId = doc.getString("toProfileId")
        latestChat.senderName = doc.getString("senderName")
        latestChat.latestMessage = doc.getString("latestMessage")
        latestChat.timeStamp =
            doc.getTimestamp("timeStamp")
        latestChat.idPair = doc.getString(Constants.idPair)

        return latestChat
    }

    private fun initUI() {
        fragmentChatListBinding!!.rvChatList.layoutManager = LinearLayoutManager(mContext)
        adapter = ChatListAdapter(requireContext(), this)
        fragmentChatListBinding!!.rvChatList.adapter = adapter

    }

    private fun String.getIdPair(chatToId: String): String {
        return if (this > chatToId)
            "${this}_$chatToId"
        else
            "${chatToId}_${this}"
    }

    private fun getMatchedProfiles() {
        UiUtil.showProgressDialog(mContext, getString(R.string.please_wait))
        if (isDating) DataFetcher.getDatingMatchedChatProfiles(
            mContext,
            onMatchedProfilesSuccessListener,
            DatingProfileResponse::class.java,
            onErrorListener,
            1,
            pageSize
        ) else DataFetcher.getMatrimonialMatchedChatProfiles(
            mContext,
            onMatchedProfilesSuccessListener,
            DatingProfileResponse::class.java,
            onErrorListener,
            1,
            pageSize
        )
    }

    private fun startChatActivity(datingProfile: DatingProfile) {
        val intent = Intent(mContext, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.FirstName))
        intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.ProfileId)
        intent.putExtra(Constants.EXTRA_IS_DATING, isDating)
        startActivity(intent)
    }

    private val onMatchedProfilesSuccessListener =
        Response.Listener { response: DatingProfileResponse? ->
            if (activity != null) if (response != null && response.returnMessage == Constants.SUCCESS) {
                if (response.returnData.size > 0) {
//                    hasMoreProjects = response.returnData.size >= pageSize
//                    loading = true
                    datingProfiles = response.returnData
                    getLatestChats()
                } else {
                    UiUtil.cancelProgressDialog()
                    UiUtil.showToast(mContext, getString(R.string.no_matches_found))
                }
            } else {
                UiUtil.cancelProgressDialog()
                UiUtil.showToast(mContext, getString(R.string.err_occurred))
            }
        }

    override fun <E> onItemClick(
        item: E?,
        position: Int,
        view: View?,
        action: ListItemClickAction?
    ) {
        if (item != null) {
            val datingProfile = item as DatingProfile
            startChatActivity(datingProfile)
        }
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
