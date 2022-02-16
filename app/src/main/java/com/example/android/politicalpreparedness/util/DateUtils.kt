package com.example.android.politicalpreparedness.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    @JvmStatic
    fun formatLocalDate(date: LocalDate?): String {
        if (date == null) return ""

        val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.getDefault());
        return formatter.format(date)
    }
}