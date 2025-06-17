package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.data.local.DatabaseBuilder
import com.example.levelup.model.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val habitDao = DatabaseBuilder.getInstance(application).habitDao()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    fun loadHabitsForUser(userId: String) {
        viewModelScope.launch {
            habitDao.getHabitsForUser(userId).collect { list ->
                _habits.value = list
            }
        }
    }

    fun insertHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.insertHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch(Dispatchers.IO) {
            habitDao.deleteHabit(habit)
            habit.userId.let { loadHabitsForUser(it) }
        }
    }

    fun getHabitsForDate(date: LocalDate): StateFlow<List<Habit>> {
        return habits.map { list ->
            list.filter { habit ->
                val createdDate = Instant.ofEpochMilli(habit.createdAt)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                !date.isBefore(createdDate)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun toggleHabitCompletion(habit: Habit, date: LocalDate, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val dateKey = date.toString()
            val newCompletions = habit.completions.toMutableMap().apply {
                this[dateKey] = isCompleted
            }
            habitDao.updateCompletions(habit.id, newCompletions)
            habit.userId.let { loadHabitsForUser(it) }
        }
    }

    fun isHabitCompletedForDate(habit: Habit, date: LocalDate): Boolean {
        return habit.completions[date.toString()] ?: false
    }
}
