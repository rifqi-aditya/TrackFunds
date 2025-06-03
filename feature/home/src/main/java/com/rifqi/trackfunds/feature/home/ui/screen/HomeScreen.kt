package com.rifqi.trackfunds.feature.home.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToAllTransactions: (String) -> Unit, // Contoh callback navigasi
    onNavigateToAddTransaction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
        // TODO: Tambahkan UI untuk balance, recent transactions, tombol view all
        // Contoh: Button(onClick = { onNavigateToAllTransactions("EXPENSE") }) { Text("View All Expenses") }
        // Contoh: Button(onClick = onNavigateToAddTransaction) { Text("Add New Transaction") }
    }
}