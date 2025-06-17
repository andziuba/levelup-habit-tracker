package com.example.levelup.ui.screens.dailyhabit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelup.model.Habit
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.example.levelup.viewmodel.HabitViewModel
import java.time.LocalDate
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.outlined.Delete


@Composable
fun HabitListDisplay(
    habits: List<Habit>,
    date: LocalDate,
    viewModel: HabitViewModel
) {
    val dayOfWeek = date.dayOfWeek

    val visibleHabits = habits.filter { habit ->
        habit.selectedDays.contains(dayOfWeek)
    }

    if (visibleHabits.isEmpty()) {
        Text(
            text = "No habits to display",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            visibleHabits.forEach { habit ->
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
    viewModel: HabitViewModel,
    today: LocalDate = LocalDate.now()
) {
    val isCompleted = viewModel.isHabitCompletedForDate(habit, date)
    val isFutureDate = date.isAfter(today)
    var showDialog by remember { mutableStateOf(false) }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete \"${habit.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit(habit)
                        showDialog = false
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { isChecked ->
                    viewModel.toggleHabitCompletion(habit, date, isChecked)
                },
                enabled = !isFutureDate,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.tertiary,
                    uncheckedColor = MaterialTheme.colorScheme.tertiary,
                    checkmarkColor = MaterialTheme.colorScheme.onTertiary
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = habit.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete habit",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
