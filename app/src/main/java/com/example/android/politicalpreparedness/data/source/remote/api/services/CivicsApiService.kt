package com.example.android.politicalpreparedness.data.source.remote.api.services

import com.example.android.politicalpreparedness.models.ElectionResponse
import com.example.android.politicalpreparedness.models.VoterInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A retrofit service to fetch data from Google Civics API.
 * Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call
    @GET("elections")
    suspend fun elections(): ElectionResponse

    //TODO: Add voterinfo API Call
    @GET("voterinfo")
    suspend fun voterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int
    ): VoterInfoResponse

    //TODO: Add representatives API Call
}
