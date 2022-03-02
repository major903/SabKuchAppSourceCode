package vedam.subkuch.ui.jobs

import android.content.Context
import vedam.subkuch.interfaces.OnListViewItemClickListener
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import vedam.subkuch.R
import vedam.subkuch.utils.UiUtil
import android.text.TextUtils
import vedam.subkuch.utils.AppUtil
import android.widget.LinearLayout
import vedam.subkuch.ui.jobs.models.Post
import android.text.SpannableStringBuilder
import android.text.style.MetricAffectingSpan
import vedam.subkuch.uicomponent.CustomTypefaceSpan
import android.graphics.Typeface
import android.text.Spannable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import vedam.subkuch.ui.jobs.models.Job
import vedam.subkuch.utils.ListItemClickAction
import java.util.ArrayList

class JobsAdapter constructor(
    private val context: Context,
    private val jobs: ArrayList<Job>,
    private val listener: OnListViewItemClickListener?
) : RecyclerView.Adapter<JobsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.fragment_jobs_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]
        holder.tvOrganisation.text = job.organisationName
        UiUtil.setTextView(job.distance, "away", holder.tvDistance)
        UiUtil.setTextView("Dealing in : ", job.dealingIn, holder.tvDealsIn)
        UiUtil.setTextView(
            "Job Location : ",
            String.format("%s", job.jobLocation),
            holder.tvLocation
        )
        UiUtil.setTextView(holder.tvContact, job.howToContact)
        if (job.posts != null && !job.posts.isEmpty()) setPosition(
            holder.llPosition,
            job.posts
        ) else holder.llPosition.visibility = View.GONE
        if (!TextUtils.isEmpty(job.latitude) && !TextUtils.isEmpty(job.longitude)) {
            holder.ibDirection.visibility = View.VISIBLE
            holder.ibDirection.setOnClickListener { view: View? ->
                val webURL =
                    "https://www.google.com/maps/dir/?api=1&" + "destination=" + job.latitude + "%2C" + job.longitude
                AppUtil.openUrl(context, webURL)
            }
        } else holder.ibDirection.visibility = View.GONE
        holder.ibShare.setOnClickListener { view: View? ->
            listener?.onItemClick(
                job,
                position,
                null,
                null
            )
        }
    }

    private fun setPosition(llPosition: LinearLayout, posts: ArrayList<Post>) {
        llPosition.visibility = View.VISIBLE
        llPosition.removeAllViews()
        for (i in posts.indices) {
            val post = posts[i]
            var layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val tv = TextView(llPosition.context)
            if (i > 0) {
                layoutParams.setMargins(0, AppUtil.dpToPx(llPosition.context, 16), 0, 0)
            }
            tv.layoutParams = layoutParams
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.text = "${i + 1}. ${AppUtil.deNull(post.jobTitle)}"
            llPosition.addView(tv)

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val tvPosition = TextView(llPosition.context)
            tvPosition.layoutParams = layoutParams
            tvPosition.text = "${post.requirement}"
            llPosition.addView(tvPosition)

            if (post.apply) {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val button = Button(llPosition.context)
                layoutParams.setMargins(0, AppUtil.dpToPx(llPosition.context, 8), 0, 0)
                button.layoutParams = layoutParams
                button.setText(R.string.apply)
                button.setBackgroundColor(
                    ContextCompat.getColor(
                        llPosition.context,
                        R.color.colorPrimary
                    )
                )
                button.setTextColor(ContextCompat.getColor(llPosition.context, R.color.white))
                button.isAllCaps = false
                button.setOnClickListener {
                    listener?.onItemClick(
                        post,
                        0,
                        null,
                        ListItemClickAction.SELECT_POST
                    )
                }
                llPosition.addView(button)
            }
        }
        //        StringBuilder fullString = new StringBuilder("");
//        val fullJobPost = getJobPost(posts)

//        llPosition.setText(fullJobPost);
    }

    private fun getJobPost(posts: ArrayList<Post>): CharSequence {
        val fullString = SpannableStringBuilder("")
        for (i in posts.indices) {
            val post = posts[i]
            val boldSpan: MetricAffectingSpan = CustomTypefaceSpan(Typeface.DEFAULT_BOLD)
            val currentLength = fullString.length
            fullString.append((i + 1).toString()).append(". ")
                .append(AppUtil.deNull(post.jobTitle))
            fullString.setSpan(
                boldSpan,
                currentLength,
                currentLength + post.jobTitle.length + 3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            if (!TextUtils.isEmpty(post.requirement)) fullString.append("\n")
                .append(post.requirement)
            if (i != posts.size - 1) fullString.append("\n\n")
        }
        return fullString
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrganisation: TextView = itemView.findViewById(R.id.tv_organisation)
        val tvDealsIn: TextView = itemView.findViewById(R.id.tv_deals)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_address)
        val llPosition: LinearLayout = itemView.findViewById(R.id.ll_position)
        val tvContact: TextView = itemView.findViewById(R.id.tv_contact)
        val ibDirection: ImageView = itemView.findViewById(R.id.ib_direction)
        val ibShare: ImageView = itemView.findViewById(R.id.ib_share)

    }
}