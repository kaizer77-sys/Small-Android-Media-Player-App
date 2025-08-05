package com.example.smallandroidmediaplayerapp.presentation.media_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smallandroidmediaplayerapp.domain.use_case.GetAllAudioFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val getAllAudioFilesUseCase: GetAllAudioFilesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MediaListState())
    val state: StateFlow<MediaListState> = _state


    fun loadAudioFiles() {
        viewModelScope.launch {
            _state.value = MediaListState(isLoading = true)
            try {
                val files = getAllAudioFilesUseCase()
                _state.value = MediaListState(audioFiles = files)
            } catch (e: Exception) {
                _state.value = MediaListState(error = e.message)
            }
        }
    }

}