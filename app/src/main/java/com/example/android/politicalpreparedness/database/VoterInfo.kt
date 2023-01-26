package com.example.android.politicalpreparedness.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DataBaseConstants.VOTER_INFO_TABLE_NAME)
data class VoterInfo(
    @PrimaryKey val id: Int,
    val stateName: String,
    val votingLocationUrl: String?,
    val ballotInformationUrl: String?)
