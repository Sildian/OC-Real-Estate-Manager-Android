package com.openclassrooms.realestatemanager.model.sqlite.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.coremodel.Extra

/**************************************************************************************************
 * DAO for Extra
 *************************************************************************************************/

@Dao
interface ExtraDAO {

    @Query("SELECT * FROM Extra")
    fun getAllExtras(): LiveData<List<Extra>>

    @Query("SELECT * FROM Extra WHERE id=:id")
    fun getExtra(id:Int):LiveData<Extra>

    @Insert
    fun insertExtra(extra:Extra)
}