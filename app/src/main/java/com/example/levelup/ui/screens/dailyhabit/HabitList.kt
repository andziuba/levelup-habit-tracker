package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.model.Habit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.example.levelup.viewmodel.HabitViewModel
import java.time.LocalDate

@Composable
fun HabitListDisplay(
    habits: List<Habit>,
    date: LocalDate,
    viewModel: HabitViewModel
) {
    if (habits.isEmpty()) {
        Text(
            text = "No habits to display",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            habits.forEach { habit ->
                HabitChecklistItem(
                    habit = habit,
                    date = date,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HabitChecklistItem(
    habit: Habit,
    date: LocalDate,
    viewModel: HabitViewModel
) {
    val isCompleted = viewModel.isHabitCompletedForDate(habit, date)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(width = 2.dp, color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { isChecked ->
                    viewModel.toggleHabitCompletion(habit, date, isChecked)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = habit.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}