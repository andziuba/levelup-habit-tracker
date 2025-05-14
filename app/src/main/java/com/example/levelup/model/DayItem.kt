package com.example.levelup.model

import java.time.LocalDate

data class DayItem(
    val date: LocalDate,
    val isCenter: Boolean = false,
    val isToday: Boolean = false
)