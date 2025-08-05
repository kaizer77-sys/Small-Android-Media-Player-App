package com.example.smallandroidmediaplayerapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smallandroidmediaplayerapp.presentation.common.HideNavigationBarOnly
import com.example.smallandroidmediaplayerapp.presentation.navigation.NavigationGraph
import com.example.smallandroidmediaplayerapp.ui.theme.SmallAndroidMediaPlayerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmallAndroidMediaPlayerAppTheme {
                HideNavigationBarOnly()

                NavigationGraph()
            }
        }
    }
}
