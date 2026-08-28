package vedam.subkuch.ui.events

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import vedam.subkuch.R
import vedam.subkuch.base.BaseActivity
import vedam.subkuch.databinding.ActivityLearnCourseDetailsBinding
import vedam.subkuch.network.models.learn.LearnCourseDetailsData
import vedam.subkuch.network.models.learn.LearnTopic
import vedam.subkuch.utils.AppPrefs
import java.util.Locale

@Suppress("TooManyFunctions")
class LearnCourseDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityLearnCourseDetailsBinding
    private lateinit var repository: LearnRepository
    private lateinit var contentAdapter: LearnCourseContentAdapter
    private var details: LearnCourseDetailsData? = null
    private var courseId = 0
    private var activeTopic: LearnTopic? = null
    private var completionPollJob: Job? = null
    private var hasAutoStarted = false
    private var waitingForInitialPlayerReveal = false
    private var fullscreenContainer: FrameLayout? = null
    private var fullscreenCallback: WebChromeClient.CustomViewCallback? = null
    private lateinit var fullscreenBackCallback: OnBackPressedCallback
    private val completedTopicIds = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarBackButton()
        setTitle(R.string.learn_course_details)

        courseId = intent.getIntExtra(EXTRA_COURSE_ID, 0)
        if (courseId <= 0) {
            finish()
            return
        }

        repository = LearnRepository(applicationContext)
        contentAdapter = LearnCourseContentAdapter(::openTopic)
        binding.rvLearnCourseContent.apply {
            layoutManager = LinearLayoutManager(this@LearnCourseDetailsActivity)
            adapter = contentAdapter
            isNestedScrollingEnabled = false
        }
        binding.btnLearnRetry.setOnClickListener { loadCourse(courseId) }
        binding.btnLearnCourseAction.setOnClickListener { handleCourseAction() }
        fullscreenBackCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() = closeFullscreen(notifyPlayer = true)
        }
        onBackPressedDispatcher.addCallback(this, fullscreenBackCallback)
        configureInlinePlayer()
        loadCourse(courseId)
    }

    private fun loadCourse(courseId: Int) {
        showLoading(true)
        lifecycleScope.launch {
            val result = repository.getCourseDetails(courseId)
            showLoading(false)
            if (result?.course == null) {
                showError()
            } else {
                details = result
                bindDetails(result)
            }
        }
    }

    private fun bindDetails(data: LearnCourseDetailsData) {
        val course = data.course
        binding.layoutLearnPlayerHeader.isVisible = true
        binding.scrollLearnCourse.isVisible = true
        binding.layoutLearnCourseError.isVisible = false
        binding.tvLearnDetailsName.text = course.name
        binding.tvLearnDetailsDescription.text = course.description
        binding.tvLearnDetailsTrainer.text = getString(
            R.string.learn_by_trainer,
            course.trainerName.orEmpty()
        )
        binding.tvLearnDetailsRating.text = getString(
            R.string.learn_rating_reviews,
            course.rating,
            course.reviewCount
        )
        // Purchased courses: no price, no MRP, no action button — lessons auto-play and unlock directly.
        binding.tvLearnDetailsPrice.isVisible = !course.isSubscribed
        binding.tvLearnDetailsPrice.text = formatPrice(course.price)
        binding.tvLearnDetailsMrp.apply {
            isVisible = !course.isSubscribed && course.mrp > course.price
            text = formatPrice(course.mrp)
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        binding.btnLearnCourseAction.isVisible = !course.isSubscribed
        binding.btnLearnCourseAction.setText(R.string.learn_buy)
        Glide.with(binding.ivLearnDetailsPoster)
            .load(course.imageUrl)
            .centerCrop()
            .into(binding.ivLearnDetailsPoster)
        binding.ivLearnDetailsPoster.isVisible = true
        binding.ivLearnDetailsPoster.alpha = 1f
        binding.webLearnPreview.isVisible = false
        binding.progressLearnVideo.isVisible = false
        binding.tvLearnNowPlaying.isVisible = false

        val chapters = data.chapters.orEmpty()
        completedTopicIds.clear()
        completedTopicIds.addAll(loadCompletedTopicIds())
        contentAdapter.submitChapters(chapters, course.isSubscribed, completedTopicIds)
        binding.tvLearnNoLessons.isVisible = chapters.none { it.topics.orEmpty().isNotEmpty() }
        if (!hasAutoStarted) {
            resumeTopic()?.let {
                hasAutoStarted = true
                openTopic(it)
            }
        }
    }

    private fun handleCourseAction() {
        val course = details?.course ?: return
        if (!course.isSubscribed) {
            openDetailsPage(course.encryptedCourseId, course.purchaseUrl, course.name)
            return
        }
        val firstTopic = resumeTopic()
        if (firstTopic == null) {
            Toast.makeText(this, R.string.learn_no_preview_available, Toast.LENGTH_SHORT).show()
        } else {
            openTopic(firstTopic)
        }
    }

    private fun openDetailsPage(
        encryptedCourseId: String?,
        purchaseUrl: String?,
        courseName: String?
    ) {
        val detailsUrl = purchaseUrl?.takeIf { it.isNotBlank() }
            ?: encryptedCourseId?.takeIf { it.isNotBlank() }?.let { courseId ->
                Uri.parse(COURSE_DETAILS_URL)
                    .buildUpon()
                    .appendQueryParameter("id", courseId)
                    .build()
                    .toString()
            }
        if (detailsUrl == null) {
            Toast.makeText(this, R.string.learn_purchase_link_unavailable, Toast.LENGTH_SHORT).show()
            return
        }
        startActivity(LearnWebsiteActivity.newIntent(this, detailsUrl, courseName))
    }

    private fun openTopic(topic: LearnTopic) {
        val videoUrl = topic.videoEmbedUrl
        if (topic.isLocked || videoUrl.isNullOrBlank()) {
            Toast.makeText(this, R.string.learn_lesson_locked, Toast.LENGTH_SHORT).show()
            return
        }
        if (!isTrustedPlayerUrl(videoUrl)) {
            Toast.makeText(this, R.string.learn_no_preview_available, Toast.LENGTH_SHORT).show()
            return
        }

        waitingForInitialPlayerReveal = !binding.webLearnPreview.isVisible
        binding.webLearnPreview.apply {
            alpha = if (waitingForInitialPlayerReveal) 0f else 1f
            isVisible = true
        }
        binding.progressLearnVideo.isVisible = true
        activeTopic = topic
        contentAdapter.markPlaying(topic.topicId)
        binding.tvLearnNowPlaying.apply {
            text = getString(R.string.learn_now_playing, topic.topicName.orEmpty())
            isVisible = true
        }
        binding.webLearnPreview.loadDataWithBaseURL(
            BUNNY_PLAYER_ORIGIN,
            createPlayerHtml(videoUrl),
            HTML_MIME_TYPE,
            HTML_ENCODING,
            null
        )
        binding.scrollLearnCourse.post { binding.scrollLearnCourse.smoothScrollTo(0, 0) }
    }

    private fun configureInlinePlayer() {
        binding.webLearnPreview.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.setSupportZoom(false)
            webChromeClient = PlayerChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean = request.isForMainFrame && !isTrustedPlayerUrl(request.url.toString())

                override fun onPageFinished(view: WebView, url: String) {
                    binding.progressLearnVideo.isVisible = false
                    revealPlayerSmoothly()
                    activeTopic?.let(::startCompletionPolling)
                }
            }
        }
    }

    private fun revealPlayerSmoothly() {
        if (!waitingForInitialPlayerReveal) return
        waitingForInitialPlayerReveal = false
        binding.webLearnPreview.animate()
            .alpha(1f)
            .setDuration(PLAYER_FADE_DURATION_MS)
            .start()
        binding.ivLearnDetailsPoster.animate()
            .alpha(0f)
            .setDuration(PLAYER_FADE_DURATION_MS)
            .withEndAction {
                binding.ivLearnDetailsPoster.isVisible = false
                binding.ivLearnDetailsPoster.alpha = 1f
            }
            .start()
    }

    private inner class PlayerChromeClient : WebChromeClient() {
        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            if (fullscreenContainer != null) {
                callback.onCustomViewHidden()
                return
            }
            val container = FrameLayout(this@LearnCourseDetailsActivity).apply {
                setBackgroundColor(Color.BLACK)
                addView(
                    view,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
            fullscreenContainer = container
            fullscreenCallback = callback
            window.addContentView(
                container,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            fullscreenBackCallback.isEnabled = true
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            WindowCompat.getInsetsController(window, window.decorView).apply {
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.systemBars())
            }
        }

        override fun onHideCustomView() {
            closeFullscreen(notifyPlayer = false)
        }
    }

    private fun closeFullscreen(notifyPlayer: Boolean) {
        val container = fullscreenContainer ?: return
        (container.parent as? ViewGroup)?.removeView(container)
        fullscreenContainer = null
        val callback = fullscreenCallback
        fullscreenCallback = null
        fullscreenBackCallback.isEnabled = false
        if (notifyPlayer) callback?.onCustomViewHidden()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        WindowCompat.getInsetsController(window, window.decorView)
            .show(WindowInsetsCompat.Type.systemBars())
    }

    private fun createPlayerHtml(videoUrl: String): String {
        val source = Uri.parse(videoUrl)
        val pathSegments = source.pathSegments
        val embedUrl = source.buildUpon()
            .path("/embed/${pathSegments[1]}/${pathSegments[2]}")
            .appendQueryParameter("autoplay", "true")
            .appendQueryParameter("preload", "true")
            .build()
            .toString()
        val safeEmbedUrl = TextUtils.htmlEncode(embedUrl)
        return """
            <!doctype html>
            <html>
            <head>
              <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">
              <style>
                html,body,iframe{width:100%;height:100%;margin:0;padding:0;border:0;background:#000;overflow:hidden}
              </style>
              <script src="https://assets.mediadelivery.net/playerjs/playerjs-latest.min.js"></script>
            </head>
            <body>
              <iframe id="player" src="$safeEmbedUrl"
                allow="autoplay; fullscreen; encrypted-media; picture-in-picture"
                allowfullscreen></iframe>
              <script>
                window.sabkuchEnded=false;
                var player=new playerjs.Player(document.getElementById('player'));
                player.on('ready',function(){player.play();});
                player.on('play',function(){window.sabkuchEnded=false;});
                player.on('ended',function(){window.sabkuchEnded=true;});
              </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun startCompletionPolling(topic: LearnTopic) {
        completionPollJob?.cancel()
        completionPollJob = lifecycleScope.launch {
            while (isActive && activeTopic?.topicId == topic.topicId) {
                binding.webLearnPreview.evaluateJavascript(VIDEO_ENDED_SCRIPT) { result ->
                    if (result == "true" && activeTopic?.topicId == topic.topicId) {
                        markTopicCompleted(topic.topicId)
                    }
                }
                delay(COMPLETION_POLL_INTERVAL_MS)
            }
        }
    }

    private fun markTopicCompleted(topicId: Int) {
        // Rewatching an already-completed lesson skips the bookkeeping but must still
        // auto-advance, like Udemy's autoplay.
        if (completedTopicIds.add(topicId)) {
            val storedProgress = progressPreferences()
                .getStringSet(progressStorageKey(), emptySet())
                .orEmpty()
                .toMutableSet()
            storedProgress += "$courseId:$topicId"
            progressPreferences().edit()
                .putStringSet(progressStorageKey(), storedProgress)
                .apply()
            contentAdapter.markCompleted(topicId)
            Toast.makeText(this, R.string.learn_lesson_completed, Toast.LENGTH_SHORT).show()
        }
        completionPollJob?.cancel()
        autoAdvanceFrom(topicId)
    }

    private fun playableTopics(): List<LearnTopic> = details?.chapters.orEmpty()
        .flatMap { it.topics.orEmpty() }
        .filter { !it.isLocked && !it.videoEmbedUrl.isNullOrBlank() }

    /** Udemy-style resume: first playable lesson not yet completed, else the first playable one. */
    private fun resumeTopic(): LearnTopic? {
        val playable = playableTopics()
        return playable.firstOrNull { it.topicId !in completedTopicIds } ?: playable.firstOrNull()
    }

    /** Udemy-style autoplay: after a short beat, start the next playable lesson. */
    private fun autoAdvanceFrom(finishedTopicId: Int) {
        val playable = playableTopics()
        val next = playable.getOrNull(playable.indexOfFirst { it.topicId == finishedTopicId } + 1)
        if (next == null) {
            Toast.makeText(this, R.string.learn_course_completed, Toast.LENGTH_LONG).show()
            return
        }
        lifecycleScope.launch {
            delay(AUTO_ADVANCE_DELAY_MS)
            // The user may have tapped another lesson during the delay; don't hijack playback.
            if (activeTopic?.topicId == finishedTopicId) openTopic(next)
        }
    }

    private fun loadCompletedTopicIds(): Set<Int> {
        val coursePrefix = "$courseId:"
        return progressPreferences()
            .getStringSet(progressStorageKey(), emptySet())
            .orEmpty()
            .asSequence()
            .filter { it.startsWith(coursePrefix) }
            .mapNotNull { it.substringAfter(coursePrefix).toIntOrNull() }
            .toSet()
    }

    private fun progressPreferences() =
        getSharedPreferences(LEARN_PROGRESS_PREFERENCES, Context.MODE_PRIVATE)

    private fun progressStorageKey(): String {
        val userId = AppPrefs.getPrefsUserId(this).ifBlank { GUEST_USER_ID }
        return "completed_topics_$userId"
    }

    private fun isTrustedPlayerUrl(url: String): Boolean = runCatching {
        val uri = Uri.parse(url)
            uri.scheme.equals("https", ignoreCase = true) &&
            uri.host.equals(BUNNY_PLAYER_HOST, ignoreCase = true) &&
            uri.pathSegments.size >= MIN_PLAYER_PATH_SEGMENTS &&
            uri.pathSegments.first().equals("play", ignoreCase = true)
    }.getOrDefault(false)

    override fun onPause() {
        binding.webLearnPreview.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.webLearnPreview.onResume()
    }

    override fun onDestroy() {
        activeTopic = null
        completionPollJob?.cancel()
        closeFullscreen(notifyPlayer = false)
        binding.webLearnPreview.apply {
            stopLoading()
            loadUrl("about:blank")
            clearHistory()
            webChromeClient = null
        }
        binding.layoutLearnMedia.removeView(binding.webLearnPreview)
        binding.webLearnPreview.destroy()
        super.onDestroy()
    }

    private fun showLoading(loading: Boolean) {
        binding.progressLearnDetails.isVisible = loading
        if (loading) {
            binding.layoutLearnPlayerHeader.isVisible = false
            binding.scrollLearnCourse.isVisible = false
            binding.layoutLearnCourseError.isVisible = false
        }
    }

    private fun showError() {
        binding.layoutLearnPlayerHeader.isVisible = false
        binding.scrollLearnCourse.isVisible = false
        binding.layoutLearnCourseError.isVisible = true
    }

    private fun formatPrice(price: Double): String = if (price <= 0) {
        getString(R.string.learn_free)
    } else {
        String.format(Locale.US, "\u20B9%.0f", price)
    }

    companion object {
        private const val EXTRA_COURSE_ID = "extra_course_id"
        private const val COURSE_DETAILS_URL = "https://sabkuchworld.com/LearnCourseDetail.aspx"
        private const val BUNNY_PLAYER_HOST = "player.mediadelivery.net"
        private const val BUNNY_PLAYER_ORIGIN = "https://player.mediadelivery.net/"
        private const val HTML_MIME_TYPE = "text/html"
        private const val HTML_ENCODING = "UTF-8"
        private const val LEARN_PROGRESS_PREFERENCES = "learn_progress"
        private const val GUEST_USER_ID = "guest"
        private const val MIN_PLAYER_PATH_SEGMENTS = 3
        private const val COMPLETION_POLL_INTERVAL_MS = 1_000L
        private const val AUTO_ADVANCE_DELAY_MS = 1_500L
        private const val PLAYER_FADE_DURATION_MS = 220L
        private const val VIDEO_ENDED_SCRIPT =
            "window.sabkuchEnded===true"

        @JvmStatic
        fun newIntent(context: Context, courseId: Int): Intent =
            Intent(context, LearnCourseDetailsActivity::class.java)
                .putExtra(EXTRA_COURSE_ID, courseId)
    }
}
