package com.example.levelup.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.levelup.model.Habit

@Database(entities = [Habit::class], version = 2)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}
