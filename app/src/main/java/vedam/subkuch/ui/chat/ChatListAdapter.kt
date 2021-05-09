package vedam.subkuch.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.R
import vedam.subkuch.db.chat.LatestChat
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.ui.matrimonial.models.DatingProfile
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.DateTimeUtils
import vedam.subkuch.utils.ImageSetter.ImageBuilder
import vedam.subkuch.utils.UiUtil

class ChatListAdapter constructor(
    private val context: Context,
    private val listViewItemClickListener: OnListViewItemClickListener
) : ListAdapter<DatingProfile, ChatListAdapter.ViewHolder>(ChatListDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_list_chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datingProfile = getItem(position)

        bindValues(datingProfile.latestChat, holder)

        if (datingProfile.ImagesList != null && datingProfile.ImagesList.isNotEmpty()) UiUtil.setImageView(
            ImageBuilder(
                context
            )
                .setImageLink(datingProfile.ImagesList[0].image)
                .setPlaceholderResource(R.drawable.placeholder_small)
                .setErrorResource(R.drawable.placeholder_small)
                .setTarget(holder.ivProfile)
                .build()
        ) else holder.ivProfile.setImageResource(R.drawable.placeholder_small)
        holder.bind(datingProfile, position, listViewItemClickListener)
        UiUtil.setTextView(holder.tvName, AppUtil.deNull(datingProfile.FirstName))

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)

        fun <E> bind(item: E, position: Int, listener: OnListViewItemClickListener?) {
            itemView.setOnClickListener {
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
        private fun bindValues(latestChatMessage: LatestChat?, holder: ViewHolder) {
            if (latestChatMessage != null) {
                UiUtil.setTextView(
                    holder.tvTime,
                    DateTimeUtils.getFormattedDate(
                        latestChatMessage.timeStamp!!.toDate().time,
                        DateTimeUtils.DATE_FORMAT_4
                    )
                )
                UiUtil.setTextView(holder.tvMessage, latestChatMessage.latestMessage)
            } else {
                holder.tvMessage.visibility = View.GONE
                holder.tvTime.visibility = View.GONE
            }
        }
    }

    private class ChatListDiffCallback : DiffUtil.ItemCallback<DatingProfile>() {

        override fun areItemsTheSame(oldItem: DatingProfile, newItem: DatingProfile): Boolean {
            return oldItem.ProfileId == newItem.ProfileId
        }

        override fun areContentsTheSame(oldItem: DatingProfile, newItem: DatingProfile): Boolean {
            return oldItem == newItem
        }
    }
}