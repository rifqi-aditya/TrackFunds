package com.rifqi.trackfunds.feature.categories.ui.screen

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.model.SelectionItem
import com.rifqi.trackfunds.core.ui.screen.ReusableSelectionScreen
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.categories.ui.viewmodel.SelectCategoryViewModel

/**
 * Composable stateful yang bertindak sebagai entry point untuk fitur pemilihan kategori.
 * Menghubungkan ViewModel dengan UI reusable.
 */
@Composable
fun SelectCategoryScreen(
    viewModel: SelectCategoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToAddCategory: () -> Unit,
    onSearchClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val selectionItems = remember(uiState.categories) {
        uiState.categories.map { category ->
            SelectionItem(
                id = category.id,
                name = category.name,
                iconIdentifier = category.iconIdentifier
            )
        }
    }

    ReusableSelectionScreen(
        title = "Choose a category",
        items = selectionItems,
        isLoading = uiState.isLoading,
        onNavigateBack = onNavigateBack,
        onAddItemClicked = onNavigateToAddCategory,
        onItemSelected = { selectedId ->
            val selectedCategory = uiState.categories.find { it.id == selectedId }
            if (selectedCategory != null) {
                viewModel.onCategorySelected(selectedCategory)
                onNavigateBack()
            }
        },
        topBarActions = {
            IconButton(onClick = onSearchClicked) {
                Icon(Icons.Default.Search, contentDescription = "Search Category")
            }
        }
    )
}

@Preview(showBackground = true, name = "Reusable Selection Screen - Categories Light")
@Composable
fun SelectCategoryScreenLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        val dummyItems = listOf(
            SelectionItem(id = "1", name = "Makanan", iconIdentifier = "restaurant"),
            SelectionItem(id = "2", name = "Transportasi", iconIdentifier = "car"),
            SelectionItem(id = "3", name = "Belanja", iconIdentifier = "shopping_cart")
        )
        ReusableSelectionScreen(
            title = "Choose a category",
            items = dummyItems,
            isLoading = false,
            onNavigateBack = {},
            onItemSelected = {},
            onAddItemClicked = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Reusable Selection Screen - Categories Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SelectCategoryScreenDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        val dummyItems = listOf(
            SelectionItem(id = "1", name = "Gaji", iconIdentifier = "cash"),
            SelectionItem(id = "2", name = "Bonus", iconIdentifier = "growing_money")
        )
        ReusableSelectionScreen(
            title = "Choose a category",
            items = dummyItems,
            isLoading = false,
            onNavigateBack = {},
            onItemSelected = {},
            onAddItemClicked = {},
            topBarActions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
        )
    }
}