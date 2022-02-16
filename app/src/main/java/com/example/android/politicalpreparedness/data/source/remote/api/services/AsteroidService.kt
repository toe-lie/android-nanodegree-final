package com.example.android.politicalpreparedness.data.source.remote.api.services

import com.example.android.politicalpreparedness.models.ElectionResponse
import retrofit2.http.GET

/**
 * A retrofit service to fetch data from Google Civics API.
 * Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    //TODO: Add elections API Call
    @GET("elections")
    suspend fun elections(): ElectionResponse

    //TODO: Add voterinfo API Call

    //TODO: Add representatives API Call
}
