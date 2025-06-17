package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.Achievement
import com.example.levelup.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class AchievementViewModel : ViewModel() {

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements

    // Example data — in practice, pass these from repository or other ViewModels
    private var currentUser: User? = null
    private var habitsCount: Int = 0
    private var habitsDoneCount: Int = 0

    fun updateData(user: User, habitsCount: Int, habitsDoneCount: Int) {
        currentUser = user
        this.habitsCount = habitsCount
        this.habitsDoneCount = habitsDoneCount
        calculateAchievements()
    }

    private fun calculateAchievements() {
        val user = currentUser ?: return

        val list = listOf(
            Achievement(
                id = "first_habit_added",
                title = "First Habit Added",
                description = "Add your first habit",
                achieved = habitsCount >= 1
            ),
            Achievement(
                id = "habit_completed",
                title = "Habit Completed",
                description = "Complete your first habit",
                achieved = habitsDoneCount >= 1
            ),
            Achievement(
                id = "hundred_exp",
                title = "100 EXP Reached",
                description = "Earn 100 experience points",
                achieved = user.score >= 100
            )
        )
        viewModelScope.launch {
            _achievements.emit(list)
        }
    }

    fun updateData(user: User, habitViewModel: HabitViewModel) {
        val today = LocalDate.now()
        val habitsCount = habitViewModel.habitsCount
        val habitsDoneCount = habitViewModel.habitsDoneCountForDate(today)
        currentUser = user
        this.habitsCount = habitsCount
        this.habitsDoneCount = habitsDoneCount
        calculateAchievements()
    }

}
