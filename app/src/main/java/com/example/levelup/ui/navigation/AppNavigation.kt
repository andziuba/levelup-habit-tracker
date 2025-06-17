package com.example.levelup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.ui.components.BottomNavItem
import com.example.levelup.ui.screens.achievement.AchievementScreen
import com.example.levelup.ui.screens.dailyhabit.DailyHabitScreen
import com.example.levelup.ui.screens.dailyhabit.AddHabitScreen
import com.example.levelup.ui.screens.friends.FriendsScreen
import com.example.levelup.ui.screens.leaderboard.LeaderboardScreen
import com.example.levelup.ui.screens.monthlycalendar.MonthlyCalendarScreen
import com.example.levelup.viewmodel.AchievementViewModel
import com.example.levelup.viewmodel.HabitViewModel
import com.example.levelup.viewmodel.AuthViewModel
import androidx.compose.runtime.getValue


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

        composable("achievements") {
            val achievementViewModel: AchievementViewModel = viewModel()
            val user by authViewModel.currentUser.collectAsState(initial = null)
            val habits by habitViewModel.habits.collectAsState()

            LaunchedEffect(user, habits) {
                val nonNullUser = user
                if (nonNullUser != null) {
                    // Calculate habits count and done count from the current habits list
                    val habitsCount = habits.size

                    // Count habits done today
                    val today = java.time.LocalDate.now()
                    val habitsDoneCount = habits.count { habit ->
                        habit.completions[today.toString()] == true
                    }

                    achievementViewModel.updateData(nonNullUser, habitsCount, habitsDoneCount)
                }
            }

            val achievements by achievementViewModel.achievements.collectAsState(initial = emptyList())

            AchievementScreen(achievements = achievements)
        }



    }
}
