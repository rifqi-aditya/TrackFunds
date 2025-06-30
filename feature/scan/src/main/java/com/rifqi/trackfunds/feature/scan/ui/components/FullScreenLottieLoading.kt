package com.rifqi.trackfunds.feature.scan.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun FullScreenLottieLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            // Lapisan gelap semi-transparan sebagai latar belakang
            .background(MaterialTheme.colorScheme.surface)
            // Mencegah klik pada UI di belakangnya
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animasi Lottie
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.scan_loading)
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Teks di bawah animasi
            Text(
                text = "Processing receipt...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Your receipt is being processed. please wait",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(name = "Full Screen Loading - Light", showBackground = true)
@Preview(
    name = "Full Screen Loading - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FullScreenLottieLoadingPreview() {
    TrackFundsTheme {
        // Gunakan Box untuk mensimulasikan overlay di atas konten lain
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Konten palsu di belakang untuk melihat efek transparansi
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Content behind the overlay", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Tidak melakukan apa-apa di preview */ }) {
                    Text("A Button Behind")
                }
            }

            // 2. Tampilkan overlay loading di atasnya
            FullScreenLottieLoading()
        }
    }
}