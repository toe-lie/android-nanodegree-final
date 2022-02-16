package com.example.android.politicalpreparedness.data.repository.impl

import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import com.example.android.politicalpreparedness.data.source.remote.ElectionRemoteDataSource
import com.example.android.politicalpreparedness.models.Election
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultElectionRepository @Inject constructor(
    private val electionRemoteDataSource: ElectionRemoteDataSource
) : ElectionRepository {

    override suspend fun getUpcomingElections(): List<Election> {
        return electionRemoteDataSource.getUpcomingElections()
    }

}