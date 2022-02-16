package com.example.android.politicalpreparedness.data.source.remote.api.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateJsonAdapter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @FromJson
    fun localDateFromJson(jsonString: String): LocalDate {
        return formatter.parse(jsonString, LocalDate::from)
    }

    @ToJson
    fun localDateToJson(localDate: LocalDate): String {
        return localDate.format(formatter)
    }
}