package com.example.android.politicalpreparedness.data.repository.impl

import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.data.source.local.ElectionLocalDataSource
import com.example.android.politicalpreparedness.data.source.remote.ElectionRemoteDataSource
import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultElectionRepository @Inject constructor(
    private val electionRemoteDataSource: ElectionRemoteDataSource,
    private val electionLocalDataSource: ElectionLocalDataSource
) : ElectionRepository {

    override suspend fun getUpcomingElections(): List<Election> {
        return electionRemoteDataSource.getUpcomingElections()
    }

    override suspend fun observeSavedElections(): Flow<List<Election>> {
        return electionLocalDataSource.observeElections()
    }

    override suspend fun toggleFollowing(election: Election) {
        electionLocalDataSource.toggleFollowing(election)
    }

    override fun observeIsFollowing(electionId: Int): Flow<Boolean> {
        return electionLocalDataSource.observeIsFollowing(electionId)
    }

}