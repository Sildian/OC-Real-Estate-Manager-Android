package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.coremodel.Realtor

/**************************************************************************************************
 * DAO for Realtor
 *************************************************************************************************/

@Dao
interface RealtorDAO {

    @Query("SELECT * FROM Realtor")
    fun getAllRealtors(): LiveData<List<Realtor>>

    @Query("SELECT * FROM Realtor WHERE id=:id")
    fun getRealtor(id:String):LiveData<Realtor>

    @Insert
    fun insertRealtor(realtor:Realtor)
}