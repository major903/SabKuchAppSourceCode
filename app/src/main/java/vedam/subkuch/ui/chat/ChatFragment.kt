package vedam.subkuch.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import vedam.subkuch.R
import vedam.subkuch.base.BaseFragment
import vedam.subkuch.databinding.FragmentChatBinding
import vedam.subkuch.db.chat.Chat
import vedam.subkuch.db.chat.LatestChat
import vedam.subkuch.helpers.Constants
import vedam.subkuch.utils.AppPrefs

/**
 * A sends to B
 * 1. A sends Message to server with SocketType Msg. Server Sends to A with SocketType Read for app to save UUID
 * 2. Server sends message to B with SocketType Msg.
 * 3. B receives message from server with SocketType Msg. Then sends a message to server with SocketType Ack and status = 2
 * 4. Server sends a message to A with SocketType Ack and Status = 2.
 * 5. A sends a message to server SocketType Ack and Status = 3
 */
class ChatFragment : BaseFragment() {
    private var fragmentChatBinding: FragmentChatBinding? = null
    var chatAdapter: ChatAdapter? = null
    private var senderName: String? = null
    private var chatToId: String? = null
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private var snapshotListener: ListenerRegistration? = null
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            chatToId = requireArguments().getString(Constants.EXTRA_CHAT_TO_ID)
            senderName = requireArguments().getString(Constants.EXTRA_NAME)
            setTitle(senderName)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Let's inflate & return the view
        fragmentChatBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        return fragmentChatBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        bindCallbacks()
        bindData()
        markRead()
    }

    private fun markRead() {

        firestore.collection(Constants.TABLE_MESSAGES)
            .whereEqualTo(Constants.ToProfileId, AppPrefs.getPrefsUserId(mContext))
            .whereEqualTo(Constants.FromProfileId, chatToId)
            .whereEqualTo(Constants.read, false).get().addOnSuccessListener {
                it?.documents?.forEach { doc ->
                    doc.reference.update(Constants.read, true)
                }
            }
    }

    private fun init() {
        chatAdapter = ChatAdapter(requireContext())
        fragmentChatBinding!!.rvChat.adapter = chatAdapter
        fragmentChatBinding!!.rvChat.layoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true)

        chatAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                // scroll to newly added item position
                fragmentChatBinding?.rvChat?.scrollToPosition(positionStart)
            }
        })
    }

    private fun bindCallbacks() {
        fragmentChatBinding!!.sendMessageButton.setOnClickListener { _: View? ->
            if (fragmentChatBinding!!.etMessage.text.toString()
                    .trim { it <= ' ' } == ""
            ) Toast.makeText(
                mContext, "Empty message!",
                Toast.LENGTH_SHORT
            ).show() else {
                val message = fragmentChatBinding!!.etMessage.text.toString().trim { it <= ' ' }
                storeMessage(message)
                fragmentChatBinding!!.etMessage.setText("")
            }
        }
    }

    private fun bindData() {
        snapshotListener = firestore.collection(Constants.TABLE_MESSAGES)
            .whereEqualTo("idPair", AppPrefs.getPrefsUserId(mContext).getIdPair(chatToId!!))
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null || snapshot == null) {
//                    UiUtil.showToast(context, "Error fetching data. Please try again")
                    return@addSnapshotListener
                }
                val list = mutableListOf<Chat>()
                snapshot.documents.let {
                    for (doc in it) {
                        val chat = getChatFromDoc(doc)
                        list.add(chat)
                    }
                }

                chatAdapter?.setChat(list)
                viewModel.setLatestChatMessage(list.lastOrNull())
                markRead()
            }
//        chatRepository!!.getIndividualChat(AppPrefs.getPrefsUserId(context), chatToId)
//            .observe(this, { chats: List<Chat?>? -> chatAdapter!!.setChat(chats) })
    }

    private fun storeMessage(message: String) {
        val chat = Chat()
        chat.fromProfileId = AppPrefs.getPrefsUserId(mContext)
        chat.toProfileId = chatToId
        chat.message = message
        chat.senderName = senderName
        chat.idPair = chat.fromProfileId?.getIdPair(chatToId!!)
        firestore.collection(Constants.TABLE_MESSAGES).add(chat).addOnSuccessListener {

            storeLatestMessage()
        }.addOnFailureListener {

        }
    }

    private fun storeLatestMessage() {


        firestore.collection(Constants.TABLE_MESSAGES)
            .orderBy(Constants.timeStamp, Query.Direction.DESCENDING).limit(1)
            .get().addOnSuccessListener {
                if (it.isEmpty) return@addOnSuccessListener

                val chat = mapLatestChat(it.documents[0])
                val latestChat = LatestChat(
                    latestMessage = chat.message,
                    idPair = chat.idPair,
                    senderName = chat.senderName,
                    fromProfileId = chat.fromProfileId,
                    toProfileId = chat.toProfileId,
                    timeStamp = chat.timeStamp,
                    null
                )
                firestore.collection(Constants.TABLE_LATEST_CHAT).document(chat.idPair!!)
                    .set(latestChat)
            }
    }

    private fun String.getIdPair(chatToId: String): String {
        return if (this > chatToId)
            "${this}_$chatToId"
        else
            "${chatToId}_${this}"
    }

    private fun getChatFromDoc(doc: DocumentSnapshot): Chat {

        val chat = Chat()
        chat.docId = doc.id
        chat.fromProfileId = doc.getString("fromProfileId")
        chat.toProfileId = doc.getString("toProfileId")
        chat.senderName = doc.getString("senderName")
        chat.message = doc.getString("message")
        chat.timeStamp =
            doc.getTimestamp("timeStamp", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        chat.isRead = doc.getBoolean(Constants.read) ?: false
        chat.isStatus = doc.metadata.hasPendingWrites()

        return chat
    }

    private fun mapLatestChat(doc: DocumentSnapshot): Chat {

        val chat = Chat()
        chat.docId = doc.id
        chat.fromProfileId = doc.getString("fromProfileId")
        chat.toProfileId = doc.getString("toProfileId")
        chat.senderName = doc.getString("senderName")
        chat.message = doc.getString("message")
        chat.timeStamp =
            doc.getTimestamp("timeStamp", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
        chat.isRead = doc.getBoolean(Constants.read) ?: false
        chat.isStatus = doc.metadata.hasPendingWrites()
        chat.idPair = doc.getString(Constants.idPair)

        return chat
    }

    override fun onDestroy() {
        super.onDestroy()

        snapshotListener?.remove()
    }

    companion object {
        var TAG = "TAG_CHAT"

        @JvmStatic
        fun newInstance(bundle: Bundle?): ChatFragment {
            val fragment = ChatFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
