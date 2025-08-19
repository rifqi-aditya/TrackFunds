package com.rifqi.trackfunds.feature.transaction.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatLocalDate
import com.rifqi.trackfunds.core.ui.utils.label
import com.rifqi.trackfunds.feature.transaction.ui.filter.components.SectionCard
import com.rifqi.trackfunds.feature.transaction.ui.filter.components.SelectableChip

@Composable
fun FilterRoute(
    onNavigateBack: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // handle one-off effect (navigate, show picker, etc.)
    LaunchedEffect(Unit) {
        viewModel.effect.collect { eff ->
            when (eff) {
                FilterEffect.NavigateBack -> onNavigateBack()
                FilterEffect.OpenDateRangePicker -> {
                    // TODO: buka DateRangePicker milikmu, lalu kirim FilterIntent.CustomDateChanged(start,end)
                }
            }
        }
    }

    FilterScreenContent(
        state = uiState,
        listState = listState,
        onIntent = viewModel::onIntent
    )
}

// --------------------------------------------------------
//  STATELESS UI
// --------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreenContent(
    state: FilterUiState,
    listState: LazyListState,
    onIntent: (FilterIntent) -> Unit
) {
    val activeCount =
        (if (state.selectedCategoryIds.isNotEmpty()) 1 else 0) +
                (if (state.selectedAccountIds.isNotEmpty()) 1 else 0) +
                (if (state.selectedTypes.isNotEmpty()) 1 else 0) + 1

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = stringResource(R.string.filter),
                onNavigateBack = { onIntent(FilterIntent.CancelClicked) },
                actions = {
                    TextButton(onClick = { onIntent(FilterIntent.ResetClicked) }) {
                        Text(
                            stringResource(R.string.reset)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onIntent(FilterIntent.CancelClicked) },
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.large
                    ) { Text(stringResource(R.string.cancel)) }

                    Button(
                        onClick = { onIntent(FilterIntent.ApplyClicked) },
                        modifier = Modifier.weight(2f),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TrackFundsTheme.extendedColors.accent,
                            contentColor = TrackFundsTheme.extendedColors.onAccent
                        )
                    ) {
                        Icon(Icons.Outlined.FilterList, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("${stringResource(R.string.apply_filters)} ($activeCount)")
                    }
                }
            }
        }
    ) { inner ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transaction Type
            item {
                SectionCard(title = stringResource(R.string.transaction_type)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TransactionType.entries.forEach { type ->
                            val selected = type in state.selectedTypes
                            SelectableChip(
                                text = type.name,
                                selected = selected,
                                onClick = { onIntent(FilterIntent.TypeToggled(type)) }
                            )
                        }
                    }
                }
            }

            // Category (collapsed 6, sisanya saat expand)
            item {
                val sortedAll = remember(state.allCategories) {
                    state.allCategories.sortedBy { it.name.lowercase() }
                }
                val selectedList = remember(state.selectedCategoryIds, sortedAll) {
                    sortedAll.filter { it.id in state.selectedCategoryIds }
                }
                val remainingList = remember(state.selectedCategoryIds, sortedAll) {
                    sortedAll.filter { it.id !in state.selectedCategoryIds }
                }
                val visibleCollapsed = remember(remainingList, state.initialVisibleCategoryCount) {
                    remainingList.take(state.initialVisibleCategoryCount)
                }
                val hiddenCount =
                    (remainingList.size - visibleCollapsed.size).coerceAtLeast(0)

                SectionCard(title = stringResource(R.string.category)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedList.forEach { cat ->
                            SelectableChip(
                                text = cat.name,
                                selected = true,
                                onClick = { onIntent(FilterIntent.CategoryToggled(cat.id)) },
                                leadingIcon = cat.iconIdentifier
                            )
                        }

                        // Sisa
                        val toShow =
                            if (state.showAllCategories) remainingList else visibleCollapsed
                        toShow.forEach { cat ->
                            SelectableChip(
                                text = cat.name,
                                selected = false,
                                onClick = { onIntent(FilterIntent.CategoryToggled(cat.id)) },
                                leadingIcon = cat.iconIdentifier
                            )
                        }

                        // Toggle See all / Show less
                        if (hiddenCount > 0 || state.showAllCategories) {
                            FilterChip(
                                selected = false,
                                onClick = { onIntent(FilterIntent.ToggleShowAllCategories) },
                                label = {
                                    Text(
                                        if (state.showAllCategories)
                                            stringResource(R.string.show_less)
                                        else
                                            stringResource(
                                                R.string.see_all_count, hiddenCount
                                            )
                                    )
                                },
                                shape = MaterialTheme.shapes.large
                            )
                        }
                    }
                }
            }

            // Account Source
            item {
                SectionCard(title = stringResource(R.string.account_source)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.allAccounts.forEach { acc ->
                            val selected = acc.id in state.selectedAccountIds
                            SelectableChip(
                                text = acc.name,
                                selected = selected,
                                onClick = { onIntent(FilterIntent.AccountToggled(acc.id)) },
                                leadingIcon = acc.iconIdentifier
                            )
                        }
                    }
                }
            }

            // Transaction Date
            item {
                SectionCard(title = stringResource(R.string.transaction_date)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DateRangeOption.entries.forEach { option ->
                                val selected = state.dateOption == option
                                SelectableChip(
                                    text = option.label(),
                                    selected = selected,
                                    onClick = { onIntent(FilterIntent.DateOptionSelected(option)) },
                                    leadingIcon = if (option == DateRangeOption.CUSTOM) {
                                        "calendar"
                                    } else null
                                )
                            }
                        }

                        if (
                            state.dateOption == DateRangeOption.CUSTOM &&
                            state.customStart != null && state.customEnd != null
                        ) {
                            SuggestionChip(
                                onClick = { onIntent(FilterIntent.DateOptionSelected(DateRangeOption.CUSTOM)) }, // buka date picker lewat Effect
                                label = {
                                    Text(
                                        "${formatLocalDate(state.customStart)} — ${
                                            formatLocalDate(
                                                state.customEnd
                                            )
                                        }"
                                    )
                                }
                            )
                            Text(
                                text = "(Demo) Rentang tanggal statis. Nanti hubungkan ke DateRangePicker.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }


            // Active summary (opsional)
            item {
                val summary = buildList {
                    if (state.selectedCategoryIds.isNotEmpty()) add("Category (${state.selectedCategoryIds.size})")
                    if (state.selectedAccountIds.isNotEmpty()) add("Account (${state.selectedAccountIds.size})")
                    if (state.selectedTypes.isNotEmpty()) add(state.selectedTypes.joinToString("/") {
                        it.name
                    })

                    if (state.dateOption == DateRangeOption.CUSTOM && state.customStart != null && state.customEnd != null) {
                        add(
                            "Custom: ${
                                formatLocalDate(
                                    state.customStart
                                )
                            } — ${
                                formatLocalDate(
                                    state.customEnd
                                )
                            }"
                        )
                    } else {
                        add(state.dateOption.label())
                    }
                }

                if (summary.isNotEmpty()) {
                    SectionCard(title = stringResource(R.string.active_filters, activeCount)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            summary.forEach { label ->
                                AssistChip(
                                    onClick = { /* optional: clear per filter */ },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
