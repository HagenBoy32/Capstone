package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.ViewModelBase
import com.example.android.politicalpreparedness.database.ElectionDatabase.Companion.getInstance
import com.example.android.politicalpreparedness.repositories.ElectionsRepository
import kotlinx.coroutines.launch
import java.util.*

//TODO: Construct ViewModel and provide election datasource

class ElectionsViewModel(application: Application) : ViewModelBase(application) {

    //TODO: Create live data val for upcoming electionsl
    //TODO: Create live data val for saved elections
    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    //TODO: Create functions to navigate to saved or upcoming election voter info

    private val database =  getInstance(application)
    private val electionsRepository = ElectionsRepository(database)

    val upcomingElections = electionsRepository.elections
    val savedElections = electionsRepository.savedElections

    init {
        viewModelScope.launch { electionsRepository.refreshElections() }
    }


}