package com.example.levelup.data.local

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: LocalDatabase? = null

    fun getInstance(context: Context): LocalDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LocalDatabase::class.java,
                "habit_db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
        return INSTANCE!!
    }
}
