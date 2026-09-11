package com.lcgg.glyph_me_maybe.updater

import org.json.JSONException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateCheckerTest {
    @Test
    fun parseUpdateInfo_readsAllReleaseFields() {
        val update = UpdateChecker.parseUpdateInfo(
            """
            {
              "versionCode": 12,
              "versionName": "2.1.0",
              "downloadUrl": "https://example.com/update.apk",
              "sha256": "abc123"
            }
            """.trimIndent(),
        )

        assertEquals(
            UpdateInfo(
                versionCode = 12,
                versionName = "2.1.0",
                downloadUrl = "https://example.com/update.apk",
                sha256 = "abc123",
            ),
            update,
        )
    }

    @Test(expected = JSONException::class)
    fun parseUpdateInfo_rejectsMissingRequiredFields() {
        UpdateChecker.parseUpdateInfo("""{"versionCode": 12}""")
    }

    @Test
    fun isUpdateAvailable_onlyAcceptsHigherVersionCode() {
        val update = UpdateInfo(12, "2.1.0", "https://example.com/update.apk", "abc123")

        assertTrue(UpdateChecker.isUpdateAvailable(update, currentVersionCode = 11))
        assertFalse(UpdateChecker.isUpdateAvailable(update, currentVersionCode = 12))
        assertFalse(UpdateChecker.isUpdateAvailable(update, currentVersionCode = 13))
    }
}
