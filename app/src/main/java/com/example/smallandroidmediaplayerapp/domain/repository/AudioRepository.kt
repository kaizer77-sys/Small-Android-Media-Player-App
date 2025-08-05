package com.example.smallandroidmediaplayerapp.domain.repository

import android.app.Application
import com.example.smallandroidmediaplayerapp.domain.model.AudioFile

interface AudioRepository {
    suspend fun getAllAudioFiles(): List<AudioFile>
}