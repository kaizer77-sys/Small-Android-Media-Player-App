package com.example.smallandroidmediaplayerapp.presentation.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.smallandroidmediaplayerapp.R
import com.example.smallandroidmediaplayerapp.presentation.media_list.MediaListViewModel
import com.example.smallandroidmediaplayerapp.presentation.media_list.components.PlayerSlider


@Composable
fun PlayerScreen(
    audioId: Long,
    mediaListViewModel: MediaListViewModel,
    playerViewModel: PlayerViewModel,
) {
    val state by playerViewModel.state.collectAsState()

    // Download song data when entering the screen
    LaunchedEffect(audioId) {
        val audioFiles = mediaListViewModel.state.value.audioFiles
        if (audioFiles.isNotEmpty()) {
            playerViewModel.loadAudio(audioId, audioFiles)
        }
    }


    val audioFile = state.audioFile
    val isPlaying = state.isPlaying
    val currentPosition = state.currentPosition

    if (audioFile == null) {
        // progress if audio file not download
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding()
                .background(colorResource(R.color.page)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // Album Art
            if (audioFile.albumArt != null) {
                Image(
                    bitmap = audioFile.albumArt.asImageBitmap(),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    bitmap = ImageBitmap.imageResource(R.drawable.music_note),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .padding(20.dp),
                    colorFilter = ColorFilter.tint(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = audioFile.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )

            // Artist
            Text(
                text = audioFile.artist ?: "Unknown",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Playback Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { playerViewModel.previous() }) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = colorResource(R.color.main_color)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(
                    onClick = { playerViewModel.togglePlayPause() },
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.main_color))
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }


                Spacer(modifier = Modifier.width(20.dp))

                IconButton(onClick = { playerViewModel.next() }) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = colorResource(R.color.main_color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // SeekBar
            val duration = audioFile.duration.coerceAtLeast(1L)
            PlayerSlider(
                currentPosition = currentPosition,
                duration = duration,
                onSeek = { playerViewModel.seekTo(it) }
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatTime(currentPosition))
                Text(text = formatTime(audioFile.duration))
            }
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}

