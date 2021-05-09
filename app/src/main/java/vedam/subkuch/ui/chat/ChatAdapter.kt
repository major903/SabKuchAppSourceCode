package vedam.subkuch.ui.chat

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import vedam.subkuch.R
import vedam.subkuch.db.chat.Chat
import vedam.subkuch.utils.AppPrefs
import vedam.subkuch.utils.DateTimeUtils
import vedam.subkuch.utils.UiUtil
import java.util.*

/**
 * Created by nadeemansari on 01/04/16.
 */
class ChatAdapter constructor(private val context: Context) :
    androidx.recyclerview.widget.ListAdapter<Chat, ChatAdapter.ViewHolder>(ChatDiffCallback()) {
    //    private var chats: List<Chat> = ArrayList()
    var myId: String = AppPrefs.getPrefsUserId(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, fromId, _, message, timeStamp, _, status, isRead) = getItem(position)

//        if (status == Constants.CHAT_STATUS_DELIVERED)
//            holder.ivSent.setImageResource(R.drawable.ic_done_all_black_18dp);
//        else if (status == Constants.CHAT_STATUS_SENT_BUT_NOT_DELIVERED) {
//            holder.ivSent.setImageResource(R.drawable.ic_done_black_18dp);
//        } else if (status == Constants.CHAT_STATUS_NOT_SENT) {
//            holder.ivSent.setImageResource(R.drawable.baseline_schedule_black_18);
//        } else {
//            holder.ivSent.setImageResource(R.drawable.ic_done_all_black_18dp);
//        }
        if (status) {
            holder.ivSent.setImageResource(R.drawable.ic_done_black_18dp)
            ImageViewCompat.setImageTintList(
                holder.ivSent, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.black)
                )
            )
        } else {
            holder.ivSent.setImageResource(R.drawable.ic_done_all_black_18dp)
            if (isRead) ImageViewCompat.setImageTintList(
                holder.ivSent, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.blue_tick)
                )
            ) else ImageViewCompat.setImageTintList(
                holder.ivSent, ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.black)
                )
            )
        }
        // else
        // img_sent.setVisibility(View.GONE);
        if (fromId == myId) {
            holder.ivSent.visibility = View.VISIBLE
            holder.llAlignment.gravity = Gravity.END
            holder.llAlignment.setBackgroundResource(R.drawable.white_absolute)
            holder.llLayout.gravity = Gravity.END
            holder.llRow.gravity = Gravity.END
        } else {
            holder.ivSent.visibility = View.GONE
            holder.llAlignment.gravity = Gravity.START
            holder.llAlignment.setBackgroundResource(R.drawable.white_layer)
            holder.llLayout.gravity = Gravity.START
            holder.llRow.gravity = Gravity.START
        }
        UiUtil.setTextView(holder.tvMessage, message)
        try {
            holder.tvMessageDate.visibility = View.VISIBLE
            UiUtil.setTextView(
                holder.tvMessageDate,
                DateTimeUtils.getFormattedDate(timeStamp!!.toDate().time, DateTimeUtils.DATE_FORMAT_4)
            )
        } catch (e: NumberFormatException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            holder.tvMessageDate.visibility = View.INVISIBLE
        }
    }

//    override fun getItemCount(): Int {
//        return chats.size
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.message_text)
        val tvMessageDate: TextView = itemView.findViewById(R.id.message_text_date)
        var llRow: LinearLayout = itemView.findViewById(R.id.message_row_layout)
        var llLayout: LinearLayout = itemView.findViewById(R.id.ll_container)
        var llAlignment: LinearLayout = itemView.findViewById(R.id.ll_alignment)
        val ivSent: ImageView = itemView.findViewById(R.id.chat_row_sent)

    }

    fun setChat(chats: List<Chat>) {
        submitList(chats)
//        notifyDataSetChanged()
    }

    private class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.docId == newItem.docId
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }
}