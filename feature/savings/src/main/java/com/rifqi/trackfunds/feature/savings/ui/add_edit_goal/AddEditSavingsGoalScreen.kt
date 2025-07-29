package com.rifqi.trackfunds.feature.savings.ui.add_edit_goal

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.IconPicker
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.savings.ui.model.DefaultSavingsIcons
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavingsGoalScreen(
    viewModel: AddEditSavingsGoalViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()


    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddEditGoalSideEffect.NavigateBack -> {
                    onNavigateBack()
                }

                is AddEditGoalSideEffect.ShowSnackbar -> {

                }
            }
        }
    }

    if (uiState.showIconPicker) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(AddEditSavingsEvent.IconPickerDismissed) },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Please Select an Icon",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IconPicker(
                    icons = DefaultSavingsIcons.list,
                    selectedIconIdentifier = uiState.iconIdentifier,
                    onIconSelected = { identifier ->
                        viewModel.onEvent(AddEditSavingsEvent.IconIdentifierChanged(identifier))
                    },
                )
            }

        }
    }

    if (uiState.showDatePicker) {
        CustomDatePickerDialog(
            initialDate = uiState.targetDate ?: LocalDate.now(),
            onDismiss = { viewModel.onEvent(AddEditSavingsEvent.DatePickerDismissed) },
            onConfirm = { date -> viewModel.onEvent(AddEditSavingsEvent.DateSelected(date)) },
            showDialog = true
        )
    }

    AddEditSavingsGoalContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSavingsGoalContent(
    uiState: AddEditSavingsGoalState,
    onEvent: (AddEditSavingsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = uiState.pageTitle,
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(AddEditSavingsEvent.SaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                } else {
                    Text("Save")
                }
            }
        }
    ) { padding ->
        val iconBorderColor = if (uiState.iconError != null) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outline
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = iconBorderColor,
                        shape = CircleShape
                    )
                    .clickable { onEvent(AddEditSavingsEvent.ShowIconPickerClicked) },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.iconIdentifier.isNotBlank()) {
                    DisplayIconFromResource(
                        identifier = uiState.iconIdentifier,
                        contentDescription = "Savings Icon",
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ImageSearch,
                            contentDescription = null,
                            tint = if (uiState.iconError != null) MaterialTheme.colorScheme.error else LocalContentColor.current
                        )
                        Text("Select an icon", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            if (uiState.iconError != null) {
                Text(
                    text = uiState.iconError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            GeneralTextInputField(
                value = uiState.goalName,
                onValueChange = { onEvent(AddEditSavingsEvent.NameChanged(it)) },
                label = "Savings goal name",
                placeholder = "Enter the name of the savings goal",
                isError = uiState.goalNameError != null,
                errorMessage = uiState.goalNameError,
            )

            AmountInputField(
                value = uiState.targetAmount,
                onValueChange = { onEvent(AddEditSavingsEvent.TargetAmountChanged(it)) },
                label = "Target Amount",
                placeholder = "0",
                isError = uiState.targetAmountError != null,
                errorMessage = uiState.targetAmountError,
            )

            DatePickerField(
                label = "Target Date (Optional)",
                value = uiState.targetDate,
                onClick = { onEvent(AddEditSavingsEvent.DateSelectorClicked) },
                mode = DatePickerMode.FULL_DATE
            )
        }
    }
}

@Preview(showBackground = true, name = "Add Savings Goal - Empty")
@Composable
private fun AddEditSavingsGoalContentEmptyPreview() {
    TrackFundsTheme {
        AddEditSavingsGoalContent(
            uiState = AddEditSavingsGoalState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Add Savings Goal - Filled")
@Composable
private fun AddEditSavingsGoalContentFilledPreview() {
    TrackFundsTheme {
        AddEditSavingsGoalContent(
            uiState = AddEditSavingsGoalState(
                goalName = "Liburan ke Jepang",
                targetAmount = "20000000",
                iconIdentifier = "travel"
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}