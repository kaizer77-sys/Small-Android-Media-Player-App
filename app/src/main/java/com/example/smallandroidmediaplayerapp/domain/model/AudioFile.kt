package com.example.smallandroidmediaplayerapp.domain.model

import android.graphics.Bitmap

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String?,
    val duration: Long,
    val uri: String,
    val albumArt: Bitmap?,
)
