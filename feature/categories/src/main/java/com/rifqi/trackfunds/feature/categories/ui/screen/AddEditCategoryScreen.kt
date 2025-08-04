package com.rifqi.trackfunds.feature.categories.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.core.ui.components.IconPickerSheet
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.categories.ui.event.AddEditCategoryEvent
import com.rifqi.trackfunds.feature.categories.ui.model.CategoryIcons
import com.rifqi.trackfunds.feature.categories.ui.state.AddEditCategoryUiState
import com.rifqi.trackfunds.feature.categories.ui.viewmodel.AddEditCategoryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditCategoryScreen(
    viewModel: AddEditCategoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is SharedRoutes.Categories) {
                onNavigateBack()
            }
        }
    }

    AddEditCategoryContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryContent(
    uiState: AddEditCategoryUiState,
    onEvent: (AddEditCategoryEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(AddEditCategoryEvent.SaveClicked) }) {
                Icon(Icons.Default.Check, "Simpan")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ikon yang dipilih
            DisplayIconFromResource(
                identifier = uiState.iconIdentifier.ifBlank { "help" },
                contentDescription = "Ikon Kategori",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            )

            // Input untuk nama kategori
            GeneralTextInputField(
                value = uiState.name,
                onValueChange = { onEvent(AddEditCategoryEvent.NameChanged(it)) },
                label = "Category Name",
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Fill Category Name",
                singleLine = true
            )

            HorizontalDivider()

            // Area pemilihan ikon
            Text("Pilih Ikon", style = MaterialTheme.typography.titleMedium)
            IconPickerSheet(
                icons = CategoryIcons.list,
                selectedIconIdentifier = uiState.iconIdentifier,
                onIconSelected = { onEvent(AddEditCategoryEvent.IconChanged(it)) }
            )
        }
    }
}