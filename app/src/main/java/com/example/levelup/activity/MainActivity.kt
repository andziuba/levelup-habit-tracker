package com.example.levelup.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.components.AppTopBar
import com.example.levelup.ui.components.BottomNav
import com.example.levelup.ui.components.DrawerContent
import com.example.levelup.ui.navigation.AppNavigation
import com.example.levelup.ui.navigation.AuthNavigation
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.AuthViewModel
import com.example.levelup.viewmodel.ThemeViewModel
import kotlinx.coroutines.launch
import com.example.levelup.ui.screens.splash.SplashScreen

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    private fun routeToTitle(route: String): String {
        return when (route) {
            "daily_habits" -> "LevelUP"
            "friends" -> "Friends"
            "leaderboard" -> "Leaderboard"
            "achievements" -> "Achievements"
            "monthly_calendar" -> "Monthly View"
            else -> "LevelUP"
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            var showSplash by remember { mutableStateOf(true) }


            LevelUpTheme(darkTheme = isDarkTheme) {
                if (showSplash) {
                    SplashScreen(
                        modifier = Modifier.fillMaxSize(),
                        onAnimationComplete = { showSplash = false }
                    )
                } else {
                    val currentUser by authViewModel.currentUser.collectAsState()
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showTopMenu = currentRoute !in listOf("add_habit")

                    if (currentUser == null) {
                        AuthNavigation(
                            navController = navController,
                            viewModel = authViewModel,
                            onAuthSuccess = {}
                        )
                    } else {
                        val drawerState = rememberDrawerState(DrawerValue.Closed)
                        val scope = rememberCoroutineScope()

                        ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                DrawerContent(
                                    onLogoutClicked = { authViewModel.logout() },
                                    drawerState = drawerState,
                                    scope = scope,
                                    displayName = authViewModel.currentUser.value?.displayName
                                        ?: "User",
                                    score = authViewModel.currentUser.value?.score ?: 0,
                                    isDarkTheme = isDarkTheme,
                                    onThemeChange = { themeViewModel.toggleTheme() },
                                    onNavigateToDailyHabits = { navController.navigate("daily_habits") },
                                    onNavigateToAddHabit = { navController.navigate("add_habit") },
                                    onNavigateToFriends = { navController.navigate("friends") },
                                    onNavigateToLeaderboard = { navController.navigate("leaderboard") },
                                    onNavigateToMonth = { navController.navigate("monthly_calendar") }
                                )
                            }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Scaffold(
                                    topBar = {
                                        if (showTopMenu) {
                                            AppTopBar(
                                                title = currentRoute?.let { routeToTitle(it) } ?: "",
                                                scope = scope,
                                                drawerState = drawerState
                                            )
                                        }
                                    },
                                    bottomBar = { BottomNav(navController) },
                                    content = { innerPadding ->
                                        Box(modifier = Modifier.padding(innerPadding)) {
                                            AppNavigation(
                                                navController = navController,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}