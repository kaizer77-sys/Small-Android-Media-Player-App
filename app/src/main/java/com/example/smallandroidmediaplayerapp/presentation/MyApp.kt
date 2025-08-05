package com.example.smallandroidmediaplayerapp.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.smallandroidmediaplayerapp.utils.Constant.CHANNEL_ID
import com.example.smallandroidmediaplayerapp.utils.Constant.CHANNEL_NAME
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application()