package com.example.android.politicalpreparedness.database
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import androidx.lifecycle.LiveData
@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(elections: List<Election>)

    @Query("SELECT * FROM election_table")
    fun getAll(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE id=:id")
    fun getElectionById(id: Int): LiveData<Election>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election): Long

    @Delete
    fun deleteElection(election: Election )

     @Query("SELECT * FROM  election_table")
    fun getAllElections(): LiveData<List<Election>>

     @Query("SELECT * FROM election_table where isSaved = 1")
     fun getSavedElections(): LiveData<List<Election>>


}