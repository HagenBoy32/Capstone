package com.example.android.politicalpreparedness.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApiService

import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import java.lang.Exception

class ElectionsRepository(private val database: ElectionDatabase) {


    val elections: LiveData<List<Election>> = database.electionDao.getAllElections()
    val voterInfo = MutableLiveData<VoterInfoResponse>()


    fun getElection(id: Int) = database.electionDao.getElectionById(id)

    suspend fun getVoterInfo(electionId: Int, address: String) {

        try {
            withContext(Dispatchers.IO) {
                val response = CivicsApiService.retrofitService.getVoterInfoAsync(electionId, address).await()
                voterInfo.postValue(response)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertElection(election: Election) {
        Log.i("election", election.isSaved.toString())
        withContext(Dispatchers.IO) {
            database.electionDao.insertElection(election)
        }
    }


    suspend fun refreshElections() {

        try {
            withContext(Dispatchers.IO) {

                val electionResponse = CivicsApiService.retrofitService.getElections().await()

                database.electionDao.insertAll(electionResponse.elections)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    val savedElections: LiveData<List<Election>> = database.electionDao.getSavedElections()
}