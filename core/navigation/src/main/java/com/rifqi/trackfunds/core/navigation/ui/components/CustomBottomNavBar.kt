package com.rifqi.trackfunds.core.navigation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rifqi.trackfunds.core.navigation.model.NavigationItem

@Composable
fun CustomBottomNavBar(
    navController: NavController,
    items: List<NavigationItem>,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Pastikan ada 4 item untuk tata letak ini
    if (items.size != 4) {
        // Handle error atau tampilkan state kosong
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Panel dengan Cutout (tidak berubah)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(
                    BottomNavShape(
                        // Konversi Dp ke Px di dalam Composable
                        cornerRadius = with(LocalDensity.current) { 0.dp.toPx() },
                        dockRadius = with(LocalDensity.current) { 38.dp.toPx() },
                    ),
                )
                .background(MaterialTheme.colorScheme.surface)
                .navigationBarsPadding(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Grup Kiri (Item 0 & 1)
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val item1 = items[0]
                    val isSelected1 = currentDestination?.hierarchy?.any { it.route == item1.graphRoute::class.qualifiedName } == true
                    val icon1 = if (isSelected1) item1.selectedIcon else item1.unselectedIcon
                    BottomNavItem(
                        label = item1.title,
                        icon = icon1,
                        isSelected = isSelected1,
                        onClick = { navController.navigate(item1.graphRoute::class.qualifiedName!!) }
                    )
                    val item2 = items[1]
                    val isSelected2 = currentDestination?.hierarchy?.any { it.route == item2.graphRoute::class.qualifiedName } == true
                    val icon2 = if (isSelected2) item2.selectedIcon else item2.unselectedIcon
                    BottomNavItem(
                        label = item2.title,
                        icon = icon2,
                        isSelected = isSelected2,
                        onClick = { navController.navigate(item2.graphRoute::class.qualifiedName!!) }
                    )
                }

                // Spacer untuk celah FAB
                Spacer(modifier = Modifier.weight(0.4f))

                // Grup Kanan (Item 2 & 3)
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val item3 = items[2]
                    val isSelected3 = currentDestination?.hierarchy?.any { it.route == item3.graphRoute::class.qualifiedName } == true
                    val icon3 = if (isSelected3) item3.selectedIcon else item3.unselectedIcon
                    BottomNavItem(
                        label = item3.title,
                        icon = icon3,
                        isSelected = isSelected3,
                        onClick = { navController.navigate(item3.graphRoute::class.qualifiedName!!) }
                    )
                    val item4 = items[3]
                    val isSelected4 = currentDestination?.hierarchy?.any { it.route == item4.graphRoute::class.qualifiedName } == true
                    val icon4 = if (isSelected4) item4.selectedIcon else item4.unselectedIcon
                    BottomNavItem(
                        label = item4.title,
                        icon = icon4,
                        isSelected = currentDestination?.hierarchy?.any { it.route == item4.graphRoute::class.qualifiedName } == true,
                        onClick = { navController.navigate(item4.graphRoute::class.qualifiedName!!) }
                    )
                }
            }
        }
    }
}