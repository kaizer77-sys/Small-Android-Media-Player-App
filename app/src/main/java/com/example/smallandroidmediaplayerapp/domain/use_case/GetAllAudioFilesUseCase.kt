package com.example.smallandroidmediaplayerapp.domain.use_case

import com.example.smallandroidmediaplayerapp.domain.model.AudioFile
import com.example.smallandroidmediaplayerapp.domain.repository.AudioRepository


class GetAllAudioFilesUseCase(
    private val repository: AudioRepository
) {
    suspend operator fun invoke(): List<AudioFile> {
        return repository.getAllAudioFiles()
    }
}
