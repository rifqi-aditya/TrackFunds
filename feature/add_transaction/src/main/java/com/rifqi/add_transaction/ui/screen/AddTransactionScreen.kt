package com.rifqi.add_transaction.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.add_transaction.ui.components.AmountInputForm
import com.rifqi.add_transaction.ui.components.DateTimeDisplayRow
import com.rifqi.add_transaction.ui.components.FormSelectorCard
import com.rifqi.add_transaction.ui.components.NotesInputField
import com.rifqi.add_transaction.ui.components.TransactionTypeToggleButtons
import com.rifqi.add_transaction.ui.model.AddTransactionUiState
import com.rifqi.add_transaction.ui.viewmodel.AddTransactionViewModel
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Stateful Composable (Container)
 * - Bertanggung jawab untuk mendapatkan ViewModel dan state.
 * - Menghubungkan logika ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSelectCategory: (transactionType: String) -> Unit,
    onNavigateToSelectAccount: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    CustomDatePickerDialog(
        showDialog = uiState.showDatePicker,
        initialDate = uiState.selectedDate,
        onDismiss = {
            viewModel.onDatePickerDismissed()
        },
        onConfirm = { newDate ->
            viewModel.onDateSelected(newDate)
        }
    )

    // Efek untuk navigasi kembali setelah transaksi disimpan
    LaunchedEffect(uiState.isTransactionSaved) {
        if (uiState.isTransactionSaved) {
            onNavigateBack()
            viewModel.resetTransactionSavedFlag()
        }
    }

    AddTransactionContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveTransaction = { viewModel.onSaveTransaction() },
        onTransactionTypeSelected = { viewModel.onTransactionTypeSelected(it) },
        onAmountChange = { viewModel.onAmountChange(it) },
        onNotesChange = { viewModel.onNotesChange(it) },
        onDateClick = { viewModel.onDateSelectorClicked() },
        onAccountClick = onNavigateToSelectAccount,
        onCategoryClick = { onNavigateToSelectCategory(uiState.selectedTransactionType.name) }
    )
}

/**
 * Stateless Composable (Presentational)
 * - Hanya menerima state dan lambda.
 * - Tidak tahu tentang ViewModel atau Hilt.
 * - Sangat mudah untuk di-preview dan diuji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionContent(
    uiState: AddTransactionUiState,
    onNavigateBack: () -> Unit,
    onSaveTransaction: () -> Unit,
    onTransactionTypeSelected: (TransactionType) -> Unit,
    onAmountChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = { Text("Add transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Button(
                onClick = onSaveTransaction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 8.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            DateTimeDisplayRow(
                selectedDate = uiState.selectedDate,
                onClick = onDateClick
            )

            TransactionTypeToggleButtons(
                selectedType = uiState.selectedTransactionType,
                onTypeSelected = onTransactionTypeSelected
            )

            FormSelectorCard(
                label = "Account",
                value = uiState.selectedAccount?.name ?: "Choose account",
                onClick = onAccountClick,
                leadingIconRes = uiState.selectedAccount?.iconIdentifier,
            )

            AmountInputForm(
                value = uiState.amount,
                onValueChange = onAmountChange,
            )

            NotesInputField( // Menggunakan input field biasa, bukan selector
                value = uiState.notes,
                onValueChange = onNotesChange
            )

            uiState.selectedCategory?.let {
                // Jika kategori sudah dipilih, tampilkan detailnya
                FormSelectorCard(
                    label = "Category",
                    value = it.name,
                    onClick = onCategoryClick,
                    leadingIconRes = it.iconIdentifier
                )
            } ?: // Jika belum ada kategori yang dipilih, tampilkan placeholder
            FormSelectorCard(
                label = "Category",
                value = "Choose a category",
                onClick = onCategoryClick,
                leadingIconRes = ""
            )

            uiState.error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


// --- PREVIEW sekarang memanggil AddTransactionContent ---

// Data dummy untuk digunakan di preview
private val previewDummyAccount =
    AccountItem(
        id = "acc",
        name = "Mbanking BCA",
        iconIdentifier = "ic_wallet_placeholder",
        balance = BigDecimal("1000000")
    )
private val previewDummyCategory = CategoryItem(
    id = "cat",
    name = "Makanan",
    iconIdentifier = "ic_restaurant",
    type = TransactionType.EXPENSE
)

@Preview(showBackground = true, name = "Add Transaction Content - Light")
@Composable
fun AddTransactionContentLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        val dummyState = AddTransactionUiState(
            amount = "50,000",
            selectedTransactionType = TransactionType.EXPENSE,
            selectedAccount = previewDummyAccount,
            selectedDate = LocalDate.now(),
            notes = "Makan siang di kantor.",
            selectedCategory = previewDummyCategory,
            isLoading = false
        )
        AddTransactionContent(
            uiState = dummyState,
            onNavigateBack = {},
            onSaveTransaction = {},
            onTransactionTypeSelected = {},
            onAmountChange = {},
            onDateClick = {},
            onAccountClick = {},
            onCategoryClick = {},
            onNotesChange = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Add Transaction Content - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AddTransactionContentDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        val dummyState = AddTransactionUiState(
            amount = "1,500,000",
            selectedTransactionType = TransactionType.INCOME,
            selectedAccount = AccountItem(
                id = "acc", name = "Cash Wallet", iconIdentifier = "ic_cash",
                balance = BigDecimal("2000000")
            ),
            isLoading = false,
            error = "Jumlah tidak boleh melebihi saldo."
        )
        AddTransactionContent(
            uiState = dummyState,
            onNavigateBack = {},
            onSaveTransaction = {},
            onTransactionTypeSelected = {},
            onAmountChange = {},
            onDateClick = {},
            onAccountClick = {},
            onCategoryClick = {},
            onNotesChange = {}
        )
    }
}