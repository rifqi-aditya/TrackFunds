package com.rifqi.trackfunds.feature.categories.ui.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.IconPickerBottomSheet
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.categories.ui.model.CategoryIcons
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditCategoryScreen(
    viewModel: AddEditCategoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Kumpulkan one-off effects dari ViewModel (MVI)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AddEditCategoryEffect.Saved,
                is AddEditCategoryEffect.Deleted -> onNavigateBack()

                is AddEditCategoryEffect.ShowMessage ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    AddEditCategoryContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryContent(
    uiState: AddEditCategoryUiState,
    onEvent: (AddEditCategoryEvent) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val currentIcon = uiState.iconIdentifier.ifBlank { CategoryIcons.list.firstOrNull() ?: "help" }
    var showDeleteConfirm by rememberSaveable { mutableStateOf(false) }
    val canDelete = uiState.canDelete

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = uiState.screenTitle,
                onNavigateBack = onNavigateBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Headline & subtext
            Text(
                text = if (canDelete) "Edit your category" else "Create a category",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Choose an icon and give your category a clear name so transactions stay organized.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Card: pilih ikon
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        DisplayIconFromResource(
                            identifier = currentIcon,
                            modifier = Modifier.size(28.dp),
                            contentDescription = "Category Icon"
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Category icon", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Select the most representative icon",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { onEvent(AddEditCategoryEvent.IconPickerOpened) }) {
                        Text("Select")
                    }
                }
            }

            // Card: form nama kategori
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GeneralTextInputField(
                        value = uiState.name,
                        onValueChange = { onEvent(AddEditCategoryEvent.NameChanged(it)) },
                        label = "Category name",
                        placeholder = "e.g., Groceries, Transport",
                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                        keyboardActions = KeyboardActions(
//                            onDone = { if (uiState.canSubmit && !uiState.isSaving) onEvent(AddEditCategoryEvent.SaveClicked) }
//                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (uiState.nameError != null) {
                        Text(
                            text = uiState.nameError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Action row (Delete + Save)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (canDelete) {
                    TextButton(
                        onClick = { showDeleteConfirm = true },
                        enabled = !uiState.isSaving,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                        Spacer(Modifier.width(8.dp))
                        Text("Delete")
                    }
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { onEvent(AddEditCategoryEvent.SaveClicked) },
                    enabled = uiState.canSubmit && !uiState.isSaving,
                    modifier = Modifier.widthIn(min = 140.dp)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(18.dp)
                                .padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(if (uiState.isSaving) "Saving..." else "Save")
                }
            }
        }
    }

    // Bottom sheet pemilih ikon
    if (uiState.isIconSheetVisible) {
        IconPickerBottomSheet(
            icons = CategoryIcons.list,
            selected = uiState.iconIdentifier.takeIf { it.isNotBlank() },
            onSelect = {
                onEvent(AddEditCategoryEvent.IconChanged(it))
                onEvent(AddEditCategoryEvent.IconPickerDismissed)
            },
            onDismiss = { onEvent(AddEditCategoryEvent.IconPickerDismissed) }
        )
    }

    // Dialog konfirmasi hapus
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            title = { Text("Delete category?") },
            text = {
                Text("This action cannot be undone. Transactions using this category may need to be updated.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onEvent(AddEditCategoryEvent.DeleteClicked)
                    }
                ) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}
