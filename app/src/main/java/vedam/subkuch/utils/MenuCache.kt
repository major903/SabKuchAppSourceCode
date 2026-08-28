package vedam.subkuch.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vedam.subkuch.network.models.OMenu

/** Stores the last authenticated menu separately for each signed-in user. */
object MenuCache {
    private const val KEY_PREFIX = "PREFS_MENUS_"
    private val gson = Gson()
    private val menuListType = object : TypeToken<ArrayList<OMenu>>() {}.type

    fun load(context: Context): List<OMenu>? {
        val raw = AppPrefs.getInstance(context).sharedPreferences
            .getString(cacheKey(context), null)
            ?.takeIf { it.isNotBlank() }
            ?: return null
        return runCatching {
            val menus: ArrayList<OMenu> = gson.fromJson(raw, menuListType)
            stableOrder(menus)
        }.getOrNull()
    }

    fun save(context: Context, menus: List<OMenu>) {
        AppPrefs.getInstance(context).sharedPreferences.edit()
            .putString(cacheKey(context), gson.toJson(stableOrder(menus), menuListType))
            .apply()
    }

    fun stableOrder(menus: List<OMenu>): List<OMenu> = menus
        .sortedWith(compareBy<OMenu>({ it.SortOrder?.toIntOrNull() ?: Int.MAX_VALUE }, { it.MenuId }))
        .map { it.copy() }

    fun hasSameVisibleContent(first: List<OMenu>?, second: List<OMenu>): Boolean {
        if (first == null) return false
        return stableOrder(first).map(::visibleKey) == stableOrder(second).map(::visibleKey)
    }

    private fun visibleKey(menu: OMenu): Triple<Int, String?, Int?> =
        Triple(menu.MenuId, menu.name, menu.SortOrder?.toIntOrNull())

    private fun cacheKey(context: Context): String =
        KEY_PREFIX + AppPrefs.getPrefsUserId(context)
}
