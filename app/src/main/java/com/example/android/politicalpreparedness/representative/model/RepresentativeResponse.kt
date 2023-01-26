package com.example.android.politicalpreparedness.representative.model


import com.example.android.politicalpreparedness.network.models.Official

data class RepresentativeResponse(
    val offices: List<Office>,
    val officials: List<Official>
)



