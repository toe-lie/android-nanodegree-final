package com.example.android.politicalpreparedness.data.source.local.db.daos

import androidx.room.*
import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table")
    fun observeAll(): Flow<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun observeById(id: Int): Flow<Election?>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getById(id: Int): Election?

    //TODO: Add delete query
    @Query("DELETE FROM election_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    //TODO: Add clear query
    @Query("DELETE FROM election_table")
    fun clear()
}