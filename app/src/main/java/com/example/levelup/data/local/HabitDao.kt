package com.example.levelup.data.local

import androidx.room.*
import com.example.levelup.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE userId = :userId ORDER BY id DESC")
    fun getHabitsForUser(userId: String): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Delete(entity = Habit::class)
    suspend fun deleteHabit(habit: Habit)
}
