package com.example.smallandroidmediaplayerapp.presentation.media_list

import com.example.smallandroidmediaplayerapp.domain.model.AudioFile

data class MediaListState(
    val isLoading: Boolean = false,
    val audioFiles: List<AudioFile> = emptyList(),
    val error: String? = null
)

