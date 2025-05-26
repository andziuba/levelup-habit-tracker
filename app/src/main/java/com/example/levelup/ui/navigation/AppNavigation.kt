package com.example.levelup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.components.BottomNavItem
import com.example.levelup.ui.screens.dailyhabit.DailyHabitScreen
import com.example.levelup.ui.screens.dailyhabit.AddHabitScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Today.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Today.route) {
            DailyHabitScreen(navController)
        }
        composable("add_habit") {
            AddHabitScreen(
                navController = navController,
                onHabitAdded = { habit ->
                    // TODO: Przechowaj nawyk np. w ViewModel
                }
            )
        }
    }
}
