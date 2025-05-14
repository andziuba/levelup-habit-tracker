package com.example.levelup.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Today : BottomNavItem("today", "Today", Icons.Default.Home)
    object Friends : BottomNavItem("friends", "Friends", Icons.Default.Group)
    object Leaderboard : BottomNavItem("leaderboard", "Leaderboard", Icons.Default.Leaderboard)
    object Achievements : BottomNavItem("achievements", "Achievements", Icons.Default.EmojiEvents)

    companion object {
        val items = listOf(Today, Friends, Leaderboard, Achievements)
    }
}

@Composable
fun BottomNav() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = navBackStackEntry?.destination?.route
                BottomNavItem.items.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Today.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Today.route) {
                DailyHabitScreen()
            }
            //composable(BottomNavItem.Friends.route) {}
            //composable(BottomNavItem.Leaderboard.route) {}
            //composable(BottomNavItem.Achievements.route) {}
        }
    }
}
