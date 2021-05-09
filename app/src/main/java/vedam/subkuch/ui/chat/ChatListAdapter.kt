package vedam.subkuch.ui.chat

import android.content.Context
import java.util.ArrayList
import vedam.subkuch.ui.matrimonial.models.DatingProfile
import vedam.subkuch.interfaces.OnListViewItemClickListener
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import vedam.subkuch.R
import vedam.subkuch.utils.UiUtil
import vedam.subkuch.utils.ImageSetter.ImageBuilder
import vedam.subkuch.utils.AppUtil
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import vedam.subkuch.db.chat.Chat
import vedam.subkuch.helpers.Constants
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.DateTimeUtils

class ChatListAdapter constructor(
    private val context: Context,
    private val datingProfiles: ArrayList<DatingProfile>,
    private val listViewItemClickListener: OnListViewItemClickListener
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_list_chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datingProfile = datingProfiles[position]
        /*chatRepository.getLatestChatMessage(datingProfile.getProfileId()).observe(lifecycleOwner,
                latestChatMessage -> bindValues(latestChatMessage, holder, datingProfile));*/

        if (datingProfile.latestChat == null)
            Firebase.firestore.collection(Constants.TABLE_MESSAGES)
                .whereEqualTo(
                    Constants.idPair,
                    AppPrefs.getPrefsUserId(context).getIdPair(datingProfile.profileId)
                )
                .orderBy(Constants.timeStamp, Query.Direction.DESCENDING).limit(1)
                .get().addOnSuccessListener {
                    if (it != null && it.documents.isNotEmpty()) {
                        val doc = it.documents.first()
                        val chat = Chat()
                        chat.fromProfileId = doc.getString("fromProfileId")
                        chat.toProfileId = doc.getString("toProfileId")
                        chat.senderName = doc.getString("senderName")
                        chat.message = doc.getString("message")
                        chat.timeStamp = doc.getString("timeStamp")
                        chat.isRead = doc.getBoolean(Constants.read) ?: false
                        chat.isStatus = doc.metadata.hasPendingWrites()
                        datingProfile.latestChat = chat
                        bindValues(chat, holder)
                    }
                }
        else
            bindValues(datingProfile.latestChat, holder)

//        new ChatAsyncTask(holder, datingProfile).execute();
//        Observable.fromCallable(() -> chatRepository.getLatestChatMessage(datingProfile.getProfileId()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(latestChatMessage -> bindValues(latestChatMessage, holder, datingProfile), Crashlytics::logException);

        if (datingProfile.imagesList != null && datingProfile.imagesList.isNotEmpty()) UiUtil.setImageView(
            ImageBuilder(
                context
            )
                .setImageLink(datingProfile.imagesList[0].image)
                .setPlaceholderResource(R.drawable.placeholder_small)
                .setErrorResource(R.drawable.placeholder_small)
                .setTarget(holder.ivProfile)
                .build()
        ) else holder.ivProfile.setImageResource(R.drawable.placeholder_small)
        holder.bind(datingProfile, position, listViewItemClickListener)
        UiUtil.setTextView(holder.tvName, AppUtil.deNull(datingProfile.firstName))
    }

    override fun getItemCount(): Int {
        return datingProfiles.size
    }

    private fun String.getIdPair(chatToId: String): String {
        return if (this > chatToId)
            "${this}_$chatToId"
        else
            "${chatToId}_${this}"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)
        fun <E> bind(item: E, position: Int, listener: OnListViewItemClickListener?) {
            itemView.setOnClickListener { v: View? ->
                listener?.onItemClick(
                    item,
                    position,
                    itemView,
                    null
                )
            }
        }

    }

    companion object {
        private fun bindValues(latestChatMessage: Chat?, holder: ViewHolder) {
            if (latestChatMessage != null) {
                UiUtil.setTextView(
                    holder.tvTime,
                    DateTimeUtils.getFormattedDate(
                        latestChatMessage.timeStamp!!.toLong(),
                        DateTimeUtils.DATE_FORMAT_4
                    )
                )
                UiUtil.setTextView(holder.tvMessage, latestChatMessage.message)
            } else {
                holder.tvMessage.visibility = View.GONE
                holder.tvTime.visibility = View.GONE
            }
        }
    }
}