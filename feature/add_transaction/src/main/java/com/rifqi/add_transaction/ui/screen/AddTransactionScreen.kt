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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.add_transaction.ui.components.AmountInputForm
import com.rifqi.add_transaction.ui.components.DateTimeDisplayRow
import com.rifqi.add_transaction.ui.components.FormSelectorCard
import com.rifqi.add_transaction.ui.components.NotesInputField
import com.rifqi.add_transaction.ui.components.TransactionTypeToggleButtons
import com.rifqi.add_transaction.ui.model.AccountDisplayItem
import com.rifqi.add_transaction.ui.model.AddTransactionUiState
import com.rifqi.add_transaction.ui.viewmodel.AddTransactionViewModel
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.time.LocalDate

/**
 * Stateful Composable (Container)
 * - Bertanggung jawab untuk mendapatkan ViewModel dan state.
 * - Menghubungkan logika ViewModel dengan UI.
 * - Ini yang akan dipanggil dari NavGraph.
 */
@Composable
fun AddTransactionScreen(
    viewModel: AddTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectCategory: (transactionType: String) -> Unit,
    onNavigateToSelectAccount: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.transactionSaved) {
        if (uiState.transactionSaved) {
            onNavigateBack()
            viewModel.resetTransactionSavedFlag()
        }
    }

    AddTransactionContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onSaveTransaction = { viewModel.saveTransaction() },
        onTransactionTypeSelected = { viewModel.onTransactionTypeSelected(it) },
        onAmountChange = { viewModel.onAmountChange(it) },
        onNotesChange = { viewModel.onNotesChange(it) },
        onDateClick = {
            // TODO: Buka Date Picker Dialog di sini,
            // lalu panggil viewModel.onDateSelected(newDate)
            println("Date field clicked")
        },
        onAccountClick = onNavigateToSelectAccount,
        onCategoryClick = { onNavigateToSelectCategory(uiState.selectedTransactionType.name) },
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
    onDateClick: () -> Unit,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onNotesChange: (String) -> Unit,

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBackIos, contentDescription = "Back")
                    }
                },
                // actions = { ... ikon aksi TopAppBar ... },
                windowInsets = TopAppBarDefaults.windowInsets
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
            DateTimeDisplayRow(
                selectedDate = uiState.selectedDate,
                onClick = {
                    onDateClick()
                }
            )

            TransactionTypeToggleButtons(
                selectedType = uiState.selectedTransactionType,
                onTypeSelected = onTransactionTypeSelected
            )

            FormSelectorCard(
                label = "Account",
                value = uiState.selectedAccount?.name ?: "Choose account",
                onClick = onAccountClick,
                leadingIconRes = uiState.selectedAccount?.iconIdentifier?.let {
                    // TODO: Panggil IconMapper di sini
                    // mapIconIdentifierToDrawableRes(it)
                    R.drawable.ic_wallet // Placeholder
                } ?: R.drawable.ic_wallet
            )

            AmountInputForm(
                value = uiState.amount,
                onValueChange = onAmountChange,
            )

            NotesInputField(
                value = uiState.notes,
                onValueChange = onNotesChange,
            )

            FormSelectorCard(
                label = "Category",
                value = uiState.selectedCategory?.name ?: "Choose a category",
                onClick = onCategoryClick,
                leadingIconRes = uiState.selectedCategory?.iconIdentifier?.let {
                    // TODO: Panggil IconMapper di sini
                    R.drawable.ic_diversity // Placeholder
                } ?: R.drawable.ic_diversity
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

@Preview(showBackground = true, name = "Add Transaction Content - Light")
@Composable
fun AddTransactionContentLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        val dummyState = AddTransactionUiState(
            amount = "50,000",
            selectedTransactionType = TransactionType.EXPENSE,
            selectedAccount = AccountDisplayItem(
                id = "acc",
                name = "Mbanking BCA",
                iconIdentifier = "ic_wallet_placeholder"
            ),
            selectedDate = LocalDate.now(),
            notes = "Makan siang di kantor.",
            selectedCategory = CategoryItem(
                id = "cat",
                name = "Makanan",
                iconIdentifier = "ic_restaurant",
                type = TransactionType.EXPENSE.toString()
            ),
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
            onNotesChange = {},
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
            selectedAccount = AccountDisplayItem(
                id = "acc",
                name = "Cash Wallet",
                iconIdentifier = "ic_cash"
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