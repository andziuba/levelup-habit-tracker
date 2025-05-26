package com.example.levelup.model

import java.io.Serializable

data class Habit(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val frequency: Frequency,
    val duration: Duration
) : Serializable

enum class Frequency {
    DAILY, WEEKLY, MONTHLY
}

data class Duration(
    val hours: Int,
    val minutes: Int
) : Serializable {
    override fun toString(): String {
        return "${hours}h ${minutes}m"
    }
}