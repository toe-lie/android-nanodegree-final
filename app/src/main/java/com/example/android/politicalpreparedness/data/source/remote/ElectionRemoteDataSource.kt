package com.example.android.politicalpreparedness.data.source.remote

import com.example.android.politicalpreparedness.data.source.remote.api.services.CivicsApiService
import com.example.android.politicalpreparedness.di.IoDispatcher
import com.example.android.politicalpreparedness.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ElectionRemoteDataSource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: CivicsApiService
) {
    suspend fun getUpcomingElections(): List<Election> {
        return withContext(ioDispatcher) {
            val response = apiService.elections()
            response.elections
        }
    }
}