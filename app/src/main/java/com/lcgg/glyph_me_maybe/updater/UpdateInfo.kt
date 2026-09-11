package com.lcgg.glyph_me_maybe.updater

data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val sha256: String,
)