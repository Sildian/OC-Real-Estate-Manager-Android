package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.model.coremodel.Property

/**************************************************************************************************
 * DAO for Property
 *************************************************************************************************/

@Dao
interface PropertyDAO {

    @Query("SELECT * FROM Property")
    fun getAllProperties(): LiveData<List<Property>>

    @Query("SELECT * FROM Property WHERE id=:id")
    fun getProperty(id:String):LiveData<Property>

    @Insert
    fun insertProperty(property:Property)

    @Update
    fun updateProperty(property: Property)
}