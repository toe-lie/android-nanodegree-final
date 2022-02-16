package com.example.android.politicalpreparedness.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.data.source.local.db.daos.ElectionDao
import com.example.android.politicalpreparedness.data.source.local.db.typeconverters.LocalDateTypeConverter
import com.example.android.politicalpreparedness.data.source.local.db.typeconverters.OffsetDateTimeTypeConverter
import com.example.android.politicalpreparedness.models.Election

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTypeConverter::class, OffsetDateTimeTypeConverter::class)
abstract class ElectionDatabase : RoomDatabase() {
    abstract fun electionDao(): ElectionDao
}