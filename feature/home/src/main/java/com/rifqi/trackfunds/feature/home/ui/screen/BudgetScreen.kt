package com.rifqi.trackfunds.feature.home.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale


data class BudgetCategory(
    val name: String,
    val budgetedAmount: Float,
    val spentAmount: Float
) {
    val remainingAmount: Float
        get() = budgetedAmount - spentAmount

    val progress: Float
        get() = if (budgetedAmount > 0) spentAmount / budgetedAmount else 0f
}

fun formatRupiah(amount: Float): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    try {
        // Menghilangkan desimal untuk tampilan yang lebih bersih
        format.maximumFractionDigits = 0
        return format.format(amount)
    } catch (e: Exception) {
        return "Rp ${amount.toLong()}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen() {
    // Data Statis untuk contoh
    val budgetCategories = remember {
        listOf(
            BudgetCategory(
                name = "Makanan & Minuman",
                budgetedAmount = 2000000f,
                spentAmount = 1500000f
            ),
            BudgetCategory(name = "Transportasi", budgetedAmount = 750000f, spentAmount = 800000f),
            BudgetCategory(name = "Hiburan", budgetedAmount = 500000f, spentAmount = 250000f),
            BudgetCategory(
                name = "Tagihan & Utilitas",
                budgetedAmount = 1200000f,
                spentAmount = 1200000f
            ),
            BudgetCategory(name = "Belanja", budgetedAmount = 600000f, spentAmount = 350000f),
            BudgetCategory(name = "Lain-lain", budgetedAmount = 300000f, spentAmount = 0f),
        )
    }

    // Menghitung total dari data statis
    val totalBudget = remember { budgetCategories.sumOf { it.budgetedAmount.toDouble() }.toFloat() }
    val totalSpent = remember { budgetCategories.sumOf { it.spentAmount.toDouble() }.toFloat() }
    val totalRemaining = totalBudget - totalSpent
    val overallProgress = if (totalBudget > 0) totalSpent / totalBudget else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anggaran Bulanan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Filter Bulan
            item {
                MonthSelector()
            }

            // 2. Ringkasan Total
            item {
                TotalBudgetSummary(
                    totalBudget = totalBudget,
                    totalSpent = totalSpent,
                    totalRemaining = totalRemaining,
                    progress = overallProgress
                )
            }

            // 3. Judul Daftar
            item {
                Text(
                    text = "Anggaran per Kategori",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // 4. Daftar Kategori Budget
            items(budgetCategories) { category ->
                BudgetCategoryItem(category = category)
            }
        }
    }
}

// --- Composable untuk Pemilih Bulan ---
@Composable
fun MonthSelector() {
    var currentMonth by remember { mutableStateOf("Juli 2025") } // Contoh state

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { /* TODO: Logika ganti ke bulan sebelumnya */ }) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Bulan Sebelumnya")
        }
        Text(
            text = currentMonth,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        IconButton(onClick = { /* TODO: Logika ganti ke bulan berikutnya */ }) {
            Icon(Icons.Default.ArrowForwardIos, contentDescription = "Bulan Berikutnya")
        }
    }
}

// --- Composable untuk Ringkasan Total dengan Chart ---
@Composable
fun TotalBudgetSummary(
    totalBudget: Float,
    totalSpent: Float,
    totalRemaining: Float,
    progress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Total Pemakaian dari Total Budget",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Chart Lingkaran
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Detail Angka
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                SummaryDetail("Total Budget", formatRupiah(totalBudget))
                SummaryDetail(
                    "Terpakai",
                    formatRupiah(totalSpent),
                    color = MaterialTheme.colorScheme.error
                )
                SummaryDetail("Sisa", formatRupiah(totalRemaining), color = Color(0xFF008000))
            }
        }
    }
}

@Composable
fun SummaryDetail(label: String, amount: String, color: Color = Color.Unspecified) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = amount,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}


// --- Composable untuk setiap item Kategori Budget ---
@Composable
fun BudgetCategoryItem(category: BudgetCategory) {
    val remainingColor =
        if (category.remainingAmount < 0) MaterialTheme.colorScheme.error else Color(0xFF008000)

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${formatRupiah(category.spentAmount)} / ${formatRupiah(category.budgetedAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { category.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(MaterialTheme.shapes.small),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (category.remainingAmount >= 0) "Sisa ${formatRupiah(category.remainingAmount)}" else "Lebih ${
                    formatRupiah(
                        -category.remainingAmount
                    )
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = remainingColor,
                modifier = Modifier.align(Alignment.End),
                fontWeight = FontWeight.Medium
            )
        }
    }
}


// --- Preview untuk Android Studio ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetScreenPreview() {
    // Ganti YourAppTheme dengan nama tema aplikasi Anda
    // MaterialTheme {
    BudgetScreen()
    // }
}