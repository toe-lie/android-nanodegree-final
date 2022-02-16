package com.example.android.politicalpreparedness.data.repository.impl

import com.example.android.politicalpreparedness.data.repository.VoterRepository
import com.example.android.politicalpreparedness.data.source.remote.VoterRemoteDataSource
import com.example.android.politicalpreparedness.models.Division
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import javax.inject.Inject

class DefaultVoterRepository @Inject constructor(
    private val voterRemoteDataSource: VoterRemoteDataSource
) : VoterRepository {
    override suspend fun getVoterInfo(division: Division, electionId: Int): VoterInfoResponse {
        val address = String.format("%s, %s", division.state, division.country)
        return voterRemoteDataSource.getVoterInfo(address, electionId)
    }
}