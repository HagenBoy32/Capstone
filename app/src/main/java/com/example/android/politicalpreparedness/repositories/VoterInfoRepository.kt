package com.example.android.politicalpreparedness.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionSavedDatabase
import com.example.android.politicalpreparedness.database.VoterInfo
import com.example.android.politicalpreparedness.database.VoterInfoDatabase
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepository(
    private val voterInfoDatabase: VoterInfoDatabase,
    private val ElectionsavedDatabase: ElectionSavedDatabase,
    private val api: CivicsApiService) {

    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo>
        get() = _voterInfo

   private fun convertToVoterInfo(id: Int, response: VoterInfoResponse) : VoterInfo? {

        var voterInfo: VoterInfo? = null
        val state = response.state


        if (state?.isNotEmpty() == true) {
            val votingLocatinUrl: String? =
                state[0].electionAdministrationBody.votingLocationFinderUrl?.run {
                    this
                }

            val ballotInfoUrl: String? =
                state[0].electionAdministrationBody.ballotInfoUrl?.run {
                    this
                }

            voterInfo = VoterInfo(
                id = id,
                stateName = state[0].name,
               votingLocationUrl = votingLocatinUrl,
                ballotInformationUrl = ballotInfoUrl
            )
        }

        return voterInfo
    }

    suspend fun loadVoterInfo(id:Int) {
        withContext(Dispatchers.IO) {
            val data = voterInfoDatabase.get(id)
            data?.run {_voterInfo.postValue(this)}
        }
    }



}




