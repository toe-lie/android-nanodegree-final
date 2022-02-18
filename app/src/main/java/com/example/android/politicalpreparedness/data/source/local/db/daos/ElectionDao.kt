package com.example.android.politicalpreparedness.data.source.local.db.daos

import androidx.room.*
import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Election)

    @Query("SELECT * FROM election_table")
    fun observeAll(): Flow<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun observeById(id: Int): Flow<Election?>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getById(id: Int): Election?

    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM election_table")
    fun clear()
}