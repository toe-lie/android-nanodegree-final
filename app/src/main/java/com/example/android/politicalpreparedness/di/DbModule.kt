package com.example.android.politicalpreparedness.di

import android.content.Context
import android.os.Debug
import androidx.room.Room
import com.example.android.politicalpreparedness.data.source.local.db.ElectionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {
    @Provides
    fun provideElectionDao(db: ElectionDatabase) = db.electionDao()
}

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ElectionDatabase {
        val builder =
            Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
            )
                .enableMultiInstanceInvalidation()
                .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }
}