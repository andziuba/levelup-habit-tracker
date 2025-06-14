package com.example.levelup.data.local

import androidx.room.TypeConverter
import com.example.levelup.model.Frequency

class Converters {
    @TypeConverter
    fun fromFrequency(value: Frequency): String = value.name

    @TypeConverter
    fun toFrequency(value: String): Frequency = Frequency.valueOf(value)

    @TypeConverter
    fun fromMap(map: Map<String, Boolean>): String {
        return map.entries.joinToString(",") { "${it.key}=${it.value}" }
    }

    @TypeConverter
    fun toMap(value: String): Map<String, Boolean> {
        return if (value.isEmpty()) {
            emptyMap()
        } else {
            value.split(",").associate {
                val (key, valueStr) = it.split("=")
                key to valueStr.toBoolean()
            }
        }
    }
}
