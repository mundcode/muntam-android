package com.mundcode.muntam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.mundcode.designsystem.theme.DarkColorPalette
import com.mundcode.designsystem.theme.LightColorPalette
import com.mundcode.designsystem.theme.White
import com.mundcode.muntam.navigation.MuntamNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendOpenEvent()
        initializeAdmob()

        setContent {
            MuntamApp()
        }
    }

    private fun initializeAdmob() {
        MobileAds.initialize(this) {}
    }

    // todo 리팩토링 필요
    private fun sendOpenEvent() {
        FirebaseAnalytics.getInstance(this).logEvent(FirebaseAnalytics.Event.APP_OPEN) {}
    }
}

@Composable
fun MuntamApp() {
    MuntamTheme(
        darkTheme = false // todo 다크테마 적용시 삭제
    ) {
        val navController = rememberNavController()
        val currentBackstack by navController.currentBackStackEntryAsState()

        Scaffold { innerPadding ->
            MuntamNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MuntamTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(White)

    val colors = if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = White // todo 다크테마 적용시 수정
        )
        DarkColorPalette
    } else {
        systemUiController.setStatusBarColor(
            color = White
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
