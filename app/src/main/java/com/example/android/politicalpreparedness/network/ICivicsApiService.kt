package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.representative.model.RepresentativeResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ICivicsApiService {


    @GET("elections")
    fun getElections(): Deferred<ElectionResponse>


    @GET("voterinfo")
    fun getVoterInfoAsync(
        @Query("electionId") electionId: Int,
        @Query("address") address: String,
    ): Deferred<VoterInfoResponse>

    @GET("representatives")
    suspend fun getRepresentatives(
        @Query("address") address: String,
    ): RepresentativeResponse


}