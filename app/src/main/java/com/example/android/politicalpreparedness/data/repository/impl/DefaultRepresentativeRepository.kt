package com.example.android.politicalpreparedness.data.repository.impl

import com.example.android.politicalpreparedness.data.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.data.source.remote.RepresentativeRemoteDataSource
import com.example.android.politicalpreparedness.models.Representative
import javax.inject.Inject

class DefaultRepresentativeRepository @Inject constructor(
    private val representativeRemoteDataSource: RepresentativeRemoteDataSource
) : RepresentativeRepository {
    override suspend fun getRepresentatives(state: String, country: String): List<Representative> {
        val address = String.format("%s, %s", state, country)
        return representativeRemoteDataSource.getRepresentatives(address)
    }
}