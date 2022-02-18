package com.example.android.politicalpreparedness.data.repository

import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.flow.Flow

interface ElectionRepository {
    suspend fun getUpcomingElections(): List<Election>
    suspend fun observeSavedElections(): Flow<List<Election>>
    suspend fun toggleFollowing(election: Election)
    fun observeIsFollowing(electionId: Int): Flow<Boolean>
}