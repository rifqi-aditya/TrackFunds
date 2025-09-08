package com.rifqi.trackfunds

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.ui.MainScreen
import com.rifqi.trackfunds.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        splash.setKeepOnScreenCondition {
            mainViewModel.uiState.value.startDestination == null
        }

        enableEdgeToEdge()

        setContent {
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            TrackFundsTheme(
                appTheme = uiState.theme
            ) {
                uiState.startDestination?.let { startDest ->
                    MainScreen(
                        viewModel = mainViewModel,
                        startDestination = startDest
                    )
                }
            }
        }
    }
}
