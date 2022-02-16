package com.example.android.politicalpreparedness.data.repository

import com.example.android.politicalpreparedness.models.Election

interface ElectionRepository {
    suspend fun getUpcomingElections(): List<Election>
}