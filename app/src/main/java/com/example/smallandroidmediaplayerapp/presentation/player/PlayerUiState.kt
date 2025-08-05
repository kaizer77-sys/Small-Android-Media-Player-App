package com.example.smallandroidmediaplayerapp.presentation.player

import com.example.smallandroidmediaplayerapp.domain.model.AudioFile

data class PlayerUiState(
    val audioFiles: List<AudioFile> = emptyList(),
    val currentIndex: Int = 0,
    val audioFile: AudioFile? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
)


