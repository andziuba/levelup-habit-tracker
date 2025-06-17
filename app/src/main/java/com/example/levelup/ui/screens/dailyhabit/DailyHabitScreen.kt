package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.levelup.viewmodel.HabitViewModel
import com.example.levelup.viewmodel.AuthViewModel
import java.time.LocalDate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue



@Composable
fun DailyHabitScreen(
    navController: NavController,
    habitViewModel: HabitViewModel,
    authViewModel: AuthViewModel
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val currentUser by authViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            habitViewModel.loadHabitsForUser(uid)
        }
    }

    val habits by habitViewModel.getHabitsForDate(selectedDate).collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_habit") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            DayRow(
                today = LocalDate.now(),
                onDateSelected = { date -> selectedDate = date }
            )
            HabitListDisplay(
                habits = habits,
                date = selectedDate,
                viewModel = habitViewModel,
                authViewModel = authViewModel
            )
        }
    }
}
