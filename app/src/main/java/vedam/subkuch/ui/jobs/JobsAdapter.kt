package vedam.subkuch.ui.jobs

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.MetricAffectingSpan
import android.view.LayoutInflater
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.marsad.stylishdialogs.StylishAlertDialog
import vedam.subkuch.R
import vedam.subkuch.interfaces.OnListViewItemClickListener
import java.util.Locale
import vedam.subkuch.ui.jobs.models.Job
import vedam.subkuch.ui.jobs.models.Post
import vedam.subkuch.uicomponent.CustomTypefaceSpan
import vedam.subkuch.utils.AppUtil
import vedam.subkuch.utils.ListItemClickAction
import vedam.subkuch.utils.UiUtil
import java.util.*

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
        val distance = resolveDistance(job)?.let(::formatApiDistance)
        holder.tvDistance.visibility = if (distance == null) View.GONE else View.VISIBLE
        holder.tvDistance.text = distance.orEmpty()
        UiUtil.setTextView("Dealing in : ", job.dealingIn, holder.tvDealsIn)
        UiUtil.setTextView(
            "Job Location : ",
            String.format("%s", job.jobLocation),
            holder.tvLocation
        )
        bindContactActions(holder, job)
        if (job.posts != null && job.posts.isNotEmpty()) setPosition(
            holder.llPosition,
            job.posts
        ) else holder.llPosition.visibility = View.GONE
        holder.ibDirection.visibility = View.GONE
        holder.ibShare.setOnClickListener { view: View? ->
            listener?.onItemClick(
                job,
                position,
                null,
                null
            )
        }
    }

    private fun bindContactActions(holder: ViewHolder, job: Job) {
        holder.llContactActions.removeAllViews()
        val contact = resolveContactActions(job)
        val hasContactActions =
            (contact.numbers.isNotEmpty() && (contact.isCall || contact.isWhatsApp)) ||
                !contact.email.isNullOrBlank()
        holder.tvContact.visibility = if (hasContactActions) View.GONE else View.VISIBLE
        if (!hasContactActions) {
            UiUtil.setTextView(holder.tvContact, job.howToContact)
            holder.llContactActions.visibility = View.GONE
            return
        }

        holder.llContactActions.visibility = View.VISIBLE
        contact.numbers.forEach { number ->
            if (contact.isCall) addActionButton(holder.llContactActions, "Call") {
                val dialNumber = number.filter { it.isDigit() || it == '+' }
                context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$dialNumber".toUri()))
            }
            if (contact.isWhatsApp) addActionButton(holder.llContactActions, "WhatsApp") {
                val whatsappNumber = number.filter { it.isDigit() }
                context.startActivity(Intent(Intent.ACTION_VIEW, "https://wa.me/$whatsappNumber".toUri()))
            }
        }
        contact.email?.let { email ->
            addActionButton(holder.llContactActions, "Email") {
                AppUtil.startEmailIntent(context, email, "Job application")
            }
        }
    }

    private fun formatDistance(distanceKm: Double): String = when {
        distanceKm < 1 -> "${(distanceKm * METERS_PER_KILOMETRE).toInt()} m away"
        else -> String.format(Locale.US, "%.1f km away", distanceKm)
    }

    private fun resolveDistance(job: Job): String? = sequenceOf(job.distance)
        .plus(job.posts.orEmpty().asSequence().map { it.distance })
        .mapNotNull { it?.trim()?.takeIf(String::isNotEmpty) }
        .firstOrNull()

    /**
     * The Jobs query supplies this value for the signed-in user. Retain its text when
     * the backend returns a formatted value, while giving numeric values the app's
     * usual unit label.
     */
    private fun formatApiDistance(distance: String): String =
        distance.toDoubleOrNull()?.let(::formatDistance) ?: distance

    /**
     * New API responses expose contact details as separate fields. Older versions of
     * the jobs API return only HowToContact, so parse the readable fallback generated
     * while posting an ad until all servers return the structured fields.
     */
    private fun resolveContactActions(job: Job): ContactActions {
        val structuredNumbers = listOf(job.mobile1, job.mobile2)
            .filterNotNull()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
        val structuredEmail = job.email?.trim()?.takeIf { it.isNotEmpty() }

        if (structuredNumbers.isNotEmpty() || structuredEmail != null) {
            return ContactActions(
                numbers = structuredNumbers,
                email = structuredEmail,
                isCall = job.isCall == true,
                isWhatsApp = job.isWhatsApp == true
            )
        }

        val fallback = job.howToContact.orEmpty()
        val fallbackNumbers = PHONE_PATTERN.findAll(fallback)
            .map { match -> match.value.trim() }
            .filter { number -> number.count(Char::isDigit) >= MIN_PHONE_DIGITS }
            .distinct()
            .toList()
        val fallbackEmail = EMAIL_PATTERN.find(fallback)?.value

        return ContactActions(
            numbers = fallbackNumbers,
            email = fallbackEmail,
            isCall = CALL_PATTERN.containsMatchIn(fallback),
            isWhatsApp = WHATSAPP_PATTERN.containsMatchIn(fallback)
        )
    }

    private fun addActionButton(
        container: LinearLayout,
        label: String,
        isAvailable: Boolean = true,
        unavailableMessage: String? = null,
        onClick: () -> Unit
    ) {
        val buttonSize = AppUtil.dpToPx(container.context, ACTION_BUTTON_SIZE_DP)
        val buttonPadding = AppUtil.dpToPx(container.context, ACTION_ICON_PADDING_DP)
        val button = ImageButton(container.context).apply {
            contentDescription = if (isAvailable) label else "$label. $unavailableMessage"
            setImageResource(actionIcon(label))
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding)
            background = actionButtonBackground(label, isAvailable)
            setOnClickListener {
                if (isAvailable) {
                    onClick()
                } else {
                    showCallUnavailableDialog(unavailableMessage)
                }
            }
        }
        val params = LinearLayout.LayoutParams(buttonSize, buttonSize)
        params.setMargins(0, 0, AppUtil.dpToPx(container.context, ACTION_BUTTON_SPACING_DP), 0)
        container.addView(button, params)
    }

    private fun actionButtonBackground(label: String, isAvailable: Boolean): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = AppUtil.dpToPx(context, ACTION_BUTTON_CORNER_DP).toFloat()
            if (!isAvailable) {
                setColor(CALL_UNAVAILABLE_GREY)
            } else if (label == context.getString(R.string.whatsapp)) {
                setColor(WHATSAPP_GREEN)
            } else {
                setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        }

    private fun actionIcon(label: String): Int = when (label) {
        context.getString(R.string.call) -> R.drawable.ic_job_call
        context.getString(R.string.whatsapp) -> R.drawable.ic_job_whatsapp
        context.getString(R.string.email) -> R.drawable.ic_job_email
        else -> 0
    }

    private fun showCallUnavailableDialog(message: String?) {
        val dialog = StylishAlertDialog(context, StylishAlertDialog.WARNING)
            .setTitleText("Sorry, boss is busy")
            .setContentText(message ?: "Please try again later.")
            .setContentTextSize(14)
            .setConfirmText("OK")
            .setConfirmButtonBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setConfirmButtonTextColor(ContextCompat.getColor(context, R.color.white))
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
        dialog.setOnShowListener {
            val buttonRow = dialog.getButton(StylishAlertDialog.BUTTON_CONFIRM).parent as? View
            buttonRow?.setPadding(
                buttonRow.paddingLeft,
                AppUtil.dpToPx(context, 16),
                buttonRow.paddingRight,
                buttonRow.paddingBottom
            )
        }
        dialog.show()
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
            tv.text = AppUtil.deNull(post.jobTitle)
            llPosition.addView(tv)

            val requirements = listOf(post.req1, post.req2, post.req3)
                .mapNotNull { it?.trim()?.takeIf(String::isNotEmpty) }
                .ifEmpty { listOfNotNull(post.requirement?.trim()?.takeIf(String::isNotEmpty)) }
            requirements.forEach { requirement ->
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val tvRequirement = TextView(llPosition.context)
                tvRequirement.layoutParams = layoutParams
                tvRequirement.text = "• $requirement"
                llPosition.addView(tvRequirement)
            }

            if (post.apply == true) {
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
                button.setOnClickListener { clickedButton ->
                    listener?.onItemClick(
                        post,
                        0,
                        clickedButton,
                        ListItemClickAction.SELECT_POST
                    )
                }
                llPosition.addView(button)
            } else {
                addPostContactActions(llPosition, post)
            }
        }
        //        StringBuilder fullString = new StringBuilder("");
