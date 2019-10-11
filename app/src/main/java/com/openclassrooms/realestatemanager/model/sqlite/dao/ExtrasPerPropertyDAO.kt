package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.coremodel.ExtrasPerProperty

/**************************************************************************************************
 * DAO for ExtrasPerProperty
 *************************************************************************************************/

@Dao
interface ExtrasPerPropertyDAO {

    @Query("SELECT * FROM ExtrasPerProperty WHERE propertyId=:propertyId")
    fun getPropertyExtras(propertyId:Int): LiveData<List<ExtrasPerProperty>>

    @Insert
    fun insertPropertyExtra(propertyExtra:ExtrasPerProperty)

    @Query("DELETE FROM ExtrasPerProperty WHERE propertyId=:propertyId")
    fun deletePropertyExtra(propertyId:Int)
}