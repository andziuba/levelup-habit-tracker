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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.levelup.ui.components.BottomNav
import com.example.levelup.ui.components.DrawerContent
import com.example.levelup.ui.navigation.AppNavigation
import com.example.levelup.ui.navigation.AuthNavigation
import com.example.levelup.ui.theme.LevelUpTheme
import com.example.levelup.viewmodel.AuthViewModel
import com.example.levelup.viewmodel.ThemeViewModel
import com.example.levelup.ui.components.TopMenuButton
import kotlinx.coroutines.launch
import com.example.levelup.ui.screens.splash.SplashScreen
import com.example.levelup.viewmodel.CalendarViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()

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
                    val currentUserState by authViewModel.currentUser.collectAsState()
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showTopMenu = currentRoute !in listOf("add_habit")

                    val currentUser = currentUserState

                    LaunchedEffect(currentUser) {
                        if (currentUser != null) {
                            val token = authViewModel.getGoogleAccessToken(
                                context = this@MainActivity
                            )
                            if (token != null) {
                                calendarViewModel.fetchEvents(token)
                            }
                        }
                    }

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

                                if (showTopMenu) {
                                    TopMenuButton(
                                        onClick = {
                                            scope.launch {
                                                if (drawerState.isClosed) drawerState.open()
                                                else drawerState.close()
                                            }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}