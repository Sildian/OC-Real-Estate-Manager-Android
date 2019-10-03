package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.coremodel.PropertyType

/**************************************************************************************************
 * DAO for PropertyType
 *************************************************************************************************/

@Dao
interface PropertyTypeDAO {

    @Query("SELECT * FROM PropertyType")
    fun getAllPropertyTypes(): LiveData<List<PropertyType>>

    @Insert
    fun insertPropertyType(propertyType: PropertyType)
}