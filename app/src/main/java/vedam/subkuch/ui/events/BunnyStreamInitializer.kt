package vedam.subkuch.ui.events

import android.content.Context
import net.bunny.api.BunnyStreamApi

/** Java-friendly bridge around Bunny's Kotlin-first SDK initialization API. */
object BunnyStreamInitializer {
    @JvmStatic
    fun initialize(context: Context, libraryId: String) {
        val parsedLibraryId = libraryId.toLongOrNull() ?: return
        BunnyStreamApi.initialize(context.applicationContext, null, parsedLibraryId)
    }
}
