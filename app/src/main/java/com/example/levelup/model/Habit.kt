package com.example.levelup.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val name: String,
    val frequency: Frequency,
    val hours: Int,
    val minutes: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String
) : Serializable

enum class Frequency {
    DAILY, WEEKLY, MONTHLY
}
