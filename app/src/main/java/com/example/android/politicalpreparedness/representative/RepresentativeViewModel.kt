package com.example.android.politicalpreparedness.representative

import android.app.Application

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repositories.RepresentativesRepository
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application): AndroidViewModel(application) {


    private val repository = RepresentativesRepository(CivicsApiService)

    val representatives = repository.representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address


    private val _states = MutableLiveData<List<String>>()
    val states: LiveData<List<String>>
        get() = _states

    val selectedStateIndex = MutableLiveData<Int>()

    init {
        _address.value = Address("", "","","New York","")
        _states.value = application.resources.getStringArray(R.array.states).toList()
    }



    fun onSearchButtonClick() {
        refreshRepresentatives()
    }

    private fun refreshRepresentatives() {

        viewModelScope.launch {
            try {
                address.value!!.state = getSelectedState(selectedStateIndex.value!!)
                val addressStr = address.value!!.toFormattedString()
                repository.refreshRepresentatives(addressStr)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getSelectedState(stateIndex: Int) : String {
        return states.value!!.toList()[stateIndex]
    }



    fun refreshByCurrentLocation(address: Address) {

        val stateIndex = _states.value?.indexOf(address.state)
        if (stateIndex != null && stateIndex >= 0) {
            selectedStateIndex.value = stateIndex!!
            _address.value = address
            refreshRepresentatives()

        }
    }

}
