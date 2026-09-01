package vedam.subkuch.ui.home

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal object ReferralCodeParser {
    fun extract(installReferrer: String?): String? {
        val raw = installReferrer?.trim().orEmpty()
        if (raw.isEmpty()) return null

        val components = raw.split('&')
        val explicitCode = components.firstNotNullOfOrNull { component ->
            val separator = component.indexOf('=')
            if (separator <= 0) return@firstNotNullOfOrNull null
            val key = decode(component.substring(0, separator))
            if (key.equals(REFERRER_KEY, ignoreCase = true)) {
                decode(component.substring(separator + 1)).trim().takeIf(String::isNotEmpty)
            } else {
                null
            }
        }
        if (explicitCode != null) return explicitCode

        val isCampaignMetadata = components.any { component ->
            decode(component.substringBefore('=')).startsWith(UTM_PREFIX, ignoreCase = true)
        }
        if (isCampaignMetadata) return null

        return decode(raw).trim().takeIf(String::isNotEmpty)
    }

    private fun decode(value: String): String = runCatching {
        URLDecoder.decode(value, StandardCharsets.UTF_8.name())
    }.getOrDefault(value)

    private const val REFERRER_KEY = "referrer"
    private const val UTM_PREFIX = "utm_"
}
