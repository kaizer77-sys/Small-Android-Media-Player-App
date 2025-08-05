package com.example.smallandroidmediaplayerapp.di

import com.example.smallandroidmediaplayerapp.domain.repository.AudioRepository
import com.example.smallandroidmediaplayerapp.domain.use_case.GetAllAudioFilesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAllAudioUseCase(repository: AudioRepository): GetAllAudioFilesUseCase {
        return GetAllAudioFilesUseCase(repository)
    }

}