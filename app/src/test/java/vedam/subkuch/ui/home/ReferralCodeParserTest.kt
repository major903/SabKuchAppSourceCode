package vedam.subkuch.ui.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReferralCodeParserTest {
    @Test
    fun extractsRawReferralCode() {
        assertEquals("KK47", ReferralCodeParser.extract("KK47"))
    }

    @Test
    fun extractsEncodedReferralParameter() {
        assertEquals(
            "AB 12",
            ReferralCodeParser.extract("utm_source=share&referrer=AB%2012")
        )
    }

    @Test
    fun ignoresOrganicCampaignMetadata() {
        assertNull(
            ReferralCodeParser.extract("utm_source=google-play&utm_medium=organic")
        )
    }

    @Test
    fun ignoresEmptyReferrer() {
        assertNull(ReferralCodeParser.extract("  "))
    }
}
