package com.rifqi.trackfunds.core.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.components.GenericListItem
import com.rifqi.trackfunds.core.ui.model.SelectionItem
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

/**
 * Sebuah layar generik yang bisa digunakan kembali untuk menampilkan daftar
 * item yang bisa dipilih (misalnya, Kategori, Akun, dll).
 *
 * @param title Judul yang akan ditampilkan di TopAppBar.
 * @param items Daftar SelectionItem yang akan ditampilkan.
 * @param isLoading State untuk menampilkan progress indicator.
 * @param onNavigateBack Callback untuk aksi tombol kembali.
 * @param onItemSelected Callback yang dipanggil saat sebuah item dipilih, mengembalikan ID item tersebut.
 * @param onAddItemClicked Callback untuk aksi FloatingActionButton.
 * @param topBarActions Slot opsional untuk menambahkan ikon aksi kustom di TopAppBar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableSelectionScreen(
    title: String,
    items: List<SelectionItem>,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onItemSelected: (String) -> Unit,
    onAddItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
    topBarActions: @Composable RowScope.() -> Unit = {} // Slot kosong sebagai default
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                actions = topBarActions, // Gunakan slot untuk aksi
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItemClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add New Item")
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(
                    items = items,
                    key = { item -> item.id }
                ) { item ->
                    GenericListItem(
                        item = item,
                        onClick = { onItemSelected(item.id) }
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Transactions Screen Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ReusableSelectionScreenPreview() {
    TrackFundsTheme(darkTheme = true) {
        ReusableSelectionScreen(
            title = "Select Item",
            items = listOf(
                SelectionItem(id = "1", name = "Item 1", iconIdentifier = "icon_1"),
                SelectionItem(id = "2", name = "Item 2", iconIdentifier = "icon_2")
            ),
            isLoading = false,
            onNavigateBack = {},
            onItemSelected = {},
            onAddItemClicked = {}
        )
    }
}