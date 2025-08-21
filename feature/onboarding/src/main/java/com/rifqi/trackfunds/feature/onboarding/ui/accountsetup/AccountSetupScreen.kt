package com.rifqi.trackfunds.feature.onboarding.ui.accountsetup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rifqi.trackfunds.core.ui.components.IconPickerBottomSheet
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@Composable
fun AccountSetupScreen(
    viewModel: AccountSetupViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                AccountSetupSideEffect.NavigateHome -> onNavigateHome()
                is AccountSetupSideEffect.ShowMessage -> {
                }
            }
        }
    }

    AccountSetupContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AccountSetupContent(
    uiState: AccountSetupUiState,
    onEvent: (AccountSetupEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentIcon = uiState.selectedIcon ?: uiState.availableIcons.firstOrNull()
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Start organizing your finances",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Create an account/wallet first so that your balance and transactions can be calculated correctly.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Kartu pilih ikon
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
                                contentDescription = "Ikon akun",
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Account icon", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "select the appropriate icon",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        TextButton(onClick = { onEvent(AccountSetupEvent.IconPickerOpened) }) {
                            Text("Select")
                        }
                    }
                }

                // Form
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
                        // Nama
                        GeneralTextInputField(
                            value = uiState.name,
                            onValueChange = { onEvent(AccountSetupEvent.NameChanged(it)) },
                            label = "Account name",
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (uiState.nameError != null) {
                            Text(
                                text = uiState.nameError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Saldo awal
                        AmountInputField(
                            value = uiState.initialBalance,
                            onValueChange = { onEvent(AccountSetupEvent.InitialBalanceChanged(it)) },
                            label = "Initial balance",
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (uiState.balanceError != null) {
                            Text(
                                text = uiState.balanceError,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { onEvent(AccountSetupEvent.SubmitClicked) },
                    enabled = uiState.canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
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

            if (uiState.isIconSheetVisible) {
                IconPickerBottomSheet(
                    icons = uiState.availableIcons,
                    selected = uiState.selectedIcon,
                    onSelect = { onEvent(AccountSetupEvent.IconSelected(it)) },
                    onDismiss = { onEvent(AccountSetupEvent.IconPickerDismissed) }
                )
            }
        }
    }
}
