package com.rifqi.trackfunds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.ui.TrackFundsMainScreen
import com.rifqi.trackfunds.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val useDarkTheme = when (uiState.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                else -> {
                    isSystemInDarkTheme()
                }
            }

            TrackFundsTheme(
                darkTheme = useDarkTheme
            ) {
                TrackFundsMainScreen(viewModel)
            }
        }
    }
}