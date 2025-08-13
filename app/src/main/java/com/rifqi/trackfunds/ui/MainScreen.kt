package com.rifqi.trackfunds.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.graphs.AppNavHost
import com.rifqi.trackfunds.core.navigation.model.navigationItemsLists
import com.rifqi.trackfunds.core.navigation.ui.components.CustomBottomNavBar
import com.rifqi.trackfunds.core.ui.components.AddTransactionDialog
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    startDestination: AppScreen
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.snackbarManager) {
        viewModel.snackbarManager.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { route ->
            navController.navigate(route) {
                popUpTo(navController.graph.id) { inclusive = true }
            }
        }
    }

    // Logic untuk menampilkan bottom bar (tidak berubah)
    val showBottomBar = navigationItemsLists.any { navItem ->
        currentDestination?.hierarchy?.any { dest ->
            dest.route == navItem.graphRoute::class.qualifiedName
        } == true
    }

    // Panggil komponen stateless, teruskan state dan event handlers
    TrackFundsMainContent(
        navController = navController,
        snackbarHostState = snackbarHostState,
        showBottomBar = showBottomBar,
        onFabClick = {
            viewModel.onEvent(MainEvent.FloatButtonClicked)
        },
        startDestination = startDestination
    )

    // Tampilkan dialog berdasarkan state dari ViewModel
    if (uiState.isAddActionDialogVisible) {
        AddTransactionDialog(
            onDismissRequest = { viewModel.onEvent(MainEvent.AddActionDialogDismissed) },
            onScanClicked = { viewModel.onEvent(MainEvent.ScanReceiptClicked) },
            onAddManuallyClicked = { viewModel.onEvent(MainEvent.AddTransactionManuallyClicked) }
        )
    }
}

@Composable
fun TrackFundsMainContent(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    showBottomBar: Boolean,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    startDestination: AppScreen

) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionColor = MaterialTheme.colorScheme.primary,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                CustomBottomNavBar(
                    navController = navController,
                    items = navigationItemsLists
                )
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = onFabClick, // Panggil lambda saat diklik
                    shape = CircleShape,
                    modifier = Modifier.offset(y = (+46).dp), // Offset yang disarankan
                    containerColor = TrackFundsTheme.extendedColors.accent
                ) {
                    DisplayIconFromResource(
                        identifier = "plus",
                        contentDescription = "Add Transaction",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.inverseOnSurface
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}