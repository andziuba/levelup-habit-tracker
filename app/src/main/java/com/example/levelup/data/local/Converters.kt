package com.example.levelup.data.local

import androidx.room.TypeConverter
import com.example.levelup.model.Frequency

class Converters {
    @TypeConverter
    fun fromFrequency(value: Frequency): String = value.name

    @TypeConverter
    fun toFrequency(value: String): Frequency = Frequency.valueOf(value)
}
