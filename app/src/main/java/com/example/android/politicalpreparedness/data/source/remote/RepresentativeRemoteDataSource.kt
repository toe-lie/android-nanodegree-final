package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.api.services.CivicsApiService
import com.example.android.politicalpreparedness.di.IoDispatcher
import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepresentativeRemoteDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: CivicsApiService
) {
    suspend fun getRepresentatives(address: String): List<Representative> {
        return withContext(ioDispatcher) {
            val response = apiService.representatives(address)
            response.offices.flatMap { office -> office.getRepresentatives(response.officials) }
        }
    }
}