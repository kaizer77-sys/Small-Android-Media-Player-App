package com.example.smallandroidmediaplayerapp.presentation.media_list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.smallandroidmediaplayerapp.R


@Composable
fun PlayerSlider(
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
) {
    Slider(
        value = currentPosition.toFloat(),
        onValueChange = { onSeek(it) },
        valueRange = 0f..duration.toFloat(),
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = colorResource(R.color.main_color),
            activeTrackColor = colorResource(R.color.main_color),
            inactiveTrackColor = Color(0xFFB0B0B0),
            activeTickColor = Color.Transparent,
            inactiveTickColor = Color.Transparent
        )
    )
}
