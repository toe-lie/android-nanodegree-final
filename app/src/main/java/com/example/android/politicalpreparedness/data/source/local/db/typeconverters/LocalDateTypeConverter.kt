package com.example.android.politicalpreparedness.data.source.local.db.typeconverters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object LocalDateTypeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    @JvmStatic
    fun localDateFromString(value: String?): LocalDate? {
        return value?.let {
            return formatter.parse(value, LocalDate::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun localDateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }
}