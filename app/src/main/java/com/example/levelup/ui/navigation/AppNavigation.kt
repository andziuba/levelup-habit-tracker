package com.example.levelup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.components.BottomNavItem
import com.example.levelup.ui.screens.dailyhabit.DailyHabitScreen
import com.example.levelup.ui.screens.dailyhabit.AddHabitScreen
import com.example.levelup.ui.screens.friends.FriendsScreen
import com.example.levelup.ui.screens.leaderboard.LeaderboardScreen
import com.example.levelup.ui.screens.monthlycalendar.MonthlyCalendarScreen
import com.example.levelup.viewmodel.HabitViewModel
import com.example.levelup.viewmodel.AuthViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val habitViewModel: HabitViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Today.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Today.route) {
            DailyHabitScreen(
                navController = navController,
                habitViewModel = habitViewModel,
                authViewModel = authViewModel
            )
        }

        composable("daily_habits") {
            DailyHabitScreen(
                navController = navController,
                habitViewModel = habitViewModel,
                authViewModel = authViewModel
            )
        }


        composable("monthly_calendar") {
            MonthlyCalendarScreen(
                habitViewModel = habitViewModel,
                authViewModel = authViewModel
            )
        }


        composable("add_habit") {
            AddHabitScreen(
                navController = navController,
                habitViewModel = habitViewModel,
                authViewModel = authViewModel
            )
        }
        composable("friends") {
            FriendsScreen(viewModel = authViewModel)
        }

        composable("leaderboard") {
            LeaderboardScreen(viewModel = authViewModel)
        }
    }
}
