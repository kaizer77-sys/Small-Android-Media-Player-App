package com.example.smallandroidmediaplayerapp.presentation.player

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.smallandroidmediaplayerapp.domain.model.AudioFile
import com.example.smallandroidmediaplayerapp.domain.use_case.GetAllAudioFilesUseCase
import com.example.smallandroidmediaplayerapp.presentation.service.AudioService
import com.example.smallandroidmediaplayerapp.presentation.service.PlayerAudioManager
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val context: Application,
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerUiState())
    val state: StateFlow<PlayerUiState> = _state

    private var mediaController: MediaController? = null

    init {
        connectToAudioService()
    }

    private fun connectToAudioService() {
        val sessionToken = SessionToken(context, ComponentName(context, AudioService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                observePlayer()
            },
            MoreExecutors.directExecutor()
        )
    }

    fun loadAudio(audioId: Long, audioFiles: List<AudioFile>) {
        viewModelScope.launch {
            val index = audioFiles.indexOfFirst { it.id == audioId }
            if (index != -1) {
                _state.value = _state.value.copy(
                    audioFiles = audioFiles,
                    currentIndex = index,
                    audioFile = audioFiles[index],
                    currentPosition = 0L
                )

                // store list audioFiles in PlayerAudioManager to use it in AudioService
                PlayerAudioManager.audioFiles = audioFiles

                val intent = Intent(context, AudioService::class.java).apply {
                    action = "PLAY"
                    putExtra("AUDIO_URI", audioFiles[index].uri)
                    putExtra("AUDIO_ID", audioFiles[index].id)
                }
                context.startService(intent)
            }
        }
    }

    private fun observePlayer() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _state.value = _state.value.copy(isPlaying = isPlaying)
            }

            override fun onEvents(player: Player, events: Player.Events) {
                _state.value = _state.value.copy(
                    currentPosition = player.currentPosition
                )
            }
        })
    }

    fun togglePlayPause() {
        mediaController?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun seekTo(position: Float) {
        mediaController?.seekTo(position.toLong())
        _state.value = _state.value.copy(currentPosition = position.toLong())
    }

    fun next() {
        mediaController?.seekToNext()
    }

    fun previous() {
        mediaController?.seekToPrevious()
    }

    override fun onCleared() {
        super.onCleared()
        mediaController?.release()
        mediaController = null
    }
}
