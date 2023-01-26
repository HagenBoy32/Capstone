package com.example.android.politicalpreparedness.election


import androidx.lifecycle.*
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.repositories.ElectionsRepository
import kotlinx.coroutines.launch



class ElectionsViewModel(application: Application) :AndroidViewModel(application) {

    private val database =  getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    val upcomingElections = electionsRepository.elections
    val savedElections = electionsRepository.savedElections

    init {
        viewModelScope.launch { electionsRepository.refreshElections() }
    }


}