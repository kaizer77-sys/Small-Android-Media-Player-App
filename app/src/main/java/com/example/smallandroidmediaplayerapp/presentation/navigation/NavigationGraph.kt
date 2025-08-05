package com.example.smallandroidmediaplayerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.smallandroidmediaplayerapp.presentation.common.PermissionHandler
import com.example.smallandroidmediaplayerapp.presentation.media_list.MediaListScreen
import com.example.smallandroidmediaplayerapp.presentation.media_list.MediaListViewModel
import com.example.smallandroidmediaplayerapp.presentation.player.PlayerScreen
import com.example.smallandroidmediaplayerapp.presentation.player.PlayerViewModel
import com.example.smallandroidmediaplayerapp.presentation.splash.SplashScreen
import com.example.smallandroidmediaplayerapp.utils.Constant.URI_PATTERN
import com.example.smallandroidmediaplayerapp.utils.Screen

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash
        composable(route = Screen.Splash.route) {
            SplashScreen(moveToMediaList = {
                navController.navigate(Screen.MediaList.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        // Media List
        composable(route = Screen.MediaList.route) {
            PermissionHandler {
                val mediaListViewModel: MediaListViewModel = hiltViewModel()

                MediaListScreen(mediaListViewModel, onAudioClick = { audio, audioFiles ->
                    navController.navigate("${Screen.Player.route}/${audio.id}")
                })
            }
        }

        // Player with Deep Link
        composable(
            route = "${Screen.Player.route}/{audioId}",
            arguments = listOf(navArgument("audioId") { type = NavType.LongType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "$URI_PATTERN{audioId}" }
            )
        ) { backStackEntry ->
            val audioId = backStackEntry.arguments?.getLong("audioId") ?: 0L

            // to get the parent entry
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.MediaList.route)
            }

            // shared viewmodel to get the same audio files
            val mediaListViewModel: MediaListViewModel = hiltViewModel(parentEntry)
            val playerViewModel: PlayerViewModel = hiltViewModel()

            PlayerScreen(audioId = audioId, mediaListViewModel, playerViewModel)
        }

    }
}
