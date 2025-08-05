package com.example.smallandroidmediaplayerapp.di

import android.content.Context
import com.example.smallandroidmediaplayerapp.data.repository.AudioRepositoryImpl
import com.example.smallandroidmediaplayerapp.domain.repository.AudioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AudioRepositoryModule {

    @Provides
    @Singleton
    fun provideAudioRepository(applicationContext: Context): AudioRepository {
        return AudioRepositoryImpl(context = applicationContext)
    }
}