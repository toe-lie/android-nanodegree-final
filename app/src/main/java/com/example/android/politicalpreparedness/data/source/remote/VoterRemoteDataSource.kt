package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.api.services.CivicsApiService
import com.example.android.politicalpreparedness.di.IoDispatcher
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VoterRemoteDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: CivicsApiService
) {
    suspend fun getVoterInfo(address: String, electionId: Int): VoterInfoResponse {
        return withContext(ioDispatcher) {
            apiService.voterInfo(address, electionId)
        }
    }
}