package com.example.smallandroidmediaplayerapp.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.smallandroidmediaplayerapp.R
import com.example.smallandroidmediaplayerapp.presentation.common.HideNavigationBarOnly
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(moveToMediaList: () -> Unit) {

    HideNavigationBarOnly()

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.music_loader))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // Launch effect once when composable is first shown
    LaunchedEffect(Unit) {
        delay(2080)
        moveToMediaList()
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_splash)),
    ) {

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(360.dp)
            )

//            Spacer(modifier = Modifier.height(30.dp))
//
//            Text(
//                text = "Audiohat",
//                fontFamily = FontFamily(Font(R.font.medium)),
//                fontSize = 48.sp,
//                color = colorResource(R.color.black)
//            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SplashScreen() {
    SplashScreen(moveToMediaList = {})
}