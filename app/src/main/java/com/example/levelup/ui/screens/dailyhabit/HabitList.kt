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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import com.example.levelup.viewmodel.AuthViewModel


@Composable
fun HabitListDisplay(
    habits: List<Habit>,
    date: LocalDate,
    viewModel: HabitViewModel,
    authViewModel: AuthViewModel
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
                    viewModel = viewModel,
                    authViewModel = authViewModel
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
    authViewModel: AuthViewModel,
    today: LocalDate = LocalDate.now()
) {
    val isCompleted = viewModel.isHabitCompletedForDate(habit, date)
    val isFutureDate = date.isAfter(today)
    var showDialog by remember { mutableStateOf(false) }
    val points = habit.points
    val durationText = "${habit.hours}h ${habit.minutes}m"

    // Calculate opacity based on completion status
    val contentAlpha = if (isCompleted) 0.6f else 1f

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
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Modified Checkbox - disabled when completed
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { isChecked ->
                        if (!isCompleted) { // Only allow checking, not unchecking
                            viewModel.toggleHabitCompletion(habit, date, isChecked, authViewModel)
                        }
                    },
                    enabled = !isFutureDate && !isCompleted, // Disable if already completed
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.tertiary,
                        uncheckedColor = MaterialTheme.colorScheme.tertiary,
                        checkmarkColor = MaterialTheme.colorScheme.onTertiary
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Row {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Points",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "$points pts",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Duration",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = contentAlpha),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = durationText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.padding(start = 8.dp),
                    enabled = !isCompleted // Disable delete button for completed habits
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete habit",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha)
                    )
                }
            }

            if (isCompleted) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = 1f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}
