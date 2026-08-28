package vedam.subkuch.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import vedam.subkuch.R
import vedam.subkuch.network.models.learn.LearnChapter
import vedam.subkuch.network.models.learn.LearnTopic

class LearnCourseContentAdapter(
    private val onTopicClick: (LearnTopic) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val rows = arrayListOf<Row>()
    private var subscribed = false
    private val completedTopicIds = mutableSetOf<Int>()
    private var activeTopicId: Int? = null

    fun submitChapters(
        chapters: List<LearnChapter>,
        isSubscribed: Boolean,
        completedTopics: Set<Int> = emptySet()
    ) {
        subscribed = isSubscribed
        completedTopicIds.clear()
        completedTopicIds.addAll(completedTopics)
        rows.clear()
        chapters.sortedBy { it.chapterNumber }.forEach { chapter ->
            rows += Row.Chapter(chapter)
            chapter.topics.orEmpty().sortedBy { it.topicNumber }.forEach { topic ->
                rows += Row.Topic(topic)
            }
        }
        notifyDataSetChanged()
    }

    fun markCompleted(topicId: Int) {
        if (!completedTopicIds.add(topicId)) return
        if (activeTopicId == topicId) activeTopicId = null
        val rowIndex = topicPosition(topicId)
        if (rowIndex >= 0) notifyItemChanged(rowIndex)
    }

    fun markPlaying(topicId: Int) {
        if (activeTopicId == topicId) return
        val previousPosition = activeTopicId?.let(::topicPosition) ?: -1
        activeTopicId = topicId
        if (previousPosition >= 0) notifyItemChanged(previousPosition)
        val currentPosition = topicPosition(topicId)
        if (currentPosition >= 0) notifyItemChanged(currentPosition)
    }

    private fun topicPosition(topicId: Int): Int = rows.indexOfFirst {
        it is Row.Topic && it.value.topicId == topicId
    }

    override fun getItemCount(): Int = rows.size

    override fun getItemViewType(position: Int): Int = when (rows[position]) {
        is Row.Chapter -> VIEW_TYPE_CHAPTER
        is Row.Topic -> VIEW_TYPE_TOPIC
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CHAPTER) {
            ChapterViewHolder(inflater.inflate(R.layout.item_learn_chapter, parent, false))
        } else {
            TopicViewHolder(inflater.inflate(R.layout.item_learn_topic, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val row = rows[position]) {
            is Row.Chapter -> (holder as ChapterViewHolder).bind(row.value)
            is Row.Topic -> (holder as TopicViewHolder).bind(
                row.value,
                subscribed,
                row.value.topicId in completedTopicIds,
                row.value.topicId == activeTopicId,
                onTopicClick
            )
        }
    }

    private class ChapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_learn_chapter_title)
        private val meta: TextView = view.findViewById(R.id.tv_learn_chapter_meta)

        fun bind(chapter: LearnChapter) {
            title.text = itemView.context.getString(
                R.string.learn_chapter_number,
                chapter.chapterNumber,
                chapter.chapterName.orEmpty()
            )
            val lectureCount = chapter.topics.orEmpty().size
            meta.text = itemView.resources.getQuantityString(
                R.plurals.learn_lecture_count,
                lectureCount,
                lectureCount
            )
        }
    }

    private class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_learn_topic_title)
        private val duration: TextView = view.findViewById(R.id.tv_learn_topic_duration)
        private val state: TextView = view.findViewById(R.id.tv_learn_topic_state)

        fun bind(
            topic: LearnTopic,
            subscribed: Boolean,
            completed: Boolean,
            playing: Boolean,
            onClick: (LearnTopic) -> Unit
        ) {
            title.text = topic.topicName
            duration.text = topic.topicDuration.orEmpty()
            val playable = !topic.isLocked && !topic.videoEmbedUrl.isNullOrBlank()
            state.setText(
                when {
                    topic.isLocked -> R.string.learn_locked
                    playing -> R.string.learn_playing
                    completed -> R.string.learn_completed
                    subscribed -> R.string.learn_watch
                    playable -> R.string.learn_preview
                    else -> R.string.learn_unavailable
                }
            )
            state.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    when {
                        playing -> R.color.learn_playing
                        playable -> R.color.brand_blue
                        else -> R.color.form_text_secondary
                    }
                )
            )
            itemView.isEnabled = playable
            itemView.alpha = if (playable) 1f else 0.7f
            itemView.setOnClickListener { if (playable) onClick(topic) }
        }
    }

    private sealed class Row {
        data class Chapter(val value: LearnChapter) : Row()
        data class Topic(val value: LearnTopic) : Row()
    }

    companion object {
        private const val VIEW_TYPE_CHAPTER = 0
        private const val VIEW_TYPE_TOPIC = 1
    }
}
