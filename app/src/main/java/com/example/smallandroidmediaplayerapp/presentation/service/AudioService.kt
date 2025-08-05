package com.example.smallandroidmediaplayerapp.presentation.service

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.smallandroidmediaplayerapp.presentation.MainActivity
import com.example.smallandroidmediaplayerapp.utils.Constant.URI_PATTERN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("${URI_PATTERN}0"),
            this,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val sessionActivityPendingIntent = PendingIntent.getActivity(
            this,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        mediaSession = MediaSession.Builder(this, player!!)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val action = intent?.action
        val audioUri = intent?.getStringExtra("AUDIO_URI")
        val audioId = intent?.getLongExtra("AUDIO_ID", -1L) ?: -1L

        when (action) {
            "PLAY" -> {
                if (!audioUri.isNullOrEmpty() && audioId != -1L) {
                    playAudio(audioUri, audioId)
                } else {
                    player?.play()
                }
            }
            "PAUSE" -> player?.pause()
            "STOP" -> {
                player?.pause()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
                stopSelf()
            }
        }

        return START_STICKY
    }

    @OptIn(UnstableApi::class)
    private fun playAudio(audioUri: String, audioId: Long) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("${URI_PATTERN}$audioId"),
            this,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        mediaSession?.setSessionActivity(pendingIntent)

        val currentAudioFiles = PlayerAudioManager.audioFiles
        val startIndex = currentAudioFiles.indexOfFirst { it.id == audioId }

        player?.apply {
            clearMediaItems()
            addMediaItems(currentAudioFiles.map { MediaItem.fromUri(it.uri) })
            prepare()
            if (startIndex >= 0) seekTo(startIndex, 0)
            play()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.release()
        player?.release()
        mediaSession = null
        player = null
    }
}
