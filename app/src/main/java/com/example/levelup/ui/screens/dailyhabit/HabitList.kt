package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.model.Habit

@Composable
fun HabitListDisplay(habits: List<Habit>) {
    if (habits.isEmpty()) {
        Text(
            text = "Brak nawyków do wyświetlenia",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            habits.forEach { habit ->
                HabitItem(habit)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = "${habit.frequency.name.lowercase().replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
