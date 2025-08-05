package com.example.smallandroidmediaplayerapp.utils

sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash_screen")
    object MediaList : Screen(route = "media_list_screen")
    object Player : Screen(route = "player_screen")
}