package com.example.android.politicalpreparedness.data.repository

import com.example.android.politicalpreparedness.models.Election
import com.example.android.politicalpreparedness.models.Representative

interface RepresentativeRepository {
    suspend fun getRepresentatives(state: String, country: String): List<Representative>
}