package com.rifqi.trackfunds.feature.home.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.home.ui.event.HomeEvent
import com.rifqi.trackfunds.feature.home.ui.state.HomeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentTransactionsCard(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onViewAllClick: () -> Unit,
    onTransactionClick: (String) -> Unit
) {
    val tabs = listOf("Expense", "Income")
    val pagerState = rememberPagerState(initialPage = uiState.selectedTabIndex) {
        tabs.size
    }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(pagerState.settledPage) {
        if (uiState.selectedTabIndex != pagerState.settledPage) {
            onEvent(HomeEvent.TabSelected(pagerState.settledPage))
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Recent Transactions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "See All",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.clickable { onViewAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryTabRow(
                selectedTabIndex = uiState.selectedTabIndex,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            uiState.selectedTabIndex,
                            matchContentSize = true
                        ),
                        width = Dp.Unspecified,
                        color = TrackFundsTheme.extendedColors.accent
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTabIndex == index,
                        onClick = {
                            onEvent(HomeEvent.TabSelected(index))
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = title) },
                        selectedContentColor = TrackFundsTheme.extendedColors.accent,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val heightPerItem = 60.dp
            val emptyStateHeight = 60.dp
            val maxHeight = 250.dp

            val pagerHeight = when (uiState.selectedTabIndex) {
                0 -> calculatePagerHeight(
                    uiState.recentExpenseTransactions.size,
                    heightPerItem,
                    emptyStateHeight,
                    maxHeight
                )

                else -> calculatePagerHeight(
                    uiState.recentIncomeTransactions.size,
                    heightPerItem,
                    emptyStateHeight,
                    maxHeight
                )

//                else -> calculatePagerHeight(
//                    uiState.recentSavingsTransactions.size,
//                    heightPerItem,
//                    emptyStateHeight,
//                    maxHeight
//                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(pagerHeight)
                    .animateContentSize()
            ) { page ->
                val transactionsToShow = when (page) {
                    0 -> uiState.recentExpenseTransactions
                    else -> uiState.recentIncomeTransactions
//                    else -> uiState.recentSavingsTransactions
                }

                if (transactionsToShow.isEmpty()) {
                    Text(
                        "No transactions available.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    return@HorizontalPager
                }

                TransactionList(
                    transactions = transactionsToShow,
                    onItemClick = onTransactionClick
                )
            }
        }
    }
}

private fun calculatePagerHeight(
    itemCount: Int,
    heightPerItem: Dp,
    emptyHeight: Dp,
    maxHeight: Dp
): Dp {
    return when {
        itemCount == 0 -> emptyHeight
        itemCount < 4 -> heightPerItem * itemCount
        else -> maxHeight
    }
}

@Composable
private fun TransactionList(
    transactions: List<Transaction>,
    onItemClick: (String) -> Unit
) {
    if (transactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No transactions available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(transactions, key = { it.id }) { transaction ->
                TransactionItem(
                    transactions = transaction,
                    onClick = { onItemClick(transaction.id) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}