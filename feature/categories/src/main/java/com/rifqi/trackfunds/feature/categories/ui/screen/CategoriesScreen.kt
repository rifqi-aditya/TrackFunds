package com.rifqi.trackfunds.feature.categories.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.feature.categories.ui.components.AddCategoryRow
import com.rifqi.trackfunds.feature.categories.ui.components.CategoryRow
import com.rifqi.trackfunds.feature.categories.ui.event.CategoryListEvent
import com.rifqi.trackfunds.feature.categories.ui.state.CategoryListUiState
import com.rifqi.trackfunds.feature.categories.ui.viewmodel.CategoryListViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoriesScreen(
    viewModel: CategoryListViewModel = hiltViewModel(),
    onNavigate: (AppScreen) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            onNavigate(screen)
        }
    }

    CategoryListContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryListContent(
    uiState: CategoryListUiState,
    onEvent: (CategoryListEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Manage Categories",
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    text = { Text("Income") }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    text = { Text("Expense") }
                )
            }
            HorizontalPager(state = pagerState) { page ->
                val categories =
                    if (page == 0) uiState.expenseCategories else uiState.incomeCategories
                val type = if (page == 0) TransactionType.EXPENSE else TransactionType.INCOME

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Text(
                            "Your Category",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    item {
                        AddCategoryRow(onClick = { onEvent(CategoryListEvent.AddCategoryClicked(type)) })
                    }
                    item {
                        Text(
                            "General Category",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(categories) { category ->
                        CategoryRow(
                            category = category,
                            onClick = { onEvent(CategoryListEvent.CategoryClicked(category.id)) }
                        )
                    }
                }
            }
        }
    }
}