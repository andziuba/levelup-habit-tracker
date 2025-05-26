package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DailyHabitScreen(navController: NavController) {
    HabitList(
        onAddTask = {
            navController.navigate("add_habit")
        }
    )
}


@Composable
fun HabitList(onAddTask: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            DayRow()
        }
    }
}
