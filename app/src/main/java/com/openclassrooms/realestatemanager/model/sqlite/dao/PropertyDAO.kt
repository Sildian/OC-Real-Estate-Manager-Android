package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.coremodel.Property

/**************************************************************************************************
 * DAO for Property
 *************************************************************************************************/

@Dao
interface PropertyDAO {

    @Query("SELECT * FROM Property")
    fun getAllProperties(): LiveData<List<Property>>

    @Query("SELECT * FROM Property WHERE id=:id")
    fun getProperty(id:Int):LiveData<Property>

    @Query("SELECT * FROM Property WHERE firebaseId=:firebaseId")
    fun getProperty(firebaseId:String):LiveData<Property>

    @RawQuery(observedEntities = [Property::class])
    fun getProperties(query:SupportSQLiteQuery):LiveData<List<Property>>

    @Insert
    fun insertProperty(property:Property):Long

    @Update
    fun updateProperty(property: Property)
}