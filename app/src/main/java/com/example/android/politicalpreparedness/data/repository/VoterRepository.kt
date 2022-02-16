package com.example.android.politicalpreparedness.data.repository

import com.example.android.politicalpreparedness.models.Division
import com.example.android.politicalpreparedness.models.VoterInfoResponse

interface VoterRepository {
    suspend fun getVoterInfo(division: Division, electionId: Int): VoterInfoResponse
}