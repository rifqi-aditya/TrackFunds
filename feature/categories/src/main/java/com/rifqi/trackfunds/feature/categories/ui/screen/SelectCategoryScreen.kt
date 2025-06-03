package com.rifqi.trackfunds.feature.categories.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.categories.ui.components.CategoryListItem
import com.rifqi.trackfunds.feature.categories.ui.viewmodel.SelectCategoryViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryScreen(
    onNavigateBack: () -> Unit,
    onCategorySelected: (CategoryItem) -> Unit,
    onAddCategoryClicked: () -> Unit,
    onSearchActionClicked: () -> Unit,
    viewModel: SelectCategoryViewModel = hiltViewModel()
) {
    val categoriesToDisplay by viewModel.categoriesToDisplay.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose a category") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back_ios_new),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchActionClicked) {
                        Icon(Icons.Default.Search, contentDescription = "Search Categories")
                    }
                },
                windowInsets = TopAppBarDefaults.windowInsets
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCategoryClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add New Category")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Terapkan padding dari Scaffold
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(
                items = categoriesToDisplay,
                key = { category -> category.id }
            ) { category ->
                CategoryListItem(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}
val dummyCategoriesForPreview = listOf(
    CategoryItem(id = "1", name = "Makanan", type = "EXPENSE", iconIdentifier = R.drawable.ic_restaurant.toString()),
    CategoryItem(id = "2", name = "Transportasi", type = "EXPENSE", iconIdentifier = R.drawable.ic_shuttle_bus.toString()),
    CategoryItem(id = "3", name = "Gaji", type = "INCOME", iconIdentifier = R.drawable.ic_money_transfer.toString()),
    CategoryItem(id = "4", name = "Hiburan", type = "EXPENSE", iconIdentifier = R.drawable.ic_game_controller.toString())
)

class FakeSelectCategoryViewModel : ViewModel() {

    // Pastikan nama properti dan tipenya sesuai dengan SelectCategoryViewModel asli Anda
    private val _categoriesToDisplay = MutableStateFlow(dummyCategoriesForPreview)
    val categoriesToDisplay: StateFlow<List<CategoryItem>> = _categoriesToDisplay

    // Jika ada fungsi lain di SelectCategoryViewModel Anda (misalnya `onCategoryClick`),
    // Anda bisa mengimplementasikannya di sini dengan perilaku dummy atau kosong.
    // Contoh:
    // fun searchCategories(query: String) { /* do nothing for preview */ }
}

@Preview(showBackground = true, name = "Simple Select Category Screen - Light")
@Composable
fun SimpleSelectCategoryScreenLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        // Untuk preview, kita bisa buat ViewModel palsu atau langsung pass data dummy
        // Jika ViewModel mengambil argumen dari SavedStateHandle, preview langsungnya akan sulit
        // tanpa setup Hilt preview.
        SelectCategoryScreen(
            // categories = dummySimpleCategoriesForPreview.filter { it.type == TransactionType.EXPENSE }, // Contoh untuk preview
            onNavigateBack = {},
            onCategorySelected = {},
            onAddCategoryClicked = {},
            onSearchActionClicked = {}
            // Untuk preview dengan ViewModel nyata, perlu setup Hilt atau Fake ViewModel
        )
    }
}