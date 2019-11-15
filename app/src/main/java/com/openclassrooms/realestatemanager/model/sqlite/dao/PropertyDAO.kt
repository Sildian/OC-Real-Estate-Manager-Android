package com.openclassrooms.realestatemanager.model.sqlite.dao

import android.database.Cursor
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

    @RawQuery(observedEntities = [Property::class])
    fun getProperties(query:SupportSQLiteQuery):LiveData<List<Property>>

    @Insert
    fun insertProperty(property:Property):Long

    @Update
    fun updateProperty(property: Property)

    /**For content provider**/

    @Query("SELECT * FROM Property")
    fun getAllPropertiesFromContentProvider():Cursor
}