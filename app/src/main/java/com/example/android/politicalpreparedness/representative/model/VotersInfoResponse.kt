package com.example.android.politicalpreparedness.representative.model

import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.State
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VotersInfoResponse  (
    val election: Election,
    val state: List<State>? = null

)