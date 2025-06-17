package com.example.levelup.data.local

import androidx.room.TypeConverter
import java.time.DayOfWeek

class Converters {
    @TypeConverter
    fun fromDayOfWeekSet(set: Set<DayOfWeek>): String {
        return set.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toDayOfWeekSet(value: String): Set<DayOfWeek> {
        return if (value.isEmpty()) emptySet()
        else value.split(",").map { DayOfWeek.valueOf(it) }.toSet()
    }

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
