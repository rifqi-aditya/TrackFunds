package com.rifqi.trackfunds.feature.transaction.ui.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// --- 1. Model Data Dummy ---

data class TransactionDetail(
    val amount: BigDecimal,
    val description: String,
    val categoryName: String,
    val categoryIconIdentifier: String,
    val date: LocalDateTime,
    val transactionType: String,
    val accountName: String,
    val items: List<LineItemDetail>,
    val receiptImageUrl: String?
)

data class LineItemDetail(
    val name: String,
    val quantity: Int,
    val price: BigDecimal
)

// Data statis untuk preview
val dummyDetailWithItems = TransactionDetail(
    amount = BigDecimal("-75000"),
    description = "Kopi dan Roti di Starbucks",
    categoryName = "Makanan & Minuman",
    categoryIconIdentifier = "ic_category_food",
    date = LocalDateTime.now(),
    transactionType = "Pengeluaran",
    accountName = "GoPay",
    items = listOf(
        LineItemDetail("Kopi Susu", 2, BigDecimal("20000")),
        LineItemDetail("Roti Coklat", 1, BigDecimal("15000"))
    ),
    receiptImageUrl = "https://example.com/receipt.jpg"
)

val dummyDetailWithoutItems = TransactionDetail(
    amount = BigDecimal("5000000"),
    description = "Gaji Bulanan",
    categoryName = "Gaji",
    categoryIconIdentifier = "ic_category_salary",
    date = LocalDateTime.now().minusDays(1),
    transactionType = "Pemasukan",
    accountName = "BCA",
    items = emptyList(), // Tidak ada rincian item
    receiptImageUrl = null  // Tidak ada struk
)


// --- 2. Screen & Content Composables ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    onNavigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    detail: TransactionDetail // Terima data detail
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        content = { paddingValues ->
            TransactionDetailContent(
                modifier = Modifier.padding(paddingValues),
                detail = detail
            )
        }
    )
}

@Composable
fun TransactionDetailContent(
    modifier: Modifier = Modifier,
    detail: TransactionDetail
) {
    val isExpense = detail.amount < BigDecimal.ZERO
    val amountColor = if (isExpense) MaterialTheme.colorScheme.error else Color(0xFF2E7D32)
    val formattedAmount = formatCurrency(detail.amount.abs())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Informasi Utama ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = detail.description,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Fastfood,
                    contentDescription = detail.categoryName,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = detail.categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = detail.date.format(
                    DateTimeFormatter.ofPattern(
                        "EEEE, dd MMMM yyyy, HH:mm",
                        Locale("in", "ID")
                    )
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Divider()

        // --- Detail Kontekstual ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DetailItem(label = "Tipe Transaksi", value = detail.transactionType)
            DetailItem(label = "Akun", value = detail.accountName)
        }

        // --- Rincian Tambahan (Kondisional) ---
        if (detail.items.isNotEmpty()) {
            Divider()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Rincian Barang",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                detail.items.forEach { item ->
                    LineItemRow(name = item.name, quantity = item.quantity, price = item.price)
                }
            }
        }

        if (detail.receiptImageUrl != null) {
            Divider()
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Struk",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    // Di sini Anda akan menggunakan AsyncImage dari Coil
                    Text(
                        "Placeholder untuk gambar struk",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// --- 3. Helper Composables & Functions ---

@Composable
fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun LineItemRow(name: String, quantity: Int, price: BigDecimal) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$name (x$quantity)", style = MaterialTheme.typography.bodyMedium)
        Text(
            formatCurrency(price * BigDecimal(quantity)),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}


// --- 4. Preview ---

@Preview(name = "Detail With Items", showBackground = true)
@Composable
fun TransactionDetailScreenWithItemsPreview() {
    TrackFundsTheme {
        TransactionDetailScreen(
            onNavigateBack = {},
            onEditClick = {},
            onDeleteClick = {},
            detail = dummyDetailWithItems
        )
    }
}

@Preview(name = "Detail Without Items", showBackground = true)
@Composable
fun TransactionDetailScreenWithoutItemsPreview() {
    TrackFundsTheme {
        TransactionDetailScreen(
            onNavigateBack = {},
            onEditClick = {},
            onDeleteClick = {},
            detail = dummyDetailWithoutItems
        )
    }
}