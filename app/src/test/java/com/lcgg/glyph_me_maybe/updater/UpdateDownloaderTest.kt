package com.lcgg.glyph_me_maybe.updater

import java.io.File
import java.security.MessageDigest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateDownloaderTest {
    @Test
    fun verifySha256_acceptsMatchingDigest() {
        val file = temporaryFile("glyph-me-maybe-update")
        file.writeText("apk contents")

        try {
            val expected = sha256(file)

            assertTrue(UpdateDownloader.verifySha256(file, expected))
        } finally {
            file.delete()
        }
    }

    @Test
    fun verifySha256_isCaseInsensitive() {
        val file = temporaryFile("glyph-me-maybe-update")
        file.writeText("apk contents")

        try {
            val expected = sha256(file).uppercase()

            assertTrue(UpdateDownloader.verifySha256(file, expected))
        } finally {
            file.delete()
        }
    }

    @Test
    fun verifySha256_rejectsIncorrectDigest() {
        val file = temporaryFile("glyph-me-maybe-update")
        file.writeText("apk contents")

        try {
            assertFalse(UpdateDownloader.verifySha256(file, "0".repeat(64)))
        } finally {
            file.delete()
        }
    }

    private fun temporaryFile(prefix: String): File =
        File.createTempFile(prefix, ".apk")

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
