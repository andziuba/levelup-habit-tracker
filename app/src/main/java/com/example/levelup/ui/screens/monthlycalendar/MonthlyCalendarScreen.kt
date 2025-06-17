package com.example.levelup.ui.screens.monthlycalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*
import com.example.levelup.viewmodel.AuthViewModel
import com.example.levelup.viewmodel.HabitViewModel
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.levelup.ui.screens.dailyhabit.HabitListDisplay
import androidx.compose.runtime.*

@Composable
fun MonthlyCalendarScreen(
    modifier: Modifier = Modifier,
    habitViewModel: HabitViewModel,
    authViewModel: AuthViewModel
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val currentMonth = YearMonth.from(selectedDate)
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.value % 7 // Adjust for Sunday start (0)

    // Pobierz nawyki z ViewModel
    val habits by habitViewModel.habits.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        // Month and year header
        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Day of week headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            // Add empty cells for days before the first day of the month
            items(dayOfWeekOffset) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                )
            }

            // Add cells for each day of the month
            items(daysInMonth) { day ->
                val date = currentMonth.atDay(day + 1)
                val isSelected = date == selectedDate

                // Sprawdź czy są nawyki na ten dzień
                val hasHabits = habits.any { habit ->
                    habit.selectedDays.contains(date.dayOfWeek)
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { selectedDate = date },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (day + 1).toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurface
                        )
                        if (hasHabits) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = MaterialTheme.shapes.small
                                    )
                            )
                        }
                    }
                }
            }
        }

        // Dodaj sekcję wyświetlającą nawyki dla wybranego dnia
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Habits for ${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold
        )

        val dailyHabits = habits.filter { habit ->
            habit.selectedDays.contains(selectedDate.dayOfWeek)
        }

        HabitListDisplay(
            habits = dailyHabits,
            date = selectedDate,
            viewModel = habitViewModel
        )

    }
}