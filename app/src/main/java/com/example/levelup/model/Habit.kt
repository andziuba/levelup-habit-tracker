package com.example.levelup.model

import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val name: String,
    val selectedDays: Set<DayOfWeek> = emptySet(),
    val hours: Int,
    val minutes: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val userId: String,
    val completions: Map<String, Boolean> = emptyMap()
) : Serializable