//        val fullJobPost = getJobPost(posts)

//        llPosition.setText(fullJobPost);
    }

    private fun addPostContactActions(container: LinearLayout, post: Post) {
        val actions = mutableListOf<ContactAction>()
        listOf(
            TimedCall(post.call1, post.time1, post.time2),
            TimedCall(post.call2, post.time3, post.time4)
        )
            .filter { usableContactValue(it.number) != null }
            .distinctBy { usableContactValue(it.number) }
            .forEach { call ->
                val number = usableContactValue(call.number) ?: return@forEach
                val isAvailable = isCallAvailable(call.startTime, call.endTime)
                val unavailableMessage = if (isAvailable) {
                    null
                } else {
                    context.getString(
                        R.string.boss_busy_call_time,
                        displayTime(call.startTime),
                        displayTime(call.endTime)
                    )
                }
                actions.add(ContactAction(
                    label = context.getString(R.string.call),
                    isAvailable = isAvailable,
                    unavailableMessage = unavailableMessage
                ) {
                    val dialNumber = number.filter { it.isDigit() || it == '+' }
                    context.startActivity(Intent(Intent.ACTION_DIAL, "tel:$dialNumber".toUri()))
                })
            }

        usableContactValue(post.whatsApp)?.let { number ->
            actions.add(ContactAction(label = context.getString(R.string.whatsapp)) {
                val whatsappNumber = number.filter(Char::isDigit)
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, "https://wa.me/$whatsappNumber".toUri())
                )
            })
        }

        usableContactValue(post.email1)?.let { email ->
            actions.add(ContactAction(label = context.getString(R.string.email)) {
                AppUtil.startEmailIntent(context, email, "Job application")
            })
        }

        if (actions.isNotEmpty()) {
            val row = LinearLayout(container.context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.START
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also {
                    it.setMargins(0, AppUtil.dpToPx(container.context, 16), 0, 0)
                }
            }
            actions.forEach { action ->
                addActionButton(
                    row,
                    action.label,
                    action.isAvailable,
                    action.unavailableMessage,
                    action.onClick
                )
            }
            container.addView(row)
        }
    }

    private fun isCallAvailable(startValue: String?, endValue: String?): Boolean {
        if (isAllDayTime(startValue) || isAllDayTime(endValue)) return true
        val start = parseMinutes(startValue) ?: return true
        val end = parseMinutes(endValue) ?: return true
        if (start == end) return true

        val now = Calendar.getInstance()
        val current = now.get(Calendar.HOUR_OF_DAY) * MINUTES_PER_HOUR + now.get(Calendar.MINUTE)
        return if (start < end) {
            current in start..end
        } else {
            current >= start || current <= end
        }
    }

    private fun isAllDayTime(value: String?): Boolean {
        val normalized = value.orEmpty().trim().lowercase(Locale.US)
        return normalized.contains("24 hr") || normalized == "24h" ||
            normalized.contains("all day") || normalized.contains("any time")
    }

    private fun parseMinutes(value: String?): Int? {
        val match = TIME_PATTERN.matchEntire(value.orEmpty().trim()) ?: return null
        var hour = match.groupValues[1].toIntOrNull() ?: return null
        val minute = match.groupValues[2].toIntOrNull() ?: 0
        val meridiem = match.groupValues[3].lowercase(Locale.US)
        if (minute !in 0..59) return null

        if (meridiem.isNotEmpty()) {
            if (hour !in 1..12) return null
            hour %= 12
            if (meridiem == "pm") hour += 12
        } else if (hour !in 0..23) {
            return null
        }
        return hour * MINUTES_PER_HOUR + minute
    }

    private fun displayTime(value: String?): String =
        value.orEmpty().trim().lowercase(Locale.US)

    private fun usableContactValue(value: String?): String? =
        value?.trim()?.takeIf { it.isNotEmpty() && it != "0" }

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

    private data class ContactActions(
        val numbers: List<String>,
        val email: String?,
        val isCall: Boolean,
        val isWhatsApp: Boolean
    )

    private data class ContactAction(
        val label: String,
        val isAvailable: Boolean = true,
        val unavailableMessage: String? = null,
        val onClick: () -> Unit
    )

    private data class TimedCall(
        val number: String?,
        val startTime: String?,
        val endTime: String?
    )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrganisation: TextView = itemView.findViewById(R.id.tv_organisation)
        val tvDealsIn: TextView = itemView.findViewById(R.id.tv_deals)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_address)
        val llPosition: LinearLayout = itemView.findViewById(R.id.ll_position)
        val tvContact: TextView = itemView.findViewById(R.id.tv_contact)
        val llContactActions: LinearLayout = itemView.findViewById(R.id.ll_contact_actions)
        val ibDirection: ImageView = itemView.findViewById(R.id.ib_direction)
        val ibShare: ImageView = itemView.findViewById(R.id.ib_share)

    }

    companion object {
        private const val MIN_PHONE_DIGITS = 7
        private const val METERS_PER_KILOMETRE = 1_000.0
        private const val ACTION_BUTTON_SIZE_DP = 48
        private const val ACTION_BUTTON_SPACING_DP = 10
        private const val ACTION_BUTTON_CORNER_DP = 14
        private const val ACTION_ICON_PADDING_DP = 12
        private const val MINUTES_PER_HOUR = 60
        private const val WHATSAPP_GREEN = 0xFF25D366.toInt()
        private const val CALL_UNAVAILABLE_GREY = 0xFF9E9E9E.toInt()
        private val PHONE_PATTERN = Regex("""(?<!\d)\+?\d[\d\s()/-]*\d(?!\d)""")
        private val EMAIL_PATTERN =
            Regex("""[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}""", RegexOption.IGNORE_CASE)
        private val CALL_PATTERN = Regex("""\bcall\b""", RegexOption.IGNORE_CASE)
        private val WHATSAPP_PATTERN = Regex("""\bwhats\s*app\b""", RegexOption.IGNORE_CASE)
        private val TIME_PATTERN =
            Regex("""^(\d{1,2})(?:[:.](\d{1,2}))?\s*(am|pm)?$""", RegexOption.IGNORE_CASE)
    }
}
