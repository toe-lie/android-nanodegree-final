package com.example.android.politicalpreparedness.models

import androidx.room.*
import com.squareup.moshi.*
import java.time.LocalDate

@Entity(tableName = "election_table")
data class Election(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "electionDay") val electionDay: LocalDate,
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
)