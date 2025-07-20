package com.rifqi.trackfunds.feature.splash.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.rifqi.trackfunds.feature.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // LaunchedEffect akan berjalan saat startDestination berubah dari null
    LaunchedEffect(uiState.startDestination) {
        uiState.startDestination?.let { destination ->
            // Lakukan navigasi dan hapus splash screen dari back stack
            navController.navigate(destination) {
                popUpTo("splash_route") {
                    inclusive = true
                }
            }
        }
    }

    // Tampilan UI sederhana untuk splash screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}