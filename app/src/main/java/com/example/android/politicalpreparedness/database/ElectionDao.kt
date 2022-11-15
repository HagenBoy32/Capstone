package com.example.android.politicalpreparedness.database
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import androidx.lifecycle.LiveData
@Dao
interface ElectionDao {

    //TODO: Add insert query
    //TODO: Add select all election query
    //TODO: Add select single election query
    //TODO: Add delete query
    //TODO: Add clear query

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(election: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election): Long

     @Query("SELECT * FROM  election_table")
    fun getAllElections(): LiveData<List<Election>>

     @Query("SELECT * FROM election_table where isSaved = 1")
     fun getSavedElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE  id=:id")
    fun getElectionById(id: Int): LiveData<Election>

    @Query("DELETE FROM election_table where id=:id")
    fun deleteElection(id: Int)


}