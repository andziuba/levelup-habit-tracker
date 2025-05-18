package com.example.levelup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.levelup.ui.screens.auth.LoginScreen
import com.example.levelup.ui.screens.auth.RegisterScreen
import com.example.levelup.viewmodel.AuthViewModel

@Composable
fun AuthNavigation(
    navController: NavHostController,
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = onAuthSuccess,
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { saveState = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = onAuthSuccess,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
    }
}