package vedam.subkuch.ui.matrimonial

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.R
import vedam.subkuch.helpers.Constants
import vedam.subkuch.interfaces.OnListViewItemClickListener
import vedam.subkuch.ui.chat.ChatActivity
import vedam.subkuch.ui.matrimonial.models.DatingProfile
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.ImageSetter.ImageBuilder
import vedam.subkuch.utils.UiUtil
import java.util.*

class MatchedProfileAdapter internal constructor(
    private val context: Context,
    private val datingProfiles: ArrayList<DatingProfile>,
    private val listViewItemClickListener: OnListViewItemClickListener
) : RecyclerView.Adapter<MatchedProfileAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view =
            layoutInflater.inflate(R.layout.fragment_matched_profile_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datingProfile = datingProfiles[position]
        UiUtil.setTextView(holder.tvName, AppUtil.deNull(datingProfile.FirstName))
        if (datingProfile.ImagesList != null && datingProfile.ImagesList.size > 0) UiUtil.setImageView(
            ImageBuilder(
                context
            )
                .setImageLink(datingProfile.ImagesList[0].image)
                .setPlaceholderResource(R.drawable.placeholder_small)
                .setErrorResource(R.drawable.placeholder_small)
                .setTarget(holder.ivProfile)
                .build()
        ) else holder.ivProfile.setImageResource(R.drawable.placeholder_small)

        holder.ivChat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(Constants.EXTRA_NAME, AppUtil.deNull(datingProfile.FirstName))
            intent.putExtra(Constants.EXTRA_CHAT_TO_ID, datingProfile.ProfileId)
            context.startActivity(intent)
        }
        holder.bind(datingProfile, position, listViewItemClickListener)
    }

    override fun getItemCount(): Int {
        return datingProfiles.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProfile: ImageView = itemView.findViewById(R.id.iv_profile)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val ivChat: ImageView = itemView.findViewById(R.id.iv_chat)
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
}